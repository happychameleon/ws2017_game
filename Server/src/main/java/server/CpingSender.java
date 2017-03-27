package server;

import java.io.IOException;

/**
 * Class sends ping to client, if client does not respond
 * before timeout the Server responds in a meaningful way.
 *
 * Created by m on 3/20/17.
 */
public class CpingSender {
    CommandParser commandParser;
    PingThread newPingThread;
	
    public CpingSender(CommandParser commandParser){
        this.commandParser = commandParser;
        newPingThread = new PingThread(commandParser);
    }

    /**
     * function is called if 'cpong' response has been received form client.
     * function halts PingThread and starts a new one
     */
    public void pingConfirmation(){
        newPingThread.interrupt();
        newPingThread = new PingThread(commandParser);
        System.out.println("+OK ping has been received");
        sendPing();
    }

    public void terminatePingThread(){
        System.out.println("interrupting ping thread");
        newPingThread.interrupt();
    }

    /**
     * function is called to start the PingThread
     */
    public void sendPing(){
        newPingThread.start();
    }
}

/**
 * TODO: write a good comment for javadoc
 */
class PingThread extends Thread{
    CommandParser commandParser;
    public PingThread(CommandParser commandParser){
        this.commandParser = commandParser;
    }

    public void run(){
        long pingDelay = 5000;
        long pingTimeout = 15000;
        try {
            Thread.sleep(pingDelay);
        }catch (InterruptedException e){
            return;
        }
        System.out.println("sending client ping");
        commandParser.writeBackToClient("cping");
        try {
            Thread.sleep(pingTimeout);
        } catch (InterruptedException e) {
            return;
        }
        System.out.println("-ERR client failed to respond to ping");
        System.out.println(commandParser.getReceivingUser().getName() + "has been removed form user list");
        Server.removeUserFromList(commandParser.getReceivingUser());
        try {
            commandParser.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
