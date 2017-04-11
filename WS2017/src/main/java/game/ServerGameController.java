package game;

import server.Server;
import serverclient.User;

import java.util.HashMap;

/**
 * Created by flavia on 11.04.17.
 */
public class ServerGameController extends GameController {
	
	
	
	/**
	 * Creates the Game Controller in the given state.
	 *
	 * @param gameState      {@link #gameState}.
	 * @param startingPoints {@link #startingPoints}.
	 * @param gameName       {@link #gameName}.
	 * @param users          {@link #users}.
	 */
	public ServerGameController(GameState gameState, int startingPoints, String gameName, HashMap<User, String> users) {
		super(gameState, startingPoints, gameName, users);
	}
	
	
	
	
	/**
	 * Removes the user from this game on the server.
	 * Informs the clients about the user leaving.
	 * If the game now is empty, it is removed and the clients are informed.
	 */
	@Override
	public void removeUser(User user) {
		super.removeUser(user);
		
		Server.writeToAllClients(String.format("leavg %s %s", gameName, user.getName()));
		
		if (getAllUsers().isEmpty()) {
			Server.removeGame(this);
			Server.writeToAllClients(String.format("rmgam %s", gameName));
		}
	}
	
	
}
