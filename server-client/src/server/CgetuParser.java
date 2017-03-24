package server;

/**
 * Created by flavia on 24.03.17.
 */
public class CgetuParser {
	
	private String argument;
	private CommandParser commandParser;
	
	public CgetuParser(String argument, CommandParser commandParser){
		this.argument = argument;
		this.commandParser = commandParser;
		
		handleCgetu();
	}
	
	private void handleCgetu() {
		if (argument.isEmpty() == false) {
			System.err.println("cgetu should not have an argument!");
		}
		
		String answer = "-OK cgetu";
		for (User user : Server.getAllUsers()) {
			if (user.getName().isEmpty()) {
				// We ignore non-registered users, since they will be sent to the client as new users when logging in.
				continue;
			}
			answer += " " + user.getName();
		}
		commandParser.writeBackToClient(answer);
	}
	
}
