package server;

import server.parser.CommandParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by m on 3/9/17.
 */
public class Server {
	private static ArrayList <User> userList = new ArrayList<User>();
	
	/**
	 * @return A shallow clone of the {@link #userList}.
	 */
	public static ArrayList<User> getAllUsers() {
		return (ArrayList<User>) userList.clone();
	}
	
	/**
	 * @see #userList
	 */
	static boolean addUserToList(User user) {
		return userList.add(user);
	}
	
	/**
	 * @see #userList
	 */
	static boolean removeUserFromList(User user) {
		return userList.remove(user);
	}
	
	/**
	 * Gets the User for the specific username.
	 * @param name The username.
	 * @return The User. Can be null if username doesn't exist!
	 */
    public static User getUserByName(String name) {
    	for (User user : userList) {
    		if (user.getName() != null && user.getName().equals(name)) {
    			return user;
		    }
	    }
	    return null;
    }
	
	/**
	 * Writes the output message to all clients.
	 * @param output the message
	 */
	public static void writeToAllClients(String output) {
		for (User user : getAllUsers()) {
			try {
				OutputStream outputStream = user.getSocket().getOutputStream();
				outputStream.write((output + "\r\n").getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
    

    public static void main(String[] args){
	    int connectedGameClient = 1;
	    int port = 1030;
	    if (args.length > 0) {
	    	port = Integer.parseInt(args[0]);
	    }
	    String serverIP = "-";
	    try {
		    serverIP = InetAddress.getLocalHost().getHostAddress();
	    } catch (UnknownHostException e) {
		    e.printStackTrace();
	    }
	
	    try{
            ServerSocket gamesServer = new ServerSocket(port);
	        System.out.print("Server running with ip " + serverIP + ", waiting for client connection on Port " + port + "\n");
	        while(true) {
                Socket socket = gamesServer.accept();
                (new GameClientThread(connectedGameClient++, userList, socket)).start(); //creates a new Server thread
            }
        } catch (IOException e) {
            System.err.println(e.toString());
            System.exit(1);
        }
    }
}

/**
 * TODO: write a good comment for javadoc
 */
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
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            out.write(("+OK gameserver connected to client " + connectedGameClient + " \r\n").getBytes());
            User user = new User(null, socket);
            Server.addUserToList(user);
            CommandParser gameProtocol = new CommandParser(in, out, user);
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