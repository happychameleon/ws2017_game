package server;

import game.ServerGameController;
import game.startscreen.ServerGameStartController;

import java.io.IOException;
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
	
	/**
	 * The games which are just opened and are waiting to be started.
	 * Users can still join them (if there are still places available, max 4)
	 * They start when there are at least 2 users and all users are ready.
	 */
	private static ArrayList<ServerGameStartController> waitingGameList = new ArrayList<>();
	
	/**
	 * The games which are currently playing and can't be joined anymore.
	 */
	private static ArrayList<ServerGameController> runningGameList = new ArrayList<>();
	
	private static ArrayList <ServerUser> userList = new ArrayList<ServerUser>();
	
	/**
	 * @return A shallow clone of the {@link #userList}.
	 */
	public static ArrayList<ServerUser> getAllUsers() {
		return (ArrayList<ServerUser>) userList.clone();
	}
	
	/**
	 * @see #userList
	 */
	static boolean addUserToList(ServerUser user) {
		return userList.add(user);
	}
	
	/**
	 * @see #userList
	 */
	static boolean removeUserFromList(ServerUser user) {
		return userList.remove(user);
	}
	
	/**
	 * Gets the ServerUser for the specific username.
	 * @param name The username.
	 * @return The ServerUser. Can be null if username doesn't exist!
	 */
    public static ServerUser getUserByName(String name) {
    	for (ServerUser user : userList) {
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
		for (ServerUser user : getAllUsers()) {
			try {
				OutputStream outputStream = user.getSocket().getOutputStream();
				outputStream.write((output + "\r\n").getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Adds the newly created game (which is still in the game start phase) to the list of new games.
	 * Tells all the clients about this game.
	 * @see #waitingGameList
	 */
	public static void addNewWaitingGame(ServerGameStartController newGame) {
		waitingGameList.add(newGame);
		String newGameMessage = "";
		// TODO: Tell all the clients about the newly opened game!
		writeToAllClients(newGameMessage);
	}
	
	/**
	 * This removes the game from the waiting games, either because all players have left or because it has started.
	 */
	public static void removeWaitingGame(ServerGameStartController oldGame) {
		waitingGameList.remove(oldGame);
	}
	
	/**
	 * Adds the newly started game to the running game list.
	 * The corresponding waitingGame should be removed before calling this via {@link #removeWaitingGame(ServerGameStartController)}.
	 */
	public static void addNewRunningGame(ServerGameController newRunningGame) {
		runningGameList.add(newRunningGame);
	}
    
	

    public static void main(String[] args){
	    int connectedGameClient = 1;
	    int port = 1030;
	    if (args.length > 1) {
	    	port = Integer.parseInt(args[1]);
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
	
	/**
	 * Checks the existing games for duplicates.
	 * @param newGameName The proposed new name for the game.
	 * @return true if the newGameName is unique, false otherwise.
	 */
	public static boolean isGameNameUnique(String newGameName) {
		for (ServerGameStartController game : waitingGameList) {
			if (game.getGameName().equals(newGameName))
				return false;
		}
		for (ServerGameController game : runningGameList) {
			if (game.getGameName().equals(newGameName))
				return false;
		}
		return true;
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