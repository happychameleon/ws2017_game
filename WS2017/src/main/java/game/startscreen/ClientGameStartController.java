package game.startscreen;

import client.Client;
import client.ClientUser;
import serverclient.User;

import java.util.HashMap;
import java.util.HashSet;

/**
 * The {@link GameStartController} on the Client side
 *
 * Created by flavia on 27.03.17.
 */
public class ClientGameStartController extends GameStartController {
	
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
		ClientUser thisUser = Client.getThisUser();
		if (choosingUsers.contains(thisUser) || waitingUsers.containsKey(thisUser)) {
			Client.getMainChatWindow().getMainChatPanel().displayInfo("You've already joined this game.");
			return;
		}
		
		String userJoinMessage = "joing " + gameName + " " + thisUser.getName();
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
			gameLobby = new GameLobby(this);
		}
		gameLobby.addUserToLobby((ClientUser) joinedUser);
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
			
		} else {
			System.out.println("ClientGameStartController#moveUserToWaiting - It's not this user");
		}
	}
	
	/**
	 * Removes the user from the game.
	 *
	 * @param user
	 */
	@Override
	public void removeUser(User user) {
		System.out.println("ClientGameStartController#removeUser");
		super.removeUser(user);
		
		if (user == Client.getThisUser()) {
			System.out.println("ClientGameStartController#removeUser removing this user");
			gameLobby.removeUser((ClientUser) user);
			//if (startScreen != null) {
				startScreen.dispose();
			//}
		}
	}
	
	/**
	 * Sends a message to the server to tell it this client wants to leave the game.
	 */
	public void leaveGame() {
		String message = "leavg " + getGameName() + " " + Client.getThisUser().getName();
		Client.sendMessageToServer(message);
	}
}
