package game.startscreen;

import client.Client;
import serverclient.User;

import java.util.HashMap;
import java.util.HashSet;

/**
 *
 *
 * Created by flavia on 27.03.17.
 */
public class ClientGameStartController extends GameStartController {
	
	/**
	 * The Window to choose the team from.
	 */
	private StartScreen startScreen;
	
	
	
	/**
	 * Opens the Window to choose the team from.
	 * Is called when the server gives the ok for creating or joining a game.
	 * @param waitingUsers The users already chosen the team and waiting for the others.
	 * @param choosingUsers The users still choosing the team.
	 * @param gameName The name of this game.
	 */
	public ClientGameStartController(HashMap<User, String> waitingUsers, HashSet<User> choosingUsers, String gameName, int startingPoints) {
		super(waitingUsers, choosingUsers, gameName, startingPoints);
		
	}
	
	/**
	 * Asks the server to join the game.
	 */
	public void joinGame() {
		
		String userJoinMessage = "joing " + gameName + " " + Client.getThisUser().getName();
		Client.sendMessageToServer(userJoinMessage);
	}
	
	/**
	 * The server informs that a new user has joined the game. They get added to the choosingUsers List
	 * If it is this user, the startScreen opens to select the Team.
	 */
	@Override
	public void addUserToGame(User joinedUser) {
		
		choosingUsers.add(joinedUser);
		if (Client.getThisUser() == joinedUser) {
			startScreen = new StartScreen(this, startingPoints);
		}
	}
	
	
	/**
	 * This is called by the StartScreen window and tells the server that this client is ready to play.
	 */
	public void clientIsReady (String characterString) {
		System.out.println("ClientGameStartController#clientIsReady");
		
		String message = String.format("ready %s %s %s", Client.getThisUser().getName(), gameName, characterString);
		Client.sendMessageToServer(message);
	}
	
	/**
	 * Moves the user to the waitingUsers list and if it is this user, it opens the
	 */
	@Override
	public void moveUserToWaiting(User user, String characterString) {
		super.moveUserToWaiting(user, characterString);
		
		if (Client.getThisUser() == user) {
			System.out.println("ClientGameStartController#moveUserToWaiting - It's this user");
			startScreen.dispose();
			
			// TODO: Open Lobby window here!
		} else {
			System.out.println("ClientGameStartController#moveUserToWaiting - It's not this user");
		}
	}
}
