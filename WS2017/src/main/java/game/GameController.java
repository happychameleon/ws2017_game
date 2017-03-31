package game;

import serverclient.User;

import java.util.ArrayList;

/**
 * Created by flavia on 31.03.17.
 */
public abstract class GameController {
	
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
	
	
	public ArrayList<User> getAllUsers() {
		// TODO!
		return null;
	}
}
