package client.commands;

import client.Client;
import client.ClientUser;

/**
 * Created by flavia on 25.03.17.
 */
public class ClientCquitHandler extends CommandHandler {
	
	/**
	 * Handles the cquit command from the server telling this client that the client specified in the argument has logged off.
	 * @param argument the command argument containing the logged of client's name.
	 */
	public void handleCommand(String argument) {
		if (Client.isLoggedIn() == false)
			return;
		
		ClientUser user = Client.getUserByName(argument);
		if (user == Client.getThisUser()) {
			System.err.println("Trying to remove this user? This should never happen!");
		}
		if (user != null) {
			Client.removeUser(user);
			Client.getChatWindow().displayInfo(user.getName() + " has logged off!");
		} else {
			System.err.println("cquit used with a non-exisitng username!");
		}
	}
	
	@Override
	public void handleAnswer(String argument, boolean isOK) {
		// NOT CURRENTLY NEEDED
	}
	
}
