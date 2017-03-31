package client.commands;

import client.Client;
import game.startscreen.ClientGameStartController;
import serverclient.User;

/**
 * Created by flavia on 31.03.17.
 */
public class ClientJoingHandler extends CommandHandler {
	
	@Override
	public void handleCommand(String argument) {
		String gameName = argument.substring(0, argument.indexOf(" "));
		String username = argument.substring(argument.indexOf(" ") + 1);
		User joinedUser = Client.getUserByName(username);
		ClientGameStartController game = Client.getChatWindow().getWaitingGameByName(gameName);
		
		if (joinedUser == null || game == null) {
			System.err.println("USER OR GAME DOESN'T EXIST!");
			return;
		}
		
		game.addUserToGame(joinedUser);
	}
	
	@Override
	public void handleAnswer(String argument, boolean isOK) {
	
	}
	
}
