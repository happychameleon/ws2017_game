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
		
		// TODO: Inform the server with the selected characters.
		String message = "ready " + Client.getThisUser().getName() + " ";
		message += characterString;
		Client.sendMessageToServer(message);
	}
	
	
}
