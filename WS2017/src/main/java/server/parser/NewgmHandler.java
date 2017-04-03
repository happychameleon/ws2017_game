package server.parser;


import game.startscreen.ServerGameStartController;
import server.Server;

import java.util.HashMap;
import java.util.HashSet;


/**
 * Creates a new game on the server and then tells all clients about it.
 * If the name is taken it writes back an -ERR answer.
 *
 * Created by flavia on 31.03.17.
 */
public class NewgmHandler extends CommandHandler {
	
	
	@Override
	public void handleCommand() {
		int maxPoints = Integer.parseInt(argument.substring(0, argument.indexOf(" ")));
		String gameName = argument.substring(argument.indexOf(" ") + 1, argument.length());
		
		if (Server.isGameNameUnique(gameName) == false) {
			String errNameTakenMessage = "-ERR newgm game name taken";
			commandParser.writeBackToClient(errNameTakenMessage);
			return;
		}
		
		ServerGameStartController newGame = new ServerGameStartController(new HashMap<>(), new HashSet<>(), gameName, maxPoints);
		Server.addNewWaitingGame(newGame);
		
		// Tell all the clients that a new game has opened.
		String newGameMessage = "newgm " + argument;
		Server.writeToAllClients(newGameMessage);
		System.out.println("Sent to clients: " + newGameMessage);
	}
	
	
}
