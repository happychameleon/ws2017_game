package server;

/**
 * Class sends ping to client, if client does not respond
 * before timeout the Server responds in a meaningful way
 *
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
    /**
     * function is called if 'cpong' response has been received form client.
     * function halts PingThread and starts a new one
     */
    public void pingConfirmation(){
        newPingThead.interrupt();
        newPingThead = new PingThread(commandParser);
        System.out.println("+OK ping has been received");
        sendPing();
    }

    public void terminatePingThread(){
        System.out.println("interrupting ping thread");
        newPingThead.interrupt();
    }

    /**
     * function is called to start the PingThread
     */
    public void sendPing(){
        newPingThead.start();
    }
}

class PingThread extends Thread{
    CommandParser commandParser;
    public PingThread(CommandParser commandParser){
        this.commandParser = commandParser;
    }
    //TODO: A more meaningful response a client timeout (closing socket?)
    public void run(){
        long pingDelay = 15000;
        long pingTimeout = 2000;
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
    }
}
