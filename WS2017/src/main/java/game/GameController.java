package game;

import serverclient.User;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * This handles the connection between a game and the server or client.
 * For each game there is one GameController per Client/Server.
 *
 * All the game commands are sent to the game via this controller.
 *
 * Created by flavia on 31.03.17.
 */
public abstract class GameController {
	
	private final HashSet<User> users;
	
	/**
	 * @return A shallow copy of {@link #users}.
	 */
	public ArrayList<User> getAllUsers() {
		return (ArrayList<User>) users.clone();
	}
	
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
	
	
	
	public GameController(HashSet<User> users, String gameName) {
		this.users = users;
		this.gameName = gameName;
	}
	
	
	/**
	 * Removes the user from the list.
	 */
	public void removeUser(User user) {
		users.remove(user);
		// TODO: What should happen when a user leaves?
	}
	
	@Override
	public String toString() {
		
		StringBuffer s = new StringBuffer(gameName);
		s.append(" (");
		
		for (User user : users) {
			s.append(user.getName());
			s.append(" ");
		}
		// Remove the last " " if there is one.
		if (s.charAt(s.length() - 1) == ' ')
			s.deleteCharAt(s.length() - 1);
		
		s.append(")");
		
		return s.toString();
	}
	
}
