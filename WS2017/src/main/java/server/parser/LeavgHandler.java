package server.parser;

import game.ServerGameController;
import game.startscreen.ServerGameStartController;
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
	public void handleCommand(String argument) {
		String gameName = argument.substring(0, argument.indexOf(" "));
		String username = argument.substring(argument.indexOf(" ") + 1);
		
		User user = Server.getUserByName(username);
		if (user == null) {
			System.err.println("leavg: username doesn't exist: " + username);
			return;
		}
		
		ServerGameStartController waitingGame = Server.getWaitingGameByName(gameName);
		if (waitingGame != null) {
			waitingGame.removeUser(user);
			return;
		}
		
		ServerGameController runningGame = Server.getRunningGameByName(gameName);
		if (runningGame != null) {
			runningGame.removeUser(user);
			return;
		}
		
		System.err.println("leavg: Game Name doesn't exist: " + gameName);
	}
}
