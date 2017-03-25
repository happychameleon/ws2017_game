package client;

/**
 * Handles the nuser command by adding the new user to the userlist IF this user is already logged in.
 *
 * Created by flavia on 24.03.17.
 */
public class ClientNuserParser {
	
	private String argument;
	
	public ClientNuserParser(String argument) {
		this.argument = argument;
		
		handleNuser();
	}
	
	private void handleNuser() {
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
		
		// TODO: Maybe add to the chat that a user with 'name' has logged in.
	}
	
}
