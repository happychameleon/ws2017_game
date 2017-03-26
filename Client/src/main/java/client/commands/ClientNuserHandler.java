package client.commands;

import client.Client;
import client.ClientUser;

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
	
	@Override
	public void handleAnswer(String argument, boolean isOK) {
		if(isOK) {
			nameChange(argument);
		}
	}
	
	/**
	 * Receives and handles the answer to a name change request from another user.
	 * @param argument the argument of the answer, starting with "nuser ".
	 */
	private void nameChange(String argument) {
		String usernameSeparator = " ";
		int separatorIndex = argument.indexOf(usernameSeparator);
		if (separatorIndex < 1) {
			System.err.println("nameChange - nuser answer wrong formated");
			return;
		}
		String oldName = argument.substring(0, separatorIndex);
		String newName = argument.substring(separatorIndex + 1, argument.length());
		
		ClientUser user = Client.getUserByName(oldName);
		if (user != null) {
			user.setName(newName);
			if (Client.isLoggedIn() && Client.getChatWindow() != null) {
				Client.getChatWindow().displayInfo( oldName + " changed their name to " + newName );
			}
		} else {
			System.err.println("User who changed their name wasn't registered!");
		}
		
	}
	
}
