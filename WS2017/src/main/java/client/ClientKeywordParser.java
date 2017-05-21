package client;

import client.commands.ClientCommand;

/**
 * Compares keywords and if invalid returns an error
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
		ClientCommand command;
		try {
			command = Enum.valueOf(ClientCommand.class, keyword);
		} catch (IllegalArgumentException iae) {
			System.err.println("Received command does not exist! " + keyword + " " + argument);
			return;
		}
		
		command.handleCommand(commandParser, argument);
		
	}
	
	
}
