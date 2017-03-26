package client.commands;

import client.ClientCommandParser;

/**
 * Created by flavia on 26.03.17.
 */
public abstract class CommandHandler {
	
	private ClientCommandParser commandParser;
	
	public void setCommandParser(ClientCommandParser commandParser) {
		this.commandParser = commandParser;
	}
	
	
	public abstract void handleCommand(String argument);
	
	public abstract void handleAnswer(String argument, boolean isOK);
	
}
