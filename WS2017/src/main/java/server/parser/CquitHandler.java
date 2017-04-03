package server.parser;

/**
 * Receives the
 *
 * Created by m on 3/20/17.
 */
public class CquitHandler extends CommandHandler {
    
    @Override
    public void handleCommand() {
        if (argument != null && argument.length() != 0) {
	        System.err.println("CquitHandler#handleCommand - cquit command takes no argument(s)!");
            return;
        }
        commandParser.writeBackToClient("+OK cquit terminating tasks and disconnecting");
        commandParser.shouldQuit = true;
    }
    
}
