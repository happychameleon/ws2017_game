package client.commands;

import client.Client;
import game.ClientGameController;
import game.GameController;
import game.engine.Tile;

/**
 * Created by flavia on 16.05.17.
 */
public class ClientCpushHandler extends ClientCommandHandler {
	/**
	 * Executes the command from the server.
	 */
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
		String gameName = getAndRemoveNextArgumentWord();
		ClientGameController gameController = Client.getGameByName(gameName);
		
		Tile attackerTile = parsePosition(getAndRemoveNextArgumentWord(), gameController);
		Tile pushedTile = parsePosition(getAndRemoveNextArgumentWord(), gameController);
		
		assert attackerTile.getCharacter() != null;
		assert pushedTile.getCharacter() != null;
		
		gameController.getWorld().pushCharacter(attackerTile, pushedTile);
	}
	
	/**
	 * Tells the Server about the Character who pushed the other.
	 * String is formated as 'cpush <gamename> <attackerPosition> <pushedPosition>'.
	 * @param gameController The game in which the push happens.
	 * @param attackerTile Where the pushing Character stands.
	 * @param pushedTile Where the Character to be pushed stands.
	 */
	public static void sendPushToServer(GameController gameController, Tile attackerTile, Tile pushedTile) {
		String attackerPosition = attackerTile.getXPosition() + "," + attackerTile.getYPosition();
		String pushedPosition = pushedTile.getXPosition() + "," + pushedTile.getYPosition();
		Client.sendMessageToServer(String.format("cpush %s %s %s", gameController.getGameName(), attackerPosition, pushedPosition));
	}
}
