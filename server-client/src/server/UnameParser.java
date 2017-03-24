package server;

/**
 * TODO: write a good comment for javadoc
 *
 * Created by m on 3/20/17.
 */
public class UnameParser {
    private String argument;
    private CommandParser commandParser;
	
    public UnameParser(String argument, CommandParser commandParser){
        this.argument = argument;
	    this.commandParser = commandParser;
	    
	    handleUname();
    }
	
	private void handleUname() {
    	if (validateArgument() == false) {
    		return;
	    }
    	addUsernameToServer();
	}
	
	/**
	 * @return <code>true</code> if the argument is correctly formatted and valid, otherwise <code>false</code>.
	 */
	private boolean validateArgument() {
		if (argument == null || argument.isEmpty()) {
			commandParser.writeBackToClient("ERROR: Username is empty! Please enter a username!");
			return false;
		}
		if (argument.charAt(0) == '\'' && argument.charAt(argument.length() - 1) == '\'') {
			argument = argument.substring(1, argument.length() - 1);
		}
    	if (argument.contains(" ") || argument.contains("'")) {
    		commandParser.writeBackToClient("ERROR: Username contains invalid characters. Don't use ' or <space>!");
    		return false;
	    }
		
	    if (commandParser.getReceivingUser().getName() != null && commandParser.getReceivingUser().getName().equals(argument)) {
			commandParser.writeBackToClient("-ERR same username entered");
			return false;
	    }
	    
	    if (Server.getUserByName(argument) != null) {
    		int i = 2;
    		while (Server.getUserByName(argument + i) != null) {
    			i++;
		    }
		    String nameSuggestion = argument + i;
		    commandParser.writeBackToClient("-ERR uname " + nameSuggestion);
		    return false;
	    }
	    return true;
	}
	
	/**
	 * This sets the username of the User who entered this command to the given username.
	 * If the user didn't already have a name it newly assigns one, otherwise it overwrites the old one.
	 */
	private void addUsernameToServer() {
		User sendingUser = commandParser.getReceivingUser();
		String oldName = sendingUser.getName();
		String newName = argument;
		
		// Does the user change their already existing name or define a new one?
		boolean nameChange = false;
		if (oldName != null) {
			nameChange = true;
		}
		
		sendingUser.setName(newName);
		
		if (nameChange) {
			commandParser.writeBackToClient("+OK you are " + newName); // TODO: change message to "+OK uname <username>"
			commandParser.writeToAllOtherClients("+OK nuser " + oldName + " " + newName);
		} else {
			commandParser.writeBackToClient("+OK you are " + newName);
			commandParser.writeToAllOtherClients("nuser " + newName);
		}
		
	}
	
	
}
