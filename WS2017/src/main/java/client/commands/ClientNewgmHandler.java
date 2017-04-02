package client.commands;

import client.Client;
import game.startscreen.ClientGameStartController;

import java.util.HashMap;
import java.util.HashSet;

/**
 * This command is received when a client has opened a new game.
 * It is added to the list of games to choose from where this client can now join it.
 *
 * Created by flavia on 31.03.17.
 */
public class ClientNewgmHandler extends CommandHandler {
	
	
	@Override
	public void handleCommand(String argument) {
		int maxPoints = Integer.parseInt(argument.substring(0, argument.indexOf(" ")));
		String gameName = argument.substring(argument.indexOf(" ") + 1, argument.length());
		
		ClientGameStartController newGame = new ClientGameStartController(new HashMap<>(), new HashSet<>(), gameName, maxPoints);
		
		Client.getChatWindow().addNewGameToList(newGame);
		
		
	}
	
	@Override
	public void handleAnswer(String argument, boolean isOK) {
		if (isOK == false && argument.startsWith("game name taken")) {
			Client.getChatWindow().displayError("The name for the new Game already exists. Please choose a new one!");
		}
	}
	
	
}
