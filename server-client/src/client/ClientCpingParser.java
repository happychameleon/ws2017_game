package client;

/**
 * Created by m on 3/23/17.
 */
public class ClientCpingParser {
    ClientCommandParser commandParser;


    public ClientCpingParser(ClientCommandParser commandParser){
        this.commandParser = commandParser;
    }
    public void sendPong(){
        //System.out.println("responding to ping with pong");
        Client.sendMessageToServer("cpong");
        
        // Just for testing:
	    System.out.println("Current users:");
	    for (ClientUser user : Client.users) {
		    System.out.println(" " + user.getName());
	    }
    }
}
