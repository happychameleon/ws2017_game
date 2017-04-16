package server.parser;

import game.ServerGameController;
import server.Server;
import serverclient.User;

/**
 * This is called when a server left a game.
 * The ServerGame(Start)Controller then informes all clients about the user leaving the game
 *
 * Created by flavia on 01.04.17.
 */
public class LeavgHandler extends CommandHandler {
	
	@Override
	public void handleCommand() {
		String gameName = argument.substring(0, argument.indexOf(" "));
		String username = argument.substring(argument.indexOf(" ") + 1);
		
		User user = Server.getUserByName(username);
		if (user == null) {
			System.err.println("leavg: username doesn't exist: " + username);
			return;
		}
		
		ServerGameController gameController = Server.getGameByName(gameName);
		if (gameController != null) {
			gameController.removeUser(user);
			return;
		}
		// The game wasn't found, meaning it is probably an ended game, only stored on the client for the lobby chat.
		// Just send it to the Clients.
		writeLeavgToClients(gameName, username);
		
	}
	
	/**
	 * Send a message to all Clients about the user who left the game.
	 * @param gameName The name of the game which the user left.
	 * @param username The name of the user who left.
	 */
	public static void writeLeavgToClients(String gameName, String username) {
		Server.writeToAllClients(String.format("leavg %s %s", gameName, username));
	}
}
