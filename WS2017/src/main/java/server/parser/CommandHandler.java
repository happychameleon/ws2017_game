package server.parser;

import server.CommandParser;

/**
 * Created by flavia on 26.03.17.
 */
public abstract class CommandHandler {
	
	protected String argument;
	
	public void setArgument(String argument) {
		this.argument = argument;
	}
	
	protected CommandParser commandParser;
	
	
	public void setCommandParser(CommandParser commandParser) {
		this.commandParser = commandParser;
	}
	
	
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
