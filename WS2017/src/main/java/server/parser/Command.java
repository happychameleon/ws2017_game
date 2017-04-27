package server.parser;


import server.CommandParser;

/**
 * The different command keywords. They all have their CommandHandler stored to send the argument to process.
 *
 * Created by flavia on 26.03.17.
 */
public enum Command {
	
	cpong(new CpongHandler()),
	cquit(new CquitHandler()),
	chatm(new ChatmHandler()),
	chatw(new ChatwHandler()),
	chatl(new ChatlHandler()),
	uname(new UnameHandler()),
	cgetu(new CgetuHandler()),
	cgetg(new CgetgHandler()),
	newgm(new NewgmHandler()),
	joing(new JoingHandler()),
	leavg(new LeavgHandler()),
	ready(new ReadyHandler()),
	stgam(new StgamHandler()),
	endtn(new EndtnHandler()),
	attch(new AttchHandler()),
	chpos(new ChposHandler()),
	chpow(new ChpowHandler()),
	chwea(new ChweaHandler()),
	rmgam(new RmgamHandler()),
	uhigh(new UhighHandler()),
	cgeth(new CgethHandler()),
	wtchg(new WtchgHandler());
	
	/**
	 * The correct CommandHandler which processes the command.
	 */
	private final CommandHandler commandHandler;
	
	/**
	 * Creates the Command.
	 * @param commandHandler The CommandHandler belonging to this Command.
	 */
	Command(CommandHandler commandHandler) {
		this.commandHandler = commandHandler;
	}
	
	
	/**
	 * First sets the commandParser and the argument and then handles the Command.
	 * @see CommandHandler#handleCommand()
	 *
	 * @param commandParser The CommandParser to set.
	 * @param argument The argument given with the command.
	 */
	public void handleArgument(CommandParser commandParser, String argument) {
		commandHandler.setCommandParser(commandParser);
		commandHandler.setArgument(argument);
		commandHandler.handleCommand();
	}
}
