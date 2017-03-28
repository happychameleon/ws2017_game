package game;

import game.engine.World;
import game.gamegui.Window;
import server.User;

/**
 * This handles the connection between a game and the server or client.
 * For each game there is one GameController.
 *
 * It starts a new game on the server which sends it to the client.
 * On the client side it creates the game as a copy from the one sent from the server.
 *
 * All the game commands are sent to this controller which handles them.
 *
 * Created by flavia on 27.03.17.
 */
public class GameController {
	
	/**
	 * This starts a new game on the server with the given players.
	 * @param players the players playing the game.
	 */
	public void startGameOnServer (User[] players) {
	
	}
	
	
	/**
	 * Only for testing to see if the game still works.
	 * TODO Meilenstein 3: remove this test.
	 */
	public static void main (String[] args) {
		World world = new World(30, 25, new GameController(), 2);
		Window window = new Window(world, "WasserschlachtSimulator 2017 Prototype");
	}
	
}
