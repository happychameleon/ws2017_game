package server.parser;

/**
 * TODO: write a good javadoc
 *
 * Created by flavia on 26.03.17.
 */
public class CpongHandler extends CommandHandler {
	
	
	@Override
	public void handleCommand() {
		commandParser.getCpingSender().pingConfirmation();
	}
}
