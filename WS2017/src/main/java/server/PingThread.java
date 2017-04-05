package server;

import java.io.IOException;

/**
 * PingThread is started by PingSender. PingThread sends a ping to the client and if the thread is
 * not interrupted within timeout by pong from client, will remove the username of the client
 * and close client socket.
 *
 * Created by m on 4/04/17.
 */
class PingThread extends Thread{
    CommandParser commandParser;
    public PingThread(CommandParser commandParser){
        this.commandParser = commandParser;
    }

    public void run(){
        long pingDelay = 1000;
        long pingTimeout = 1000;
        for(int i = 0; i < 3; i++) {
            try {
                Thread.sleep(pingDelay);
            } catch (InterruptedException e) {
                return;
            }
            //System.out.println("sending client ping");
            commandParser.writeBackToClient("cping");
        }

        try {
            Thread.sleep(pingTimeout);
        } catch (InterruptedException e) {
            return;
        }
        System.out.println("-ERR client failed to respond to ping");
        System.out.println(commandParser.getReceivingUser().getName() + " has been removed from user list");
        Server.removeUserFromList(commandParser.getReceivingUser());
        try {
            // Sending empty byte[] to tell the input stream to quit.
            commandParser.getIn().read(new byte[] {});
            commandParser.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
