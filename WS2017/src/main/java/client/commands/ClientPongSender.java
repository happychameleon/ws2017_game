package client.commands;

import client.Client;
import client.ClientCommandParser;

/**
 * Class sends pong to server after receiving a ping and restarts
 * a timeout thread.
 *
 * Created by m on 3/26/17.
 */
public class ClientPongSender {
    ClientCommandParser commandParser;
    TimeoutThread newTimeoutThread = new TimeoutThread(commandParser);

    public ClientPongSender(ClientCommandParser commandParser){
        this.commandParser = commandParser;
    }

    /**
     * Function is called if a ping has been received from the server.
     */
    public void sendCpong(){
        newTimeoutThread.interrupt();
        Client.sendMessageToServer("cpong");
        newTimeoutThread = new TimeoutThread(commandParser);
        newTimeoutThread.start();
    }
}





