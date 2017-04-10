package game;

import client.Client;
import client.ClientUser;
import game.gamegui.Window;
import serverclient.User;

import java.util.HashSet;

/**
 * The {@link GameRunningController} on the Client side.
 *
 * Created by flavia on 30.03.17.
 */
public class ClientGameRunningController extends GameRunningController {

	Window window;
	
	public ClientGameRunningController(HashSet<User> users, String gameName) {
		super(users, gameName);
	}
	
	
	/**
	 * Removes the user from the lobby window.
	 * @param user the user to remove.
	 */
	public void removeUser(ClientUser user) {
		gameLobby.removeUser(user);
	}
	
	/**
	 * TODO: Ability to watch a game.
	 */
	public void watchGame() {
		ClientUser thisUser = Client.getThisUser();
		if (users.contains(thisUser)) {
			Client.getMainChatWindow().getMainChatPanel().displayInfo("You're already watching this game.");
			return;
		}
	}

}
