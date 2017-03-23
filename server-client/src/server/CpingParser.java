package server;

import java.io.IOException;

/**
 * Created by m on 3/20/17.
 */
public class CpingParser {
    CommandParser commandParser;
    PingThread newPingThead;
	
    public CpingParser(CommandParser commandParser){
        this.commandParser = commandParser;
        newPingThead = new PingThread(commandParser);
    }

    //TODO: Still need to implement client side response
    public void pingConfirmation(){
        newPingThead.interrupt();
        newPingThead = new PingThread(commandParser);
        System.out.println("+OK ping has been received");
        newPingThead.start();
    }

    public void sendPing(){
        newPingThead.start();
    }
}

class PingThread extends Thread{
    CommandParser commandParser;
    public PingThread(CommandParser commandParser){
        this.commandParser = commandParser;
    }
    //TODO: A more meaningful way of handling a client timeout
    public void run(){
        commandParser.writeBackToClient("cping");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.err.println(e.toString());
        }
        System.out.println("-ERR client failed to respond to ping");
    }
}
