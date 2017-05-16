package server.parser;

import game.ServerGameController;
import game.engine.Tile;
import server.Server;

/**
 * Created by flavia on 16.05.17.
 */
public class CpushHandler extends CommandHandler {
	
	/**
	 * Executes the command from the client.
	 */
	@Override
	public void handleCommand() {
		String wholeArgument = argument;
		
		String gameName = getAndRemoveNextArgumentWord();
		ServerGameController gameController = Server.getGameByName(gameName);
		
		Tile attackerTile = parsePosition(getAndRemoveNextArgumentWord(), gameController);
		Tile pushedTile = parsePosition(getAndRemoveNextArgumentWord(), gameController);
		
		assert attackerTile.getCharacter() != null;
		assert pushedTile.getCharacter() != null;
		
		gameController.getWorld().pushCharacter(attackerTile, pushedTile);
		
		commandParser.writeToAllGamingClients("+OK cpush " + wholeArgument, gameController);
	}
	
}
