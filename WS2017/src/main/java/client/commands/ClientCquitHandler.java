package client.commands;

import client.Client;
import client.ClientUser;

/**
 * Handles the cquit command from the server telling this client that the client specified in the argument has logged off.
 *
 * Created by flavia on 25.03.17.
 */
public class ClientCquitHandler extends CommandHandler {
	
	/**
	 * Handles the cquit command from the server telling this client that the client specified in the argument has logged off.
	 */
	public void handleCommand() {
		if (Client.isLoggedIn() == false)
			return;
		
		ClientUser user = Client.getUserByName(argument);
		if (user == Client.getThisUser()) {
			System.err.println("Trying to remove this user? This should never happen!");
		}
		if (user != null) {
			Client.removeUser(user);
			Client.getMainChatWindow().getMainChatPanel().displayInfo(user.getName() + " has logged off!");
		} else {
			System.err.println("cquit used with a non-exisitng username!");
		}
	}
	
	@Override
	public void handleAnswer(boolean isOK) {
		// NOT CURRENTLY NEEDED
	}
	
}
