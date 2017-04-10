package client.commands;

import client.Client;
import game.startscreen.ClientGameStartController;

/**
 * Removes the game (because every user has left it).
 *
 * Created by flavia on 31.03.17.
 */
public class ClientRmgamHandler extends CommandHandler {
	
	
	@Override
	public void handleCommand() {
		if (Client.isLoggedIn() == false)
			return;
		
		ClientGameStartController game = Client.getMainChatWindow().getWaitingGameByName(argument);
		
		if(game != null) {
			Client.getMainChatWindow().removeGameFromList(game);
		} else {
			System.err.println("Game to delete doesn't exist: " + argument);
		}
	}
	
	@Override
	public void handleAnswer(boolean isOK) {
	
	}
	
}
