package client;

/**
 *ClientThread is called by client and creates a new ClientCommandParser
 *
 * Created by m on 04/04/17.
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

class ClientThread extends Thread{
    Socket serverSocket;
    InputStream in;
    OutputStream out;
    boolean stopreaquest;
    public ClientThread(Socket serverSocket){
        super();
        this.serverSocket = serverSocket;
        stopreaquest = false;
    }

    /**
     * requestStop causes the client thread to terminate
     */
    public synchronized void requestStop(){
        stopreaquest = true;
    }

    public void run(){
        ClientCommandParser commandParser = new ClientCommandParser(serverSocket, stopreaquest);
        commandParser.stopValidatingCommand(stopreaquest);
    }
}
