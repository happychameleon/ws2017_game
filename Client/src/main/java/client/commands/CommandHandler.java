package client.commands;

import client.ClientCommandParser;

/**
 * This is the base class for classes which handle the command and the answer for a specific command
 *
 * Created by flavia on 26.03.17.
 */
public abstract class CommandHandler {
	
	/**
	 * The commandParser from this command.
	 */
	protected ClientCommandParser commandParser;
	
	/**
	 * @param commandParser {@link #commandParser}
	 */
	public void setCommandParser(ClientCommandParser commandParser) {
		this.commandParser = commandParser;
	}
	
	/**
	 * Executes the command from the server.
	 * @param argument the argument from the given command.
	 */
	public abstract void handleCommand(String argument);
	
	/**
	 * Executes the answer received from the server to the specific command this class is for.
	 * @param argument the argument from the given answer.
	 * @param isOK <code>true</code> if the answer started with "+OK", <code>false</code> otherwise (answer started with "-ERR").
	 */
	public abstract void handleAnswer(String argument, boolean isOK);
	
}
