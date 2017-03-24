package client;

/**
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
	}
	
}
