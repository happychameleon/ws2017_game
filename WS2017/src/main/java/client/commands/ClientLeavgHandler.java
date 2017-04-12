package client.commands;

import client.Client;
import game.ClientGameController;
import serverclient.User;

/**
 * Removes the user from the game on the Client side (opposite of {@link ClientJoingHandler}).
 *
 * Created by flavia on 01.04.17.
 */
public class ClientLeavgHandler extends CommandHandler {
	
	@Override
	public void handleCommand() {
		if (Client.isLoggedIn() == false)
			return;
		
		String gameName = argument.substring(0, argument.indexOf(" "));
		String username = argument.substring(argument.indexOf(" ") + 1);
		
		User user = Client.getUserByName(username);
		if (user == null) {
			System.err.println("leavg: username doesn't exist: " + username);
			return;
		}
		
		ClientGameController game = Client.getGameByName(gameName);
		if (game != null) {
			game.removeUser(user);
			return;
		}
		
		System.err.println("ClientLeavgHandler#handleCommand - gamename doesn't exist: " + gameName);
	}
	
	@Override
	public void handleAnswer(boolean isOK) {
	
	}
	
	/**
	 * Sends a message to the server to tell it this client wants to leave the game.
	 * @param gameName The name of the game to leave.
	 */
	public static void sendLeavgRequest(String gameName) {
		Client.sendMessageToServer(String.format("leavg %s %s", gameName, Client.getThisUser().getName()));
	}
}
