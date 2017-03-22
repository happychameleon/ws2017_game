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
    	if (argument.contains(" ") || argument.contains("'")) {
    		commandParser.writeBackToClient("ERROR: Username contains invalid characters. Don't use ' or <space>!");
    		return false;
	    }
	    if (server.getUserByName(argument) != null) {
    		int i = 1;
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
		if (sendingUser.getName() != null) {
			System.err.println("ERROR: User already had a name! Why did they log in twice? New username assigned.");
		}
		sendingUser.setName(argument);
		commandParser.writeBackToClient("+OK you are " + argument);
		
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
