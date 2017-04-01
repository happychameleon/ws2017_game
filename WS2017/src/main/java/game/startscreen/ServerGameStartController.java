package game.startscreen;

import server.Server;
import serverclient.User;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by flavia on 31.03.17.
 */
public class ServerGameStartController extends GameStartController {
	
	
	public ServerGameStartController(HashMap<User, String> waitingUsers, HashSet<User> choosingUsers, String gameName, int startingPoints) {
		super(waitingUsers, choosingUsers, gameName, startingPoints);
		
	}
	
	
	/**
	 * Removes the user from this game on the server TODO: and tells the clients.
	 * If the game now is empty, it is removed and the clients are informed.
	 */
	public void removeUser(User user) {
		super.removeUser(user);
		
		if (getAllUsers().isEmpty()) {
			Server.removeWaitingGame(this);
			Server.writeToAllClients("rmgam " + gameName);
		} else {
			Server.writeToAllClients(String.format("leavg %s %s", gameName, user.getName()));
		}
	}
}