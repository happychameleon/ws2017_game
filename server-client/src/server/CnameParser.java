package server;

/**
 * Created by m on 3/21/17.
 * TODO: Maybe only use the 'uname' command to either assign or change the username?
 */
public class CnameParser {
    private String argument;
    private CommandParser commandParser;
	
	
	public CnameParser(String argument, CommandParser commandParser){
        this.argument = argument;
	    this.commandParser = commandParser;
	    
	    handleCname();
    }
	
	private void handleCname() {
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
		if (sendingUser.getName() == null) {
			commandParser.writeBackToClient("ERROR: User did not already have a name! Please use the uname command to get a name.");
			return;
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
