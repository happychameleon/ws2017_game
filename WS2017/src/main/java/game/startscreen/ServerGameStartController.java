package game.startscreen;

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
}
