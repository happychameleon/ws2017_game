package client.commands;

import client.Client;
import client.ClientUser;
import game.ClientGameController;

/**
 * Adds the joined user to the game on the client side when the server informs this Client about the newly joined user.
 *
 * Created by flavia on 31.03.17.
 */
public class ClientJoingHandler extends CommandHandler {
	
	@Override
	public void handleCommand() {
		if (Client.isLoggedIn() == false)
			return;
		String gameName = argument.substring(0, argument.indexOf(" "));
		String username = argument.substring(argument.indexOf(" ") + 1);
		ClientUser joinedUser = Client.getUserByName(username);
		ClientGameController game = Client.getMainWindow().getWaitingGameByName(gameName);
		
		if (joinedUser == null || game == null) {
			System.err.println("USER OR GAME DOESN'T EXIST!");
			return;
		}
		
		game.addUserToGame(joinedUser);
	}
	
	@Override
	public void handleAnswer(boolean isOK) {
	
	}
	
	/**
	 * Sends the message to the server asking to join the specified game.
	 * @param gameName The name of the game this user wants to join.
	 */
	public static void sendJoingRequest(String gameName) {
		Client.sendMessageToServer(String.format("joing %s %s", gameName, Client.getThisUser().getName()));
	}
}
