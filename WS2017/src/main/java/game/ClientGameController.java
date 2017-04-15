package game;

import client.Client;
import client.ClientUser;
import client.commands.ClientJoingHandler;
import client.commands.ClientLeavgHandler;
import client.commands.ClientStgamHandler;
import game.engine.World;
import game.gamegui.Window;
import game.startscreen.GameLobby;
import game.startscreen.StartScreen;
import serverclient.User;

import java.util.HashMap;

/**
 * Created by flavia on 11.04.17.
 */
public class ClientGameController extends GameController {
	
	//region General Data
	/**
	 * The Lobby of this ClientGameController.
	 */
	protected GameLobby gameLobby;
	
	/**
	 * @return {@link #gameLobby}.
	 */
	public GameLobby getGameLobby() {
		return gameLobby;
	}
	
	/**
	 * Sets the gameLobby to null. Used by the gameLobby itself if it closes.
	 */
	public void removeGameLobby() {
		gameLobby = null;
	}
	
	/**
	 * The window displaying the game.
	 */
	private Window window;
	
	/**
	 * @return {@link #window}.
	 */
	public Window getWindow() {
		return window;
	}
	//endregion
	
	//region GameStart Data
	/**
	 * The Window to choose the team from.
	 */
	private StartScreen startScreen;
	
	/**
	 * @return {@link #startScreen}.
	 */
	public StartScreen getStartScreen() {
		return startScreen;
	}
	//endregion
	
	
	
	/**
	 * Creates the Game Controller in the given state.
	 * @param gameState      {@link #gameState}.
	 * @param startingPoints {@link #startingPoints}.
	 * @param gameName       {@link #gameName}.
	 * @param users          {@link #users}.
	 * @param map            {@link #gameMap}
	 */
	public ClientGameController(GameState gameState, int startingPoints, String gameName, HashMap<User, String> users, GameMap map) {
		super(gameState, startingPoints, gameName, users, map);
	}
	
	
	
	//region GameStart Methods
	/**
	 * Asks the server to join the game via {@link ClientJoingHandler}.
	 */
	public void askToJoinGame() {
		if (users.containsKey(Client.getThisUser())) {
			Client.getMainWindow().getMainChatPanel().displayInfo("You've already joined this game.");
			return;
		}
		
		ClientJoingHandler.sendJoingRequest(gameName);
	}
	
	/**
	 * The server informs that a new user has joined the game. They get added to the choosingUsers List
	 * If it is this user, the startScreen opens to select the Team.
	 */
	@Override
	public void addUserToGame(User joinedUser) {
		
		users.put(joinedUser, null);
		if (Client.getThisUser() == joinedUser) {
			gameLobby = new GameLobby(this);
			startScreen = new StartScreen(this, startingPoints);
		}
		if (gameLobby != null) {
			gameLobby.addUserToLobby((ClientUser) joinedUser);
		}
	}
	
	/**
	 * This is called by the StartScreen window and tells the server that this client is ready to play.
	 */
	public void thisClientIsReady(String characterString) {
		System.out.println("ClientGameStartController#thisClientIsReady");
		
		String message = String.format("ready %s %s %s", Client.getThisUser().getName(), gameName, characterString);
		Client.sendMessageToServer(message);
	}
	
	/**
	 * Moves the user to the waitingUsers list and if it is this user, it closes the {@link #startScreen}.
	 */
	@Override
	public void setUserAsWaiting(User user, String characterString) {
		super.setUserAsWaiting(user, characterString);
		
		if (Client.getThisUser() == user) {
			startScreen.dispose();
		}
		
	}
	
	/**
	 * Sends a message telling the server to start the game via {@link ClientStgamHandler#sendStartGame}.
	 */
	public void sendStartGame() {
		if (getAllChoosingUsers().isEmpty()) {
			if (getAllUsers().size() >= 1) { // TODO: set back to 2, 1 is only for testing!
				if (gameState == GameState.STARTING) {
					ClientStgamHandler.sendStartGame(gameName);
					
				} else {
					System.err.println("ClientGameController#sendStartGame - Can't start game twice!");
				}
			} else {
				gameLobby.getLobbyChat().displayInfo("Please wait for another user to start the game!");
			}
		} else {
			String choosingUserNames = "";
			for (User user : getAllChoosingUsers()) {
				choosingUserNames += " " + user.getName();
			}
			gameLobby.getLobbyChat().displayInfo("Please wait for following users to select their team:" + choosingUserNames);
		}
	}
	
	/**
	 * Starts the game as a copy to the one on the server.
	 * @param user The user who has started the game.
	 */
	public void startGame(User user) {
		super.startGame();
		
		if (users.keySet().contains(Client.getThisUser())) {
			world = new World(gameMap, this);
			
			window = new Window(this, world, gameName);
			
			gameLobby.gameHasStarted(user);
		}
		Client.getMainWindow().moveGameToRunning(this);
	}
	//endregion
	
	//region General Methods
	/**
	 * Removes the user from the game.
	 * Tells the world and the gameLobby about the user leaving.
	 * If it was this user who left, it also closes all the windows belonging to this game.
	 * @param user the user to remove
	 */
	@Override
	public void removeUser(User user) {
		super.removeUser(user);
		
		if (gameLobby != null)
			gameLobby.removeUser((ClientUser) user);
		
		if (user == Client.getThisUser()) {
			if (startScreen != null) {
				startScreen.dispose();
			}
			if (window != null) {
				window.dispose();
			}
		} else {
			if (window != null) {
				window.getMainGamePanel().repaintImage();
			}
		}
	}
	
	/**
	 * Sends a message to the server to tell it this client wants to leave the game via {@link ClientLeavgHandler}.
	 */
	public void askToLeaveGame() {
		ClientLeavgHandler.sendLeavgRequest(gameName);
	}
	//endregion
	
	//region Game Watching
	/**
	 * TODO: Ability to watch a game.
	 */
	public void watchGame() {
		ClientUser thisUser = Client.getThisUser();
		if (users.containsKey(thisUser)) {
			Client.getMainWindow().getMainChatPanel().displayInfo("You're already playing this game. You can't watch and play the same game.");
			return;
		}
		
	}
	//endregion
	
	//region Game Ending
	
	/**
	 * Ends the game.
	 * Removes the game from the running game list and displays it properly to the client if the client is playing the game.
	 *
	 * @param playerScore     The highscore of the winning players.
	 * @param winningTeamName The name of the Team which has won.
	 */
	@Override
	public void endGame(HashMap<String, Integer> playerScore, String winningTeamName) {
		Client.getMainWindow().removeGameFromList(this); // must be called before super.endGame() because it must be removed as a running game.
		
		super.endGame(playerScore, winningTeamName);
		
		Client.getMainWindow().addGameToList(this);
		
		if (window != null) {
			window.dispose();
		}
		
		if (users.containsKey(Client.getThisUser())) { // TODO: (M5) Also check for watching users and display it to them.
			// TODO: Display an info panel with the highscore.
			gameLobby.getLobbyChat().displayInfo("Team " + winningTeamName + " has won!");
			for (String username : playerScore.keySet()) {
				gameLobby.getLobbyChat().displayInfo(String.format("Player %s has scored %d points", username, playerScore.get(username)));
			}
		}
		
	}
	
	//endregion
	
	
	
	
	
	
	
	
	
	
	
	
	
}
