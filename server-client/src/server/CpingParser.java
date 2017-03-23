package server;

/**
 * Class sends ping to client, if client does not respond
 * before timeout the server responds in a meaningful way
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
    //TODO: A more meaningful way of handling a client timeout
    public void run(){
        commandParser.writeBackToClient("cping");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            return;
        }
        System.out.println("-ERR client failed to respond to ping");
    }
}
