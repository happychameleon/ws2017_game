package game;

import game.startscreen.GameLobby;
import game.startscreen.GameStartController;
import serverclient.User;

import java.util.ArrayList;

/**
 * The abstract class for the game controllers of the running and the open games
 * @see game.startscreen.GameStartController
 * @see GameRunningController
 *
 * Created by flavia on 06.04.17.
 */
public abstract class GameController {
	
	/**
	 * The Lobby of this ClientGameStartController.
	 */
	protected GameLobby gameLobby;
	
	/**
	 * @return {@link #gameLobby}.
	 */
	public GameLobby getGameLobby() {
		return gameLobby;
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
	 * The unique name of the game.
	 */
	protected String gameName;
	
	/**
	 * @return {@link #gameName}.
	 */
	public String getGameName() {
		return gameName;
	}
	
	
	public abstract ArrayList<User> getAllUsers();
	
	/**
	 * TODO!
	 * @param startController
	 * @return
	 */
	public static GameRunningController createRunningControllerFromStartController (GameStartController startController) {
		return null;
	}
	
	
}
