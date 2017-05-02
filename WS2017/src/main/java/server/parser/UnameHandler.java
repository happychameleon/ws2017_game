package server.parser;

import server.Server;
import server.ServerUser;

/**
 * Handles the uname command by checking if
 * - the name doesn't contain illegal characters,
 * - the name isn't the same as before (sends same username entered message back),
 * - the name isn't already taken (sends suggested name back)
 * and if the name is ok it changes the name and informs all clients about the name change or name assignment.
 *
 * Created by m on 3/20/17.
 */
public class UnameHandler extends CommandHandler {
	
	
	@Override
	public void handleCommand() {
		if (validateArgument(argument) == false) {
			return;
		}
		addUsernameToServer(argument);
	}
	
	
	/**
	 * @return <code>true</code> if the argument is correctly formatted and valid, otherwise <code>false</code>.
	 * @param argument
	 */
	private boolean validateArgument(String argument) {
		if (argument == null || argument.isEmpty()) {
			// This should actually never happen, because the client doesn't send empty names.
			commandParser.writeBackToClient("-ERR uname username is empty");// Please enter a username!
			return false;
		}
		if (argument.charAt(0) == '\'' && argument.charAt(argument.length() - 1) == '\'') {
			argument = argument.substring(1, argument.length() - 1);
		}
    	if (argument.contains(" ") || argument.contains("'")) {
		    // This should actually never happen, because the client doesn't send these names.
    		commandParser.writeBackToClient("-ERR uname username contains invalid characters");// Don't use ' or <space>!
    		return false;
	    }
		
	    if (commandParser.getReceivingUser().getName() != null && commandParser.getReceivingUser().getName().equals(argument)) {
			commandParser.writeBackToClient("-ERR uname same username entered");
			return false;
	    }
	    
	    if (Server.getUserByName(argument) != null) {
    		int i = 2;
    		while (Server.getUserByName(argument + i) != null) {
    			i++;
		    }
		    String nameSuggestion = argument + i;
		    commandParser.writeBackToClient("-ERR uname suggested " + nameSuggestion);
		    return false;
	    }
	    return true;
	}
	
	/**
	 * This sets the username of the ServerUser who entered this command to the given username.
	 * If the user didn't already have a name it newly assigns one, otherwise it overwrites the old one.
	 * @param argument the New Name
	 */
	private void addUsernameToServer(String argument) {
		ServerUser sendingUser = commandParser.getReceivingUser();
		String oldName = sendingUser.getName();
		String newName = argument;
		
		// Does the user change their already existing name or define a new one?
		boolean nameChange = false;
		if (oldName != null) {
			nameChange = true;
		}
		
		sendingUser.setName(newName);
		
		if (nameChange) {
			commandParser.writeBackToClient("+OK uname you are " + newName);
			commandParser.writeToAllOtherClients("+OK nuser " + oldName + " " + newName);
		} else {
			commandParser.writeBackToClient("+OK uname you are " + newName);
			commandParser.writeToAllOtherClients("nuser " + newName);
		}
	}
}
