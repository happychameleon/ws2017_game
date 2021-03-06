package server.parser;

import game.GameState;
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
	    
	    if (gameController.getGameState() != GameState.RUNNING)
	    	return; // The Attack has ended the game, no need to send it.
        
        commandParser.writeToAllGamingClients(String.format("+OK attch %s", wholeArgument), gameController);
    }
	
}
