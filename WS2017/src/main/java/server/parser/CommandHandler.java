package server.parser;

import server.CommandParser;

/**
 * Created by flavia on 26.03.17.
 */
public abstract class CommandHandler {
	
	protected CommandParser commandParser;
	
	
	public void setCommandParser(CommandParser commandParser) {
		this.commandParser = commandParser;
	}
	
	
	public abstract void handleCommand(String argument);
	
}
