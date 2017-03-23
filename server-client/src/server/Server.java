package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
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
	
	static boolean addUserToList(User user) {
		return userList.add(user);
	}
	
	static boolean removeUserFromList(User user) {
		return userList.remove(user);
	}
	
	/**
	 * Gets the User for the specific username.
	 * @param name The username.
	 * @return The User. Can be null if username doesn't exist!
	 * TODO: Delete the user/username pair when logging off.
	 */
    public static User getUserByName(String name) {
    	for (User user : userList) {
    		if (user.getName() != null && user.getName().equals(name)) {
    			return user;
		    }
	    }
	    return null;
    }
    

    public static void main(String[] args){
        int connectedGameClient = 1;

        try{
            System.out.print("Server running, waiting for client connection \n");
            ServerSocket gamesServer = new ServerSocket(1030);
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

class GameClientThread extends Thread{
    private int connectedGameClient;
    private Socket socket;
    public GameClientThread(int connectedGameClient, ArrayList userList, Socket socket){
        this.connectedGameClient = connectedGameClient;
        this.socket = socket;
    }
    public void run() {
        System.out.println("gameserver connectd to client " + connectedGameClient);
        try {
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            out.write(("gameserver connected to client " + connectedGameClient + "\r\n").getBytes());
            User user = new User(null, socket);
            Server.addUserToList(user);
            CommandParser gameProtocol = new CommandParser(in, out, user);
            gameProtocol.validateProtocol();
            System.out.println("connection to client " + connectedGameClient + " is being terminated");
            Server.removeUserFromList(user);
            socket.close();
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }
}