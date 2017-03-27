package client.commands;

import client.Client;
import client.ClientCommandParser;
import java.io.IOException;

/**
 * Class sends pong to server, if server does not respond
 * before timeout the Client closes socket and shuts down.
 *
 * Created by m on 3/26/17.
 */
public class ClientCpongSender {
    ClientCommandParser commandParser;
    TimeoutThread newTimeoutThread = new TimeoutThread(commandParser);

    public ClientCpongSender(ClientCommandParser commandParser){
        this.commandParser = commandParser;
    }

    /**
     * function is called if a ping has been received from the server.
     */
    public void sendCpong(){
        newTimeoutThread.interrupt();
        Client.sendMessageToServer("cpong");
        newTimeoutThread = new TimeoutThread(commandParser);
        newTimeoutThread.start();
    }
}





/**
 * TODO: write a good comment for javadoc
 */
class TimeoutThread extends Thread{
    ClientCommandParser commandParser;
    public TimeoutThread(ClientCommandParser commandParser){
        this.commandParser = commandParser;
    }

    public void run(){
        long timeoutDelay = 20000;
        try {
            Thread.sleep(timeoutDelay);
        }catch (InterruptedException e){
            return;
        }
        System.out.println("-ERR Sever failed to send ping");
        try {
            commandParser.serverSocket.close();
            System.out.println("closing socket and closing client, TimeoutDelay: " + timeoutDelay);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(-1);
    }
}
