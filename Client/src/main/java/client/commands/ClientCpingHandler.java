package client.commands;

import client.Client;
import client.ClientUser;

/**
 * TODO: write a good comment for javadoc
 *
 * Created by m on 3/23/17.
 */
public class ClientCpingHandler extends CommandHandler {
	
	/**
	 * Handles the cping by sending cpong back.
	 * For testing also prints all the currently registered user by this client.
	 * @param argument empty argument.
	 */
	@Override
	public void handleCommand(String argument) {
		//System.out.println("responding to ping with pong");
		Client.sendMessageToServer("cpong");
		
		// Just for testing:
		System.out.println("Current users:");
		for (ClientUser user : Client.getAllUsers()) {
			System.out.println(" " + user.getName());
		}
	}
	
	@Override
	public void handleAnswer(String argument, boolean isOK) {
		// NOT CURRENTLY USED
	}
	
}
