package server.parser;

import game.GameController;
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
	    GameController gameController = Server.getGameByName(gameName);
	
	    String username = getAndRemoveNextArgumentWord();
	
	    Tile oldPosition = parsePosition(getAndRemoveNextArgumentWord(), gameController);
	    Tile newPosition = parsePosition(getAndRemoveNextArgumentWord(), gameController);
	
	    int distance = Integer.parseInt(getAndRemoveNextArgumentWord());
	
	    if (oldPosition.getCharacter() == null) {
		    System.err.println("ChposHandler#handleCommand - NO CHARACTER ON TILE!");
	    }
	    
	    oldPosition.getCharacter().moveCharacterTo(newPosition, distance);
     
	    Server.writeToAllClients(String.format("+OK chpos %s", wholeArgument));
    }
	
	/**
	 * Reads the position from the string as 'x,y'
	 * @param positionString the positionString formatted as x,y
	 * @return The correct Tile.
	 */
	private Tile parsePosition(String positionString, GameController gameController) {
		
		String xString = "";
		String yString = "";
		int i = 0;
		for (char c : positionString.toCharArray()) {
			if (i == 0)
				if (c == ',') i++;
				else xString += c;
			else
				yString += c;
		}
		int x = Integer.parseInt(xString);
		int y = Integer.parseInt(yString);
		System.out.println("ChposHandler#parsePosition - x: " + x + " y: " + y);
		return gameController.getWorld().getTileAt(x, y);
	}
	
}
