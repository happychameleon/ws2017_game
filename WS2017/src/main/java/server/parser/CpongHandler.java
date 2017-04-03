package server.parser;

/**
 * Created by flavia on 26.03.17.
 */
public class CpongHandler extends CommandHandler {
	
	
	@Override
	public void handleCommand() {
		//System.out.println();
		commandParser.getCpingSender().pingConfirmation();
	}
}
