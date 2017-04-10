package client.commands;

import client.Client;
import game.ClientGameRunningController;
import game.startscreen.ClientGameStartController;
import serverclient.User;

/**
 * Removes the user from the game on the Client side (opposite of {@link ClientJoingHandler}).
 *
 * Created by flavia on 01.04.17.
 */
public class ClientLeavgHandler extends CommandHandler {
	
	@Override
	public void handleCommand() {
		String gameName = argument.substring(0, argument.indexOf(" "));
		String username = argument.substring(argument.indexOf(" ") + 1);
		
		User user = Client.getUserByName(username);
		if (user == null) {
			System.err.println("leavg: username doesn't exist: " + username);
			return;
		}
		
		ClientGameStartController waitingGame = Client.getMainChatWindow().getWaitingGameByName(gameName);
		if (waitingGame != null) {
			waitingGame.removeUser(user);
			return;
		}
		
		ClientGameRunningController runningGame = Client.getRunningGameByName(gameName);
		if (runningGame != null) {
			runningGame.removeUser(user);
			return;
		}
		
		System.err.println("ClientLeavgHandler#handleCommand - gamename doesn't exist: " + gameName);
	}
	
	@Override
	public void handleAnswer(boolean isOK) {
	
	}
	
}
