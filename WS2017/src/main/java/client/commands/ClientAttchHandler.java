package client.commands;


import client.Client;
import game.ClientGameController;
import game.engine.Character;
import game.engine.Tile;

/**
 * Handles an attack by the player on an other player and attacks by other players
 *
 * Created by m on 10/04/17.
 */
public class ClientAttchHandler extends ClientCommandHandler {
    
    @Override
    public void handleCommand() {
    	
    }
    
    /**
     * Reads the attack Command's argument and carries out the attack.
     *
     * @param isOK <code>true</code> if the answer started with "+OK", <code>false</code> otherwise (answer started with "-ERR").
     */
    @Override
    public void handleAnswer(boolean isOK) {
	    String gameName = getAndRemoveNextArgumentWord();
	    ClientGameController gameController = Client.getGameByName(gameName);
	
	    String targetedChildPosition = getAndRemoveNextArgumentWord();
	    String attackerChildPosition = getAndRemoveNextArgumentWord();
	    int attackIntensity = Integer.parseInt(getAndRemoveNextArgumentWord());
	
	    Tile attackingTile = parsePosition(attackerChildPosition, gameController);
	    Tile targetedTile = parsePosition(targetedChildPosition, gameController);
	
	    gameController.getWorld().attackCharacter(attackingTile, targetedTile, attackIntensity);
    }
	
	/**
	 * Sends an attack message to Server.
	 * @param clientGameController The gameController of the world where the Attack happens.
	 * @param targetedCharacter The targeted enemy Character.
	 * @param attackingCharacter This users attacking Character.
	 */
	public static void sendAttackToServer(ClientGameController clientGameController, Character targetedCharacter, Character attackingCharacter) {
		String gameName = clientGameController.getGameName();
		String targetedChildPosition = targetedCharacter.getTile().getXPosition() + "," + targetedCharacter.getTile().getYPosition();
		String attackerChildPosition = attackingCharacter.getTile().getXPosition() + "," + attackingCharacter.getTile().getYPosition();
		String wetnessValue = String.valueOf(attackingCharacter.getWeapon().getDamage());
		
		Client.sendMessageToServer(String.format("attch %s %s %s %s", gameName, targetedChildPosition, attackerChildPosition, wetnessValue));
	}
}
