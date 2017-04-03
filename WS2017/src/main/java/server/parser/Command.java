package server.parser;


import server.CommandParser;

/**
 * Created by flavia on 26.03.17.
 */
public enum Command {
	
	cpong(new CpongHandler()),
	cquit(new CquitHandler()),
	chatm(new ChatmHandler()),
	uname(new UnameHandler()),
	cgetu(new CgetuHandler()),
	cgetg(new CgetgHandler()),
	newgm(new NewgmHandler()),
	joing(new JoingHandler()),
	leavg(new LeavgHandler()),
	ready(new ReadyHandler());
	
	
	private final CommandHandler commandHandler;
	
	Command(CommandHandler commandHandler) {
		this.commandHandler = commandHandler;
	}
	
	
	public void handleArgument(CommandParser commandParser, String argument) {
		commandHandler.setCommandParser(commandParser);
		commandHandler.setArgument(argument);
		commandHandler.handleCommand();
	}
}
