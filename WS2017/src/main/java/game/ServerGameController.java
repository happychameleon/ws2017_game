package game;

import server.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * The {@link GameController} on the server side.
 *
 * Created by flavia on 30.03.17.
 */
public class ServerGameController {
	
	/**
	 * The users waiting for the game to start and their chosen Characters.
	 */
	private HashMap<User, ArrayList<Character>> waitingUsers;
	
	/**
	 * The users still choosing their characters. When all users have chosen their Characters the game begins.
	 */
	private HashSet<User> choosingUsers;
	
	/**
	 * This starts a new game on the server with the given players after everyone selected a Team in the StartScreen.
	 * @param players the players playing the game.
	 */
	public void startGameOnServer (User[] players) {
	
	}

}
