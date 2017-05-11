package server.parser;

import game.ServerGameController;
import game.engine.Tile;
import server.Server;

/**
 * Created by m on 10/04/17.
 */
public class ChposHandler extends CommandHandler {
	
    @Override
    public void handleCommand() {
    	String wholeArgument = argument;
	
	    String gameName = getAndRemoveNextArgumentWord();
	    ServerGameController gameController = Server.getGameByName(gameName);
	
	    String username = getAndRemoveNextArgumentWord();
	
	    Tile oldPosition = parsePosition(getAndRemoveNextArgumentWord(), gameController);
	    Tile newPosition = parsePosition(getAndRemoveNextArgumentWord(), gameController);
	
	    int distance = Integer.parseInt(getAndRemoveNextArgumentWord());
	
	    if (oldPosition.hasCharacter() == false) {
		    System.err.println("ChposHandler#handleCommand - NO CHARACTER ON TILE!");
	    }
	    
	    oldPosition.getCharacter().moveCharacterTo(newPosition, distance);
	
	    commandParser.writeToAllGamingClients(String.format("+OK chpos %s", wholeArgument), gameController);
	    
    }
	
}
