package game.startscreen;

import serverclient.User;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by flavia on 31.03.17.
 */
public class GameStartController {
	
	/**
	 * The users waiting for the game to start and the string representing their chosen characters.
	 */
	HashMap<User, String> waitingUsers;
	
	/**
	 * The users still choosing their characters. When all users have chosen their Characters the game begins.
	 */
	HashSet<User> choosingUsers;
	
	/**
	 * The unique name of the game.
	 */
	String gameName;
	
	/**
	 * @return {@link #gameName}.
	 */
	public String getGameName() {
		return gameName;
	}
	
	/**
	 * The points to spend on the starting team.
	 */
	final int startingPoints;
	
	public GameStartController(HashMap<User, String> waitingUsers, HashSet<User> choosingUsers, String gameName, int startingPoints) {
		this.waitingUsers = waitingUsers;
		this.choosingUsers = choosingUsers;
		this.gameName = gameName;
		this.startingPoints = startingPoints;
	}

}
