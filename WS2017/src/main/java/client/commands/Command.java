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
	nuser(new ClientNuserHandler()),
	newgm(new ClientNewgmHandler()),
	joing(new ClientJoingHandler()),
	leavg(new ClientLeavgHandler()),
	rmgam(new ClientRmgamHandler()),
	ready(new ClientReadyHandler());
	
	/**
	 * The correct CommandHandler which processes the command.
	 */
	private final CommandHandler commandHandler;
	
	Command(CommandHandler commandHandler) {
		this.commandHandler = commandHandler;
	}
	
	/**
	 * First sets the commandParser and then handles the Command.
	 * @see CommandHandler#handleCommand
	 */
	public void handleCommand(ClientCommandParser commandParser, String argument) {
		commandHandler.setCommandParser(commandParser);
		commandHandler.setArgument(argument);
		commandHandler.handleCommand();
	}
}
