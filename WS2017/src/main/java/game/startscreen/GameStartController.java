package game.startscreen;

import serverclient.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Represents the game before it started, where users can still choose their team.
 *
 * Created by flavia on 31.03.17.
 */
public abstract class GameStartController {
	
	
	/**
	 * The users waiting for the game to start and the string representing their chosen characters.
	 */
	HashMap<User, String> waitingUsers;
	
	/**
	 * @return A shallow copy of {@link #waitingUsers}.
	 */
	public HashMap<User, String> getAllWaitingUsers() {
		return (HashMap<User, String>) waitingUsers.clone();
	}
	
	/**
	 * The users still choosing their characters. When all users have chosen their Characters the game begins.
	 */
	HashSet<User> choosingUsers;
	
	/**
	 * @return A shallow copy of {@link #choosingUsers}.
	 */
	public HashSet<User> getAllChoosingUsers() {
		return choosingUsers;
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
	
	/**
	 * The points to spend on the starting team.
	 */
	final int startingPoints;
	
	/**
	 * @return {@link #startingPoints}.
	 */
	public int getStartingPoints() {
		return startingPoints;
	}
	
	public GameStartController(HashMap<User, String> waitingUsers, HashSet<User> choosingUsers, String gameName, int startingPoints) {
		this.waitingUsers = waitingUsers;
		this.choosingUsers = choosingUsers;
		this.gameName = gameName;
		this.startingPoints = startingPoints;
	}
	
	/**
	 * @return All users either in the {@link #waitingUsers} or {@link #choosingUsers} List.
	 */
	public ArrayList<User> getAllUsers() {
		ArrayList<User> users = new ArrayList<>();
		for (User user : waitingUsers.keySet()) {
			users.add(user);
		}
		for (User user : choosingUsers) {
			users.add(user);
		}
		return users;
	}
	
	/**
	 * Adds the user to the game's {@link #choosingUsers} list.
	 */
	public void addUserToGame(User user) {
		choosingUsers.add(user);
	}
	
	/**
	 * Removes the user from the game.
	 */
	public void removeUser (User user) {
		if (waitingUsers.containsKey(user))
			waitingUsers.remove(user);
		else if (choosingUsers.contains(user))
			choosingUsers.remove(user);
	}
	
	/**
	 * Removes the user from {@link #choosingUsers} and adds them to {@link #waitingUsers}.
	 */
	public void moveUserToWaiting(User user, String characterString) {
		choosingUsers.remove(user);
		waitingUsers.put(user, characterString);
	}
	
	
	@Override
	public String toString() {
		return gameName + " (" + startingPoints + "Pts)";
	}
}
