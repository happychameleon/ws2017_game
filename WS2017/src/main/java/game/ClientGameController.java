package game;

import game.gamegui.Window;
import serverclient.User;

/**
 * This handles the connection between a game and the server or client.
 * For each game there is one ClientGameStartController per Client.
 *
 * All the game commands are sent to this controller which handles them.
 *
 * Created by flavia on 30.03.17.
 */
public class ClientGameController extends GameController {

	Window window;
	
	
	@Override
	public void removeUser(User user) {
		super.removeUser(user);
		//TODO!
	}

}
