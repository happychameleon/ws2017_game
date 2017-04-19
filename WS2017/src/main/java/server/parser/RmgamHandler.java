package server.parser;

import server.Server;

/**
 * Created by m on 10/04/17.
 */
public class RmgamHandler extends CommandHandler {
	
    @Override
    public void handleCommand() {
    
    }
    
    public static void sendRmgamMessageToAllClients(String gameName) {
        Server.writeToAllClients(String.format("rmgam %s", gameName));
    }
    
}
