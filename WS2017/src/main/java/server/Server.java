package server;

import game.GameState;
import game.ServerGameController;
import server.parser.CquitHandler;

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
	private static ArrayList<ServerGameController> gameList = new ArrayList<>();
	
	/**
	 * @return A shallow copy of {@link #gameList}.
	 */
	public static ArrayList<ServerGameController> getAllGames() {
		return (ArrayList<ServerGameController>) gameList.clone();
	}
	
	/**
	 * A list of all the connected users.
	 */
	private static ArrayList <ServerUser> userList = new ArrayList<>();
	
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
	 * The ServerGameController(s) the User was in informs the other client's about the user leaving.
	 * @see #userList
	 * @param user The User to remove.
	 */
	static boolean removeUserFromList(ServerUser user) {
		System.out.println("removeUserFromList");
		for (int i = 0; i < gameList.size(); i++) {
			ServerGameController sgc = gameList.get(i);
			if (sgc.getAllUsers().contains(user)) {
				sgc.removeUser(user);
			}
		}
		CquitHandler.sendCQuitCommand(user);
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
	 * Gets the ServerGameController for the specific gamename.
	 * @param name The game's name.
	 * @return The ServerGameController. Can be null if username doesn't exist!
	 */
	public static ServerGameController getGameByName(String name) {
		for (ServerGameController sgc : gameList) {
			if (sgc.getGameName().equals(name))
				return sgc;
		}
		return null;
	}
	
	/**
	 * @return An ArrayList of all games which have not yet started.
	 */
	public static ArrayList<ServerGameController> getStartingGames() {
		ArrayList<ServerGameController> startingGames = new ArrayList<>();
		for (ServerGameController sgc : getAllGames()) {
			if (sgc.getGameState() == GameState.STARTING) {
				startingGames.add(sgc);
			}
		}
		return startingGames;
	}
	
	/**
	 * @return An ArrayList of all games which are currently being played.
	 */
	public static ArrayList<ServerGameController> getRunningGames() {
		ArrayList<ServerGameController> runningGames = new ArrayList<>();
		for (ServerGameController sgc : getAllGames()) {
			if (sgc.getGameState() == GameState.RUNNING) {
				runningGames.add(sgc);
			}
		}
		return runningGames;
	}
	
	
	/**
	 * Adds the newly created game (which is still in the game start phase) to the list of new games.
	 * @see #gameList
	 * @param newGame The new game to add.
	 */
	public static void addNewGame(ServerGameController newGame) {
		gameList.add(newGame);
	}
	
	/**
	 * This removes the game from the waiting games, either because all players have left or because it has started.
	 * @param oldGame The game to remove.
	 */
	public static void removeGame(ServerGameController oldGame) {
		gameList.remove(oldGame);
	}
	
	/**
	 * Checks the existing games for duplicates.
	 * @param newGameName The proposed new name for the game.
	 * @return true if the newGameName is unique, false otherwise.
	 */
	public static boolean isGameNameUnique(String newGameName) {
		if (getGameByName(newGameName) == null) {
			return true;
		} else {
			return false;
		}
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

