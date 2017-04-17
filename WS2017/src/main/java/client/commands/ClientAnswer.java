package client.commands;

import client.ClientCommandParser;

/**
 * Works like {@link ClientCommand}, but with the answers.
 *
 * Created by flavia on 26.03.17.
 */
public enum ClientAnswer {
	
	nuser(new ClientNuserHandler()),
	cgetu(new ClientCgetuHandler()),
	uname(new ClientUnameHandler()),
	chatm(new ClientChatmHandler()),
	newgm(new ClientNewgmHandler()),
	cgetg(new ClientCgetgHandler()),
	chpos(new ClientChposHandler()),
	attch(new ClientAttchHandler()),
	endtn(new ClientEndtnHandler()),
	cgeth(new ClientCgethHandler());
	
	/**
	 * The correct ClientCommandHandler which processes the answer.
	 */
	private final ClientCommandHandler clientCommandHandler;
	
	ClientAnswer(ClientCommandHandler clientCommandHandler) {
		this.clientCommandHandler = clientCommandHandler;
	}
	
	/**
	 * This triggers the {@link #clientCommandHandler}'s handleAnswer method with the given parameters
	 * @param commandParser The correct ClientCommandHandler
	 * @param argument The argument from the answer
	 * @param isOK If the answer is a positive (+OK) or negative (-ERR) answer.
	 */
	public void handleAnswer(ClientCommandParser commandParser, String argument, boolean isOK) {
		clientCommandHandler.setCommandParser(commandParser);
		clientCommandHandler.setArgument(argument);
		clientCommandHandler.handleAnswer(isOK);
	}
	
}
