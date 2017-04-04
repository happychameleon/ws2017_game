package game;

import game.gamegui.Window;
import serverclient.User;

import java.util.HashSet;

/**
 * The {@link GameController} on the Client side.
 *
 * Created by flavia on 30.03.17.
 */
public class ClientGameController extends GameController {

	Window window;
	
	public ClientGameController(HashSet<User> users, String gameName) {
		super(users, gameName);
	}
	
	
	@Override
	public void removeUser(User user) {
		super.removeUser(user);
		//TODO!
	}
	
	/**
	 * TODO: Ability to watch a game.
	 */
	public void watchGame() {
	
	}

}
