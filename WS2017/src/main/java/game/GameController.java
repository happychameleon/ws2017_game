package game;

import game.engine.World;
import serverclient.User;

import java.util.*;

/**
 * The abstract class for the game controllers of the running and the open games
 * @see ClientGameController
 * @see ServerGameController
 *
 * Created by flavia on 06.04.17.
 */
public abstract class GameController {
	
	//region General Data
	/**
	 * The {@link GameState} of this game.
	 */
	protected GameState gameState;
	
	/**
	 * @return {@link #gameState}.
	 */
	public GameState getGameState() {
		return gameState;
	}
	
	/**
	 * The points to spend on the starting team.
	 */
	protected int startingPoints;
	
	/**
	 * @return {@link #startingPoints}.
	 */
	public int getStartingPoints() {
		return startingPoints;
	}
	
	/**
	 * The unique name of the game. Can't be changed once chosen.
	 */
	protected final String gameName;
	
	/**
	 * @return {@link #gameName}.
	 */
	public String getGameName() {
		return gameName;
	}
	
	/**
	 * The map on which this game is playing.
	 */
	protected final GameMap gameMap;
	
	/**
	 * @return {@link #gameMap}.
	 */
	public GameMap getGameMap() {
		return gameMap;
	}
	
	/**
	 * All of the users in the game.
	 *
	 * The String is this User's characterString and indicates whether the user is ready and has chosen their team and is only used when {@link #gameState} is {@link GameState#STARTING}.
	 * If the String is <code>null</code> it means the user isn't ready yet.
	 *
	 * The value (String) should NEVER be used when the game is not {@link GameState#RUNNING}, because value can either be null or a characterstring.
	 */
	protected HashMap<User, String> users = new HashMap<>();
	
	/**
	 * @return all the users from {@link #users} as an {@link ArrayList} sorted alphabetically by their names.
	 */
	public ArrayList<User> getAllUsers() {
		Set<User> usersSet = users.keySet();
		ArrayList<User> users = new ArrayList<>(usersSet.size());
		for (User user : usersSet) users.add(user);
		
		Collections.sort(users, Comparator.comparing(User::getName));
		
		return users;
	}
	//endregion
	
	//region Starting Game
	/**
	 * Only used while game is STARTING.
	 * @return all users which have already chosen their team.
	 */
	public HashMap<User, String> getAllWaitingUsers() {
		HashMap<User, String> waitingUsers = new HashMap<>();
		for (User user : getAllUsers()) {
			if (users.get(user) != null) {
				waitingUsers.put(user, users.get(user));
			}
		}
		return waitingUsers;
	}
	
	/**
	 * Only used while game is STARTING.
	 * @return all users which have yet to choose their team.
	 */
	public Set<User> getAllChoosingUsers() {
		Set<User> choosingUsers = new HashSet<User>();
		for (User user : getAllUsers()) {
			if (users.get(user) == null) {
				choosingUsers.add(user);
			}
		}
		return choosingUsers;
	}
	//endregion
	
	//region Running Game
	/**
	 * The world of this GameController.
	 */
	protected World world;
	
	/**
	 * @return {@link #world}.
	 */
	public World getWorld() {
		return world;
	}
	
	/**
	 * A list of all the users currently watching the game.
	 * Is null when the game hasn't started.
	 */
	protected ArrayList<User> watchingUsers;
	
	/**
	 * @return A shallow copy of {@link #watchingUsers}.
	 */
	public ArrayList<User> getWatchingUsers() {
		return (ArrayList<User>) watchingUsers.clone();
	}
	
	/**
	 * Adds the user to {@link #watchingUsers}.
	 * @param user The user to add.
	 */
	public void addWatchingUser(User user) {
		if (watchingUsers == null) {
			watchingUsers = new ArrayList<>();
		}
		watchingUsers.add(user);
	}
	
	/**
	 * Removes the user from {@link #watchingUsers}.
	 * @param user The user to remove.
	 */
	public void removeWatchingUser(User user) {
		watchingUsers.remove(user);
	}
	
	/**
	 * @return How many users are currently watching (not playing) the game.
	 */
	public int getWatchingUserCount() {
		return watchingUsers.size();
	}
	//endregion
	
	/**
	 * Creates the Game Controller in the given state.
	 * @param gameState {@link #gameState}.
	 * @param startingPoints {@link #startingPoints}.
	 * @param gameName {@link #gameName}.
	 * @param users {@link #users}.
	 * @param gameMap {@link #gameMap}.
	 */
	public GameController(GameState gameState, int startingPoints, String gameName, HashMap<User, String> users, GameMap gameMap) {
		this.gameState = gameState;
		this.startingPoints = startingPoints;
		this.gameName = gameName;
		this.users = users;
		this.gameMap = gameMap;
	}
	
	
	//region General Methods
	/**
	 * Adds the user to the game's user list.
	 * @param user The User to add.
	 */
	public void addUserToGame(User user) {
		users.put(user, null);
	}
	
	/**
	 * Sets the user to be ready. Only used when {@link #gameState} is {@link GameState#STARTING}.
	 * @param user The user to set to ready.
	 * @param characterString The user's characterString.
	 */
	public void setUserAsWaiting(User user, String characterString) {
		users.put(user, characterString);
	}
	
	/**
	 * Removes the user from the list.
	 * @param user The user to remove.
	 */
	public void removeUser(User user) {
		if (world != null) {
			world.getTurnController().removePlayer(user);
		}
		users.remove(user);
		
	}
	
	/**
	 * The name of the game is differently displayed depending on the {@link #gameState}.
	 * This gets the correctly displayed name.
	 * @return The String representing the game.
	 */
	@Override
	public String toString() {
		
		switch (gameState) {
			case STARTING:
				return String.format("%s (%s %dPts)", gameName, gameMap.getName(), startingPoints);
			
			case RUNNING:
				StringBuffer s = new StringBuffer(gameName);
				s.append(" (");
				// Add all users.
				for (User user : getAllUsers()) {
					s.append(user.getName());
					s.append(" ");
				}
				// Remove the last " " if there is one.
				if (s.charAt(s.length() - 1) == ' ')
					s.deleteCharAt(s.length() - 1);
				s.append(")");
				return s.toString();
			
			case FINISHED:
				return gameName;
			
			default:
				return null;
		}
		
		
	}
	//endregion
	
	//region Game Start Methods
	/**
	 * Gets the characterString for the given User.
	 * Used ONLY when starting the game.
	 *
	 * @param user The given user.
	 * @return The characterString
	 */
	public String getStartingCharacterStringForUser(User user) {
		return users.get(user);
	}
	
	/**
	 * Sets the game state to the correct state.
	 */
	public void startGame() {
		gameState = GameState.RUNNING;
		if (watchingUsers == null)
			watchingUsers = new ArrayList<>();
	}
	
	/**
	 * Sets the game state to the correct state.
	 * @param playerScore The highscore of the winning players.
	 * @param winningTeamName The name of the Team which has won.
	 */
	public void endGame(HashMap<String, Integer> playerScore, String winningTeamName) {
		gameState = GameState.FINISHED;
	}
	//endregion
}
