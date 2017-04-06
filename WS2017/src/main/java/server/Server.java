package server;

import game.ServerGameRunningController;
import game.startscreen.ServerGameStartController;
import serverclient.User;

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
	
	public static ArrayList<ServerGameStartController> getAllWaitingGames() {
		return (ArrayList<ServerGameStartController>) waitingGameList.clone();
	}
	
	/**
	 * The games which are currently playing and can't be joined anymore.
	 */
	private static ArrayList<ServerGameRunningController> runningGameList = new ArrayList<>();
	
	/**
	 * A list of all the connected users.
	 */
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
	 * Removes the user from all the lists they are in when the connection is terminated.
	 * @see #userList
	 */
	static boolean removeUserFromList(ServerUser user) {
		System.out.println("removeUserFromList");
		for (int i = 0; i < waitingGameList.size(); i++) {
			ServerGameStartController sgsc = waitingGameList.get(i);
			for (User u : sgsc.getAllUsers()) {
				if (user == u) {
					sgsc.removeUser(user);
				}
			}
		}
		for (int i = 0; i < runningGameList.size(); i++) {
			ServerGameRunningController sgc = runningGameList.get(i);
			for (User u : sgc.getAllUsers()) {
				if (user == u) {
					sgc.removeUser(user);
				}
			}
		}
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
	 * Gets the ServerGameStartController for the specific gamename.
	 * @param name The game's name.
	 * @return The ServerGameStartController. Can be null if username doesn't exist!
	 */
	public static ServerGameStartController getWaitingGameByName(String name) {
		for (ServerGameStartController sgsc : waitingGameList) {
			if (sgsc.getGameName().equals(name))
				return sgsc;
		}
		return null;
	}
	
	/**
	 * Gets the ServerGameRunningController for the specific gamename.
	 * @param name The game's name.
	 * @return The ServerGameStartController. Can be null if username doesn't exist!
	 */
	public static ServerGameRunningController getRunningGameByName(String name) {
		for (ServerGameRunningController sgsc : runningGameList) {
			if (sgsc.getGameName().equals(name))
				return sgsc;
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
	 * @see #waitingGameList
	 */
	public static void addNewWaitingGame(ServerGameStartController newGame) {
		waitingGameList.add(newGame);
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
	public static void addNewRunningGame(ServerGameRunningController newRunningGame) {
		runningGameList.add(newRunningGame);
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
		for (ServerGameRunningController game : runningGameList) {
			if (game.getGameName().equals(newGameName))
				return false;
		}
		return true;
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
}

