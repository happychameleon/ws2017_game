package server.parser;

import game.ServerGameController;
import game.engine.Tile;
import server.Server;

/**
 * Handles an attack from one character to another.
 *
 * Created by m on 10/04/17.
 */
public class AttchHandler extends CommandHandler {
	
	/**
	 * Reads the attack Command's argument and carries out the attack.
	 */
    @Override
    public void handleCommand() {
    	String wholeArgument = argument;
    	
        String gameName = getAndRemoveNextArgumentWord();
	    ServerGameController gameController = Server.getGameByName(gameName);
        
        String targetedChildPosition = getAndRemoveNextArgumentWord();
        String attackerChildPosition = getAndRemoveNextArgumentWord();
        int attackIntensity = Integer.parseInt(getAndRemoveNextArgumentWord());
        
        Tile attackingTile = parsePosition(attackerChildPosition, gameController);
        Tile targetedTile = parsePosition(targetedChildPosition, gameController);
	
	    assert gameController != null;
	    gameController.getWorld().attackCharacter(attackingTile, targetedTile, attackIntensity);
        
        Server.writeToAllClients(String.format("+OK attch %s", wholeArgument));
    }
	
}
