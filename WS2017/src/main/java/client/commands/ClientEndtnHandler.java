package client.commands;

import client.Client;
import client.ClientUser;
import game.ClientGameController;

/**
 * Sends the request to give up the turn to the server. The server then
 *
 * Created by flavia on 15.04.17.
 */
public class ClientEndtnHandler extends ClientCommandHandler {
	/**
	 * UNUSED
	 */
	@Override
	public void handleCommand() {}
	
	/**
	 * Executes the answer received from the server to the specific command this class is for.
	 *
	 * @param isOK <code>true</code> if the answer started with "+OK", <code>false</code> otherwise (answer started with "-ERR").
	 */
	@Override
	public void handleAnswer(boolean isOK) {
		String gameName = getAndRemoveNextArgumentWord();
		if (isOK) {
			String username = getAndRemoveNextArgumentWord();
			ClientGameController gameController = Client.getGameByName(gameName);
			ClientUser user = Client.getUserByName(username);
			
			assert gameController != null;
			assert user != null;
			
			if (gameController.getWorld().getTurnController().getCurrentPlayer().getUser() != user) {
				System.err.println("ClientEndtnHandler#handleAnswer - it's not user's turn, but server didn't get that.");
				return;
			}
			
			gameController.getWorld().getTurnController().endTurn();
		
		} else if (isOK == false && argument.startsWith("its not your turn")) {
			System.out.println("ClientEndtnHandler#handleAnswer - its not your turn");
			return;
			
		}
	}
	
	/**
	 * Asks the server to give up the current turn.
	 * @param gameName The name of the game of which the user gives up the turn.
	 */
	public static void askServerToEndTurn(String gameName) {
		String username = Client.getThisUser().getName();
		
		Client.sendMessageToServer(String.format("endtn %s %s", gameName, username));
	}
}
