package client.commands;

import client.Client;
import game.ClientGameController;

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
		
		ClientGameController game = Client.getGameByName(argument);
		
		if(game != null) {
			Client.getMainWindow().removeGameFromList(game);
		} else {
			System.err.println("ClientRmgamHandler#handleCommand - Game to delete doesn't exist: " + argument);
		}
	}
	
	@Override
	public void handleAnswer(boolean isOK) {
	
	}
	
}
