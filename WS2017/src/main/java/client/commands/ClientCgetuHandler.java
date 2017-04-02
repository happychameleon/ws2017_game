package client.commands;

import client.Client;

import java.util.ArrayList;


/**
 * The answer to the client's cgetu request where the server informs this Client about the currently logged in Users.
 * After the answer is processed, and all users are registered, the Client asks the server, about the current games.
 *
 * Created by flavia on 26.03.17.
 */
public class ClientCgetuHandler extends CommandHandler {
	
	
	@Override
	public void handleCommand(String argument) {
		// NOT NEEDED CURRENTLY
	}
	
	@Override
	public void handleAnswer(String argument, boolean isOK) {
		if (isOK) {
			parseAllUsernames(argument.toCharArray());
			Client.sendMessageToServer("cgetg");
		}
	}
	
	/**
	 * This is for receiving all usernames when logging in as an answer to the cgetu command.
	 * @param argument containing all the usernames.
	 */
	private void parseAllUsernames(char[] argument) {
		String usernameSeparator = " ";
		ArrayList<String> usernames = new ArrayList<>();
		String currentUsername = "";
		for (int i = 0; i < argument.length; i++) {
			if (argument[i] == ' ') {
				usernames.add(currentUsername);
				currentUsername = "";
			} else {
				currentUsername += argument[i];
				if (i == argument.length - 1) {
					// last user isn't recognised with a space, has to be added here.
					usernames.add(currentUsername);
				}
			}
		}
		Client.readInAllUsernames(usernames);
	}
}
