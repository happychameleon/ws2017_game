package server.parser;


import game.GameMap;
import game.GameState;
import game.ServerGameController;
import server.Server;
import server.parser.CommandHandler;

import java.util.HashMap;


/**
 * Creates a new game on the server and then tells all clients about it.
 * If the name is taken it writes back an -ERR answer.
 *
 * Created by flavia on 31.03.17.
 */
public class NewgmHandler extends CommandHandler {
	
	
	@Override
	public void handleCommand() {
		String wholeArgument = argument;
		int maxPoints = Integer.parseInt(getAndRemoveNextArgumentWord());
		String gameName = getAndRemoveNextArgumentWord();
		String mapName = getAndRemoveNextArgumentWord();
		GameMap map = GameMap.getMapForName(mapName);
		
		if (Server.isGameNameUnique(gameName) == false) {
			String errNameTakenMessage = "-ERR newgm game name taken";
			commandParser.writeBackToClient(errNameTakenMessage);
			return;
		}
		if (map == null) {
			System.err.println("Map name " + mapName + " doesn't exist.");
			return;
		}
		
		ServerGameController newGame = new ServerGameController(GameState.STARTING, maxPoints, gameName, new HashMap<>(), map);
		Server.addNewGame(newGame);
		
		// Tell all the clients that a new game has opened.
		Server.writeToAllClients(String.format("newgm %s", wholeArgument));
	}
	
	
}
