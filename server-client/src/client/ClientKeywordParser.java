package client;

import java.util.ArrayList;

/**
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
		switch (keyword){
			case "cping" :
				ClientCpingParser ping = new ClientCpingParser(commandParser);
				ping.sendPong();
				break;
			
			case "nuser" :
				ClientNuserParser nuser = new ClientNuserParser(argument);
				break;
			
			case "chatm" :
				if (Client.isLoggedIn() && Client.getChatWindow() != null) {
					Client.getChatWindow().receiveMessage(argument);
				}
				break;
			
			default:
				//commandParser.writeBackToServer("-ERR entered command does not exist");
				break;
		}
	}
	
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
	
	private void nameChange(String argument) {
		String usernameSeparator = " ";
		int separatorIndex = argument.indexOf(usernameSeparator);
		if (separatorIndex < 1) {
			System.err.println("nameChange - nuser answer wrong formated");
			return;
		}
		String oldName = argument.substring(0, separatorIndex);
		String newName = argument.substring(separatorIndex + 1, argument.length() - 1);
		
		ClientUser user = Client.getUserByName(oldName);
		if (user != null) {
			user.setName(newName);
			System.out.println(oldName + " changed their name to " + newName);
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
