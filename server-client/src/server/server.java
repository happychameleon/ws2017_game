package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by m on 3/9/17.
 */
public class server {
    public static void main(String[] args){
        int connectedGameClient = 1;
        try{
            System.out.print("server running waiting for client connection \n");
            ServerSocket gamesserver = new ServerSocket(1030);
            while(true) {
                Socket socket = gamesserver.accept();
                (new gameClientThread(connectedGameClient++, socket)).start(); //creates a new server thread
            }
        } catch (IOException e) {
            System.err.println(e.toString());
            System.exit(1);
        }
    }
}

class gameClientThread extends Thread{
    private int connectedGameClient;
    private Socket socket;
    public gameClientThread(int connectedGameClient, Socket socket){
        this.connectedGameClient = connectedGameClient;
        this.socket = socket;
    }
    public void run() {
        System.out.println("gameserver connectd to client " + connectedGameClient);
        try {
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            out.write(("gameserver connected to client " + connectedGameClient + "\r\n").getBytes());
            ServerProtocol gameProtocol;
            gameProtocol = new ServerProtocol(in, out);
            gameProtocol.validateProtocol();
            System.out.println("connection to client " + connectedGameClient + " is being terminated");
            socket.close();
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }
}