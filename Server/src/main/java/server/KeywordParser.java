package server;

import server.parser.*;

/**
 * Compares the command with {@link #compareKeyword()} and if it is an existing command it creates the correct parser to execute the command.
 *
 * Created by m on 3/20/17.
 */
public class KeywordParser {
	private final CommandParser commandParser;
    private final CpingSender ping;
	private final String keyword;
    private final String argument;
    
	
    public KeywordParser(String keyword, String argument, CpingSender ping, CommandParser commandParser){
        this.keyword = keyword;
        this.argument = argument;
        this.commandParser = commandParser;
        this.ping = ping;
    }
	
	/**
	 * Compares the command and if it is an existing command it creates the correct parser to execute the command.
	 */
	public void compareKeyword(){
		if (keyword == null)
			return;
		
		Command command;
		try {
			command = Enum.valueOf(Command.class, keyword);
		} catch (IllegalArgumentException iae) {
			// TODO Meilenstein 3: Server should send the errors to the client, so the user knows something is wrong and it can be fixed.
			System.err.println("Received command does not exist!");
			return;
		}
		
		if (command == null) {
			// TODO Meilenstein 3: Server should send the errors to the client, so the user knows something is wrong and it can be fixed.
			// commandParser.writeBackToClient("Received command does not exist!");
			System.err.println("Received command does not exist!");
			return;
		}
		
		command.handleArgument(commandParser, argument);
		
    }
}
