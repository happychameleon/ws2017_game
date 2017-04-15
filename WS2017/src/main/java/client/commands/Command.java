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
	chatw(new ClientChatwHandler()),
	chatl(new ClientChatlHandler()),
	nuser(new ClientNuserHandler()),
	newgm(new ClientNewgmHandler()),
	joing(new ClientJoingHandler()),
	leavg(new ClientLeavgHandler()),
	rmgam(new ClientRmgamHandler()),
	ready(new ClientReadyHandler()),
	stgam(new ClientStgamHandler()),
	uhigh(new ClientUhighHandler());
	
	/**
	 * The correct CommandHandler which processes the command.
	 */
	private final CommandHandler commandHandler;
	
	Command(CommandHandler commandHandler) {
		this.commandHandler = commandHandler;
	}
	
	/**
	 * First sets the commandParser and the argument and then handles the Command.
	 * @see CommandHandler#handleCommand()
	 *
	 * @param commandParser The ClientCommandParser to set.
	 * @param argument The argument given with the command.
	 */
	public void handleCommand(ClientCommandParser commandParser, String argument) {
		commandHandler.setCommandParser(commandParser);
		commandHandler.setArgument(argument);
		commandHandler.handleCommand();
	}
}
