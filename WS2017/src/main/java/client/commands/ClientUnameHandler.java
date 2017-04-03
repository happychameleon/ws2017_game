package client.commands;

import client.Client;

/**
 * Created by flavia on 26.03.17.
 */
public class ClientUnameHandler extends CommandHandler {
	
	@Override
	public void handleCommand() {
		// NOT CURRENTLY NEEDED
	}
	
	@Override
	public void handleAnswer(boolean isOK) {
		if (isOK && argument.startsWith("you are ")) {
			String username = argument.substring(8);
			Client.setUsername(username);
		} else if (isOK == false && argument.equals("same username entered")) {
			// just ignore this. Maybe add message later?
		} else if (isOK == false && argument.startsWith("suggested ")) {
			String proposedUsername = argument.substring(argument.lastIndexOf(" ") + 1);
			Client.proposeUsername(proposedUsername);
		}
	}
	
}
