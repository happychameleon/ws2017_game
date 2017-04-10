package client.commands;

import client.Client;
import client.ClientUser;
import game.startscreen.ClientGameStartController;

/**
 * Adds the joined user to the game on the client side when the server informs this Client about the newly joined user.
 *
 * Created by flavia on 31.03.17.
 */
public class ClientJoingHandler extends CommandHandler {
	
	@Override
	public void handleCommand() {
		String gameName = argument.substring(0, argument.indexOf(" "));
		String username = argument.substring(argument.indexOf(" ") + 1);
		ClientUser joinedUser = Client.getUserByName(username);
		ClientGameStartController game = Client.getMainChatWindow().getWaitingGameByName(gameName);
		
		if (joinedUser == null || game == null) {
			System.err.println("USER OR GAME DOESN'T EXIST!");
			return;
		}
		
		game.addUserToGame(joinedUser);
	}
	
	@Override
	public void handleAnswer(boolean isOK) {
	
	}
	
}
