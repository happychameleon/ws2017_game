package client.commands;

import client.ClientCommandParser;

/**
 * The different command keywords. They all have their CommandHandler stored to send the argument to process.
 *
 * Created by flavia on 26.03.17.
 */
public enum Command {
	
	cping(new ClientCpingHandler()),
	cquit(new ClientCquitHandler()),
	chatm(new ClientChatmHandler()),
	nuser(new ClientNuserHandler());
	
	/**
	 * The correct CommandHandler which processes the command.
	 */
	private final CommandHandler commandHandler;
	
	Command(CommandHandler commandHandler) {
		this.commandHandler = commandHandler;
	}
	
	public void handleArgument(ClientCommandParser commandParser, String argument) {
		commandHandler.setCommandParser(commandParser);
		commandHandler.handleCommand(argument);
	}
}