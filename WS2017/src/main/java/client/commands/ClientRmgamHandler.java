package client.commands;

import client.Client;
import game.startscreen.ClientGameStartController;

/**
 * Removes the game because every user has left it.
 *
 * Created by flavia on 31.03.17.
 */
public class ClientRmgamHandler extends CommandHandler {
	
	
	@Override
	public void handleCommand(String argument) {
		if (Client.isLoggedIn() == false)
			return;
		
		ClientGameStartController game = Client.getChatWindow().getWaitingGameByName(argument);
		
		if(game != null) {
			Client.getChatWindow().removeGameFromList(game);
		} else {
			System.err.println("Game to delete doesn't exist: " + argument);
		}
	}
	
	@Override
	public void handleAnswer(String argument, boolean isOK) {
	
	}
	
}