package client.commands;

import client.Client;

/**
 * Created by flavia on 24.03.17.
 */
public class ClientNuserHandler extends CommandHandler {
	
	/**
	 * Handles the nuser command by adding the new user to the userlist IF this user is already logged in.
	 */
	@Override
	public void handleCommand(String argument) {
		if (Client.isLoggedIn() == false) {
			// Ignore this new user, because we get all user once we log in.
			return;
		}
		Client.addNewUser(argument);
		if (Client.getChatWindow() != null) {
			Client.getChatWindow().displayInfo(argument + " has logged in and joined the chat!");
		} else {
			System.err.println("Client is logged in but no Chat Window open!");
		}
		
	}
}
