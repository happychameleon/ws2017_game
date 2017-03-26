package client.commands;

import client.Client;

/**
 * Created by flavia on 26.03.17.
 */
public class ClientUnameHandler extends CommandHandler {
	
	@Override
	public void handleCommand(String argument) {
		// NOT CURRENTLY NEEDED
	}
	
	@Override
	public void handleAnswer(String argument, boolean isOK) {
		if (isOK && argument.startsWith("you are ")) {
			String username = argument.substring(8);
			Client.setUsername(username);
		} else if (isOK == false && argument.equals("same username entered")) {
			// just ignore this. Maybe add message later?
		} else if (isOK == false && argument.startsWith("new username ")) { // TODO: This message needs to be updated according to the protocol.
			String proposedUsername = argument.substring(13);
			Client.proposeUsername(proposedUsername);
		}
	}
	
}