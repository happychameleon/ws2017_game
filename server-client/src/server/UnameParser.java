package server;

/**
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
		
	    if (commandParser.getUser().getName() != null && commandParser.getUser().getName().equals(argument)) {
			commandParser.writeBackToClient("-ERR same username entered");
			return false;
	    }
	    
	    if (server.getUserByName(argument) != null) {
    		int i = 2;
    		while (server.getUserByName(argument + i) != null) {
    			i++;
		    }
		    String nameSuggestion = argument + i;
		    commandParser.writeBackToClient("-ERR uname " + nameSuggestion);
		    return false;
	    }
	    return true;
	}
	
	
	private void addUsernameToServer() {
		User sendingUser = commandParser.getUser();
		
		// Does the user change their already existing name or define a new one?
		boolean nameChange = false;
		if (sendingUser.getName() != null) {
			nameChange = true;
		}
		
		sendingUser.setName(argument);
		
		if (nameChange) {
			// TODO: Maybe different response when changing name or defining a new one?
			commandParser.writeBackToClient("+OK you are " + argument);
		} else {
			commandParser.writeBackToClient("+OK you are " + argument);
		}
		
		// Just for testing
		commandParser.writeBackToClient("All the following users are logged in:");
		for (User user : server.getAllUsers()) {
			if (user.getName() != null) {
				commandParser.writeBackToClient(user.getName());
			} else {
				commandParser.writeBackToClient("UNNAMED USER");
			}
		}
	}
	
	
}
