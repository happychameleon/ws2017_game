package client.commands;

import client.Client;
import client.ClientUser;

/**
 * Handles the nuser command by adding the new user to the userlist IF this user is already logged in.
 * Also receives and handles the answer to a name change request from another user.
 *
 * Created by flavia on 24.03.17.
 */
public class ClientNuserHandler extends CommandHandler {
	
	/**
	 * Handles the nuser command by adding the new user to the userlist IF this user is already logged in.
	 */
	@Override
	public void handleCommand() {
		if (Client.isLoggedIn() == false) {
			// Ignore this new user, because we get all user once we log in.
			return;
		}
		Client.addNewUser(argument);
		if (Client.getMainWindow() != null) {
			Client.getMainWindow().getMainChatPanel().displayInfo(argument + " has logged in and joined the chat!");
		} else {
			System.err.println("Client is logged in but no MainChatWindow Window open!");
		}
		
	}
	
	@Override
	public void handleAnswer(boolean isOK) {
		if(isOK) {
			nameChange(argument);
		}
	}
	
	/**
	 * Receives and handles the answer to a name change request from another user.
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
			if (Client.isLoggedIn() && Client.getMainWindow() != null) {
				Client.getMainWindow().renamedUser(oldName, newName, user);
			}
		} else {
			System.err.println("User who changed their name wasn't registered!");
		}
		
	}
	
}
