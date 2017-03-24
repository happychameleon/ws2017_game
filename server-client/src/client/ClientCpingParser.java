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
        System.out.println("responding to ping with pong");
        Client.sendMessageToServer("cpong");
    }
}
