package client.commands;

import client.Client;
import game.ClientGameController;
import serverclient.User;

/**
 * Starts the game after the server has started the game and informed this and all other Clients with this command.
 *
 * Created by flavia on 11.04.17.
 */
public class ClientStgamHandler extends ClientCommandHandler {
	
	/**
	 * Executes the command from the server.
	 */
	@Override
	public void handleCommand() {
		if (Client.isLoggedIn() == false)
			return;
		
		String gameName = getAndRemoveNextArgumentWord();
		String username = getAndRemoveNextArgumentWord();
		ClientGameController gameController = Client.getGameByName(gameName);
		User user = Client.getUserByName(username);
		
		gameController.startGame(user);
		
	}
	
	
	@Override
	public void handleAnswer(boolean isOK) {
	
	}
	
	/**
	 * Sends a message telling the server to start the game.
	 * @param gameName The name of the game to start.
	 */
	public static void sendStartGame(String gameName) {
		Client.sendMessageToServer(String.format("stgam %s %s", gameName, Client.getThisUser().getName()));
	}
	
}
