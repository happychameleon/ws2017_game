package server.parser;

import server.Server;
import server.ServerUser;

/**
 * Receives the cquit command and terminates the client thread in a meaningful way
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
	
	
	/**
	 * Sends the cquit command to all Clients, informing them about the user who quit.
	 * @param user The user who quit the application.
	 */
	public static void sendCQuitCommand(ServerUser user) {
		if (user.getName() != null)
			Server.writeToAllClients(String.format("cquit %s", user.getName()));
	}
}
