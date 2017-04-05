package server;

/**
 * Class sends ping to client, by start a PingThread and interrupts the
 * PingThread if it receives a pong from client.
 *
 * Created by m on 3/20/17.
 */

public class PingSender {
    CommandParser commandParser;
    PingThread newPingThread;
	
    public PingSender(CommandParser commandParser){
        this.commandParser = commandParser;
        newPingThread = new PingThread(commandParser);
    }

    /**
     * function is called if 'cpong' response has been received form client.
     * function interrupts PingThread and starts a new one
     */
    public void pingConfirmation(){
        newPingThread.interrupt();
        newPingThread = new PingThread(commandParser);
        //System.out.println("+OK ping has been received");
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
