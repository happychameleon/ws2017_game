package game;

import game.engine.World;
import serverclient.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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
	 * @return all the users from {@link #users} as an {@link Set}.
	 */
	public Set<User> getAllUsers() {
		return users.keySet();
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
	protected World world;
	//endregion
	
	/**
	 * Creates the Game Controller in the given state.
	 *  @param gameState {@link #gameState}.
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
	 */
	public void addUserToGame(User user) {
		users.put(user, null);
	}
	
	/**
	 * Sets the user to be ready. Only used when {@link #gameState} is {@link GameState#STARTING}.
	 */
	public void setUserAsWaiting(User user, String characterString) {
		users.put(user, characterString);
	}
	
	/**
	 * Removes the user from the list.
	 */
	public void removeUser(User user) {
		users.remove(user);
		// TODO: What should happen when a user leaves?
	}
	
	/**
	 * TODO: Gets the map as a String to send to the clients.
	 * @return The map String.
	 */
	public String getMapString() {
		return "TODO";
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
				// TODO: Finished game name.
				return gameName;
			
			default:
				return null;
		}
		
		
	}
	//endregion
	
	//region Game Start Methods
	
	/**
	 * Gets the characterString for the given User.
	 * @param user The given user.
	 * @return The characterString
	 */
	public String getCharacterStringForUser(User user) {
		return users.get(user);
	}
	
	/**
	 * Sets the game state to the correct state.
	 */
	public void startGame() {
		gameState = GameState.RUNNING;
	}
	//endregion
}
