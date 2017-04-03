package game;

import game.startscreen.ClientGameStartController;
import serverclient.User;

import java.util.HashSet;

/**
 * The {@link GameController} on the server side.
 *
 * Created by flavia on 30.03.17.
 */
public class ServerGameController extends GameController {
	
	
	public ServerGameController(HashSet<User> users, String gameName) {
		super(users, gameName);
	}
	
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
	 * Informs the clients about the user leaving.
	 * If the game now is empty, remove it and inform the server.
	 */
	@Override
	public void removeUser(User user) {
		super.removeUser(user);
		//TODO!
	}
}
