package server.parser;

import server.CommandParser;

/**
 * This is the base class for classes which handle the command and the answer for a specific command
 *
 * Created by flavia on 26.03.17.
 */
public abstract class CommandHandler {
	
	/**
	 * The argument of this Command
	 */
	protected String argument;
	
	public void setArgument(String argument) {
		this.argument = argument;
	}
	
	/**
	 * The commandParser from this command.
	 */
	protected CommandParser commandParser;
	
	/**
	 * @param commandParser {@link #commandParser}
	 */
	public void setCommandParser(CommandParser commandParser) {
		this.commandParser = commandParser;
	}
	
	/**
	 * Executes the command from the client.
	 */
	public abstract void handleCommand();
	
	
	/**
	 * Takes the next Word (substring from 0 to indexOf(" ")), removes it from the string and returns it.
	 */
	protected String getAndRemoveNextArgumentWord() {
		if (argument.contains(" ")) {
			String firstWord = argument.substring(0, argument.indexOf(" "));
			argument = argument.substring(argument.indexOf(" ") + 1);
			return firstWord;
		} else if (argument.isEmpty()) {
			return "";
		} else {
			String lastWord = argument;
			argument = "";
			return lastWord;
		}
	}
}
