package client;

import client.commands.Command;

import java.util.ArrayList;

/**
 * TODO: write a good comment for javadoc
 *
 * Created by m on 3/23/17.
 */
public class ClientKeywordParser {
	private final ClientCommandParser commandParser;
	private final String keyword;
	private final String argument;
	
	
	public ClientKeywordParser(String keyword, String argument, ClientCommandParser commandParser){
		this.keyword = keyword;
		this.argument = argument;
		this.commandParser = commandParser;
	}
	
	/**
	 * This compares the command and if it is an existing command it creates the correct parser to execute the command.
	 */
	public void compareKeyword(){
		Command command = Enum.valueOf(Command.class, keyword);
		
		if (command == null) {
			if (Client.getChatWindow() != null) {
				Client.getChatWindow().displayError("Received command does not exist!");
			} else {
				System.err.println("Received command does not exist!");
			}
			return;
		}
		
		command.handleArgument(commandParser, argument);
		
	}
	
	/**
	 * This compares the answer to one of the possible answers defined in the protocol.
	 */
	public void compareAnswer() {
		
		// UNAME Answers
		if (keyword.equals("+OK") && argument.startsWith("you are ")) {
			String username = argument.substring(8);
			Client.setUsername(username);
		} else if (keyword.equals("-ERR") && argument.equals("same username entered")) {
			// just ignore this. Maybe add message later?
		} else if (keyword.equals("-ERR") && argument.startsWith("uname ")) {
			String proposedUsername = argument.substring(6);
			Client.proposeUsername(proposedUsername);
		}
		
		// NUSER Answers
		else if(keyword.equals("+OK") && argument.startsWith("nuser ")) {
			nameChange(argument.substring(6));
		}
		
		// CGETU Answers
		else if (keyword.equals("+OK") && argument.startsWith("cgetu ")) {
			parseAllUsernames(argument.toCharArray());
		}
		
		// CHATM Answers
		
		
		
		// TODO: "-ERR entered command does not exist" What should we do with that?
	}
	
	/**
	 * Receives and handles the answer to a name change request from another user.
	 * @param argument the argument of the answer, starting with "nuser ".
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
			if (Client.isLoggedIn() && Client.getChatWindow() != null) {
				Client.getChatWindow().displayInfo( oldName + " changed their name to " + newName );
			}
		} else {
			System.err.println("User who changed their name wasn't registered!");
		}
		
	}
	
	/**
	 * This is for receiving all usernames when logging in as an answer to the cgetu command.
	 * @param argument
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
