package client.commands;

import client.Client;

/**
 * Reads in the answer from the server about the proposed username
 * and (if +OK) loggs the Client in
 * or (if -ERR) proposes the suggested username from the Server.
 *
 * Created by flavia on 26.03.17.
 */
public class ClientUnameHandler extends ClientCommandHandler {
	
	@Override
	public void handleCommand() {
		// NOT CURRENTLY NEEDED
	}
	
	@Override
	public void handleAnswer(boolean isOK) {
		if (isOK && argument.startsWith("you are ")) {
			try {
				Thread.sleep(20); // so the panel get's created properly first.
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
