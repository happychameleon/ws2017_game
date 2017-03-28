package server.parser;

import server.Server;
import server.User;

/**
 * Handles the cgetu command which is a request by a client to get all the existing users which already have a name.
 * The requesting client is also listed if they already have a name.
 *
 * Created by flavia on 24.03.17.
 */
public class CgetuHandler extends CommandHandler {
	
	@Override
	public void handleCommand(String argument) {
		if (argument.isEmpty() == false) {
			System.err.println("cgetu should not have an argument!");
		}
		
		String answer = "+OK cgetu";
		for (User user : Server.getAllUsers()) {
			if (user.getName() == null || user.getName().isEmpty()) {
				// We ignore non-registered users, since they will be sent to the client as new users when logging in.
				continue;
			}
			answer += " " + user.getName();
		}
		commandParser.writeBackToClient(answer);
	}
}
