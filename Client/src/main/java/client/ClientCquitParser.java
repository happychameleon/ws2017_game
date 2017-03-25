package client;

/**
 * Created by flavia on 25.03.17.
 */
public class ClientCquitParser {
	
	String argument;
	
	public ClientCquitParser(String argument) {
		this.argument = argument;
		
		handleCquit();
	}
	
	private void handleCquit() {
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
}
