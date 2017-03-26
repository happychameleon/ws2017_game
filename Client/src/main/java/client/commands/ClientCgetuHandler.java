package client.commands;

import client.Client;

import java.util.ArrayList;

/**
 * Created by flavia on 26.03.17.
 */
public class ClientCgetuHandler extends CommandHandler {
	
	
	@Override
	public void handleCommand(String argument) {
		// NOT NEEDED CURRENTLY
	}
	
	@Override
	public void handleAnswer(String argument, boolean isOK) {
		if (isOK && argument.startsWith("cgetu ")) {
			parseAllUsernames(argument.toCharArray());
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
		for (int i = 6; i < argument.length; i++) {
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
