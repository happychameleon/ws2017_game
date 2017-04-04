package client.commands;

import client.ClientCommandParser;
import java.io.IOException;

/**
 * TimeoutThread is started by the class ClientPongSender when ping is received.
 * If the thread is not interrupted in time by another ping the client closes its
 * socket and shuts down.
 *
 * Created by m on 04/04/17.
 */

class TimeoutThread extends Thread{
    ClientCommandParser commandParser;
    public TimeoutThread(ClientCommandParser commandParser){
        this.commandParser = commandParser;
    }

    public void run(){
        long timeoutDelay = 3000;
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
