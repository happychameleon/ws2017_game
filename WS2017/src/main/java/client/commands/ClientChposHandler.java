package client.commands;

import client.Client;
import game.ClientGameController;
import game.engine.Tile;

/**
 * Transmits the new position of Children during player turn to the server and
 * handles new positions of other player children during their turn.
 *
 * Created by m on 10/04/17.
 */
public class ClientChposHandler extends ClientCommandHandler {
    @Override
    public void handleCommand() {
    
    }
	
	/**
	 * Executes the answer received from the server to the specific command this class is for.
	 *
	 * @param isOK <code>true</code> if the answer started with "+OK", <code>false</code> otherwise (answer started with "-ERR").
	 */
	@Override
	public void handleAnswer(boolean isOK) {
		System.out.println("ClientChposHandler#handleAnswer");
		String gameName = getAndRemoveNextArgumentWord();
		ClientGameController gameController = Client.getGameByName(gameName);
		
		String username = getAndRemoveNextArgumentWord(); // Currently unused.
		
		Tile oldPosition = parsePosition(getAndRemoveNextArgumentWord(), gameController);
		Tile newPosition = parsePosition(getAndRemoveNextArgumentWord(), gameController);
		
		int distance = Integer.parseInt(getAndRemoveNextArgumentWord());
		
		oldPosition.getCharacter().moveCharacterTo(newPosition, distance);
		
		gameController.getWindow().getMainGamePanel().repaintImage();
	}
    
	
	/**
	 * Sends the message to the server to inform it about a Character's movement.
	 * @param gameController The gameController of the character's game.
	 * @param oldTile The Tile where the Character starts the movement.
	 * @param newTile The Tile where the Character ends the movement.
	 * @param distance The distance between oldTile and newTile in Tiles.
	 */
	public static void sendNewPositionToServer(ClientGameController gameController, Tile oldTile, Tile newTile, int distance) {
	    String gameName = gameController.getGameName();
	    String username = Client.getThisUser().getName();
	    String oldPosition = oldTile.getXPosition() + "," + oldTile.getYPosition();
		String newPosition = newTile.getXPosition() + "," + newTile.getYPosition();
	
	    Client.sendMessageToServer(String.format("chpos %s %s %s %s %d", gameName, username, oldPosition, newPosition, distance));
    }
}
