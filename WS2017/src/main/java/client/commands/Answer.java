package client.commands;

import client.ClientCommandParser;

/**
 * Works like {@link Command}, but with the answers.
 *
 * Created by flavia on 26.03.17.
 */
public enum Answer {
	
	nuser(new ClientNuserHandler()),
	cgetu(new ClientCgetuHandler()),
	uname(new ClientUnameHandler()),
	chatm(new ClientChatmHandler()),
	newgm(new ClientNewgmHandler()),
	cgetg(new ClientCgetgHandler());
	
	/**
	 * The correct CommandHandler which processes the answer.
	 */
	private final CommandHandler commandHandler;
	
	Answer(CommandHandler commandHandler) {
		this.commandHandler = commandHandler;
	}
	
	/**
	 * This triggers the {@link #commandHandler}'s handleAnswer method with the given parameters
	 * @param commandParser The correct CommandHandler
	 * @param argument The argument from the answer
	 * @param isOK If the answer is a positive (+OK) or negative (-ERR) answer.
	 */
	public void handleAnswer(ClientCommandParser commandParser, String argument, boolean isOK) {
		commandHandler.setCommandParser(commandParser);
		commandHandler.setArgument(argument);
		commandHandler.handleAnswer(isOK);
	}
	
}
