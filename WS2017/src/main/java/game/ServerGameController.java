package game;

import game.startscreen.ClientGameStartController;
import serverclient.User;

/**
 * The {@link ClientGameController} on the server side.
 *
 * Created by flavia on 30.03.17.
 */
public class ServerGameController extends GameController {
	
	
	/**
	 * This starts a new game on the server with the given players after everyone selected a Team in the StartScreen.
	 */
	public void startGameOnServer (ClientGameStartController gameStartController) {
		// TODO
		//World world = new World(30, 25, this);
		
	}
	
	/**
	 * TODO!
	 * Removes the user from this game and handles it correctly.
	 * Informs the clients.
	 * If the game now is empty, remove it and inform the server.
	 */
	public void removeUser(User user) {
		//TODO!
	}
}
