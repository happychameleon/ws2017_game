package server.parser;

/**
 * When pong is received from client CpongHandler calls pingConfirmation() function
 * PingSender
 *
 * Created by flavia on 26.03.17.
 */
public class CpongHandler extends CommandHandler {
	
	
	@Override
	public void handleCommand() {
		commandParser.getPingSender().pingConfirmation();
	}
}
