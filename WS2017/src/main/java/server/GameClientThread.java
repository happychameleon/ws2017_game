package server;

/**
 * A GameClientThread is always spun up by the Server class when a new
 * client tries to connect to the server, so that each client has its
 * dedicated thread to communicate with the server with.
 *
 * Created by m on 04/04/17.
 */

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

class GameClientThread extends Thread{
    private int connectedGameClient;
    private Socket socket;
    public GameClientThread(int connectedGameClient, ArrayList userList, Socket socket){
        this.connectedGameClient = connectedGameClient;
        this.socket = socket;
    }
    public void run() {
        System.out.println("gameserver connected to client " + connectedGameClient);
        try {
            OutputStream out = socket.getOutputStream();
            //out.write(("+OK gameserver connected to client " + connectedGameClient + " \r\n").getBytes());
            ServerUser user = new ServerUser(null, socket);
            Server.addUserToList(user);
            CommandParser gameProtocol = new CommandParser(socket, user);
            gameProtocol.validateProtocol();
            System.out.println("connection to client " + connectedGameClient + " is being terminated");
            Server.removeUserFromList(user);
            Server.writeToAllClients("cquit " + user.getName());
            socket.close();
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }
}
