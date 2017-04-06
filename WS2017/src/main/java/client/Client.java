package client;

import client.clientgui.Login;
import client.clientgui.MainChatWindow;
import game.ClientGameRunningController;
import game.GameController;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * The main class for this Client.
 * Everything is static since there is only one Client.
 *
 * Created by m on 3/10/17.
 */

public class Client {
	
	private static Login loginWindow;
	
	/**
	 * The main chat window. Here every logged in client can read everything.
	 * TODO Meilenstein 3: possibility for private chats (different windows).
	 */
	private static MainChatWindow mainChatWindow;
	
	public static MainChatWindow getMainChatWindow() {
		return mainChatWindow;
	}
	
	/**
	 * If this client has already logged in and chosen a username.
	 */
	private static boolean isLoggedIn = false;
	
	/**
	 * @return {@link #isLoggedIn}.
	 */
	public static boolean isLoggedIn() {
		return isLoggedIn;
	}
	
	/**
	 * If the username is given via command line, this is set to the given username, otherwise it stays empty.
	 * It is only used when opening the login window to set the textfield proposing the username.
	 */
	protected static String commandLineUsername = "";
	
	public static String getCommandLineUsername() {
		return commandLineUsername;
	}
	
	private static InputStream serverInputStream;
	private static OutputStream serverOutputStream;
	private static Socket serverSocket;
	
	/**
	 * The user representing this client.
	 */
	private static final ClientUser thisUser = new ClientUser(null);
	
	/**
	 * @return {@link #thisUser}
	 */
	public static ClientUser getThisUser() {
		return thisUser;
	}
	
	/**
	 * All the logged in users.
	 * It's empty until this client has logged in.
	 */
	static ArrayList<ClientUser> users = new ArrayList<>();
	
	/**
	 * @return A shallow copy of {@link #users}.
	 */
	public static ArrayList<ClientUser> getAllUsers() {
		return (ArrayList<ClientUser>) users.clone();
	}
	
	/**
	 * A list of all the running games.
	 */
	private static ArrayList<ClientGameRunningController> runningGameList = new ArrayList<>();
	
	/**
	 * Gets the User for the specific username.
	 * @param name The username.
	 * @return The User. Can be null if username doesn't exist!
	 */
	public static ClientUser getUserByName(String name) {
		for (ClientUser user : users) {
			if (user.getName() != null && user.getName().equals(name)) {
				return user;
			}
		}
		return null;
	}
	
	/**
	 * Removes the specified User from the {@link #users} list.
	 * @param user The user to delete.
	 */
	public static void removeUser(ClientUser user) {
		
		users.remove(user);
		mainChatWindow.removeUserFromUserlist(user);
	}
	
	
	/**
	 * This adds a new user with the username from the server's nuser command.
	 * @param username the username of the new user.
	 */
	public static void addNewUser(String username) {
		
		ClientUser newUser = new ClientUser(username);
		users.add(newUser);
		mainChatWindow.addUserToUserlist(newUser);
	}
	
	/**
	 * Adds this user to the list and opens the login window.
	 */
	public static void startClient () {
		
		users.add(thisUser);
		
		loginWindow = new Login();
		
	}
	
	/**
	 * This gets called only when the server gave back the ok to change the username.
	 * When the loginWindow is open (meaning the user hasn't logged in yet) it closes the loginWindow
	 * and starts the ChatWindow.
	 * @param username The new username
	 */
	public static void setUsername(String username) {
		thisUser.setName(username);
		if (loginWindow != null) {
			loginWindow.closeWindow();
			loginWindow = null;
			if (mainChatWindow == null) {
				sendMessageToServer("cgetu");
			}
		}
		if (mainChatWindow != null) {
			mainChatWindow.setTitle("Username: " + username);
		}
	}
	
	/**
	 * This is called when the server sends back a message after the client requested a username which was already taken.
	 * @param proposedUsername the alternative username (with a number at the end).
	 */
	public static void proposeUsername(String proposedUsername) {
		if (loginWindow != null) {
			loginWindow.proposeUsername(proposedUsername);
		} else if (mainChatWindow != null) {
			mainChatWindow.proposeUsername(proposedUsername);
		} else {
			System.err.println("Why is there no window open?");
		}
	}
	
	/**
	 * Reads in all the users from the cgetu command once we log in with a name.
	 * @param usernames
	 */
	public static void readInAllUsernames(ArrayList<String> usernames) {
		if (mainChatWindow != null) {
			System.err.println("MainChatWindow Window should be null before receiving all usernames!");
		}
		isLoggedIn = true;
		mainChatWindow = new MainChatWindow();
		
		for (String username : usernames) {
			if (username.equals(thisUser.getName())) {
				// We already added ourselves at the start.
				//System.out.println("Received our own name, seems to be working.");
				continue;
			}
			ClientUser user = new ClientUser(username);
			users.add(user);
			mainChatWindow.addUserToUserlist(user);
		}
		
	}
	
	/**
	 * Returns the game with the given name if it exists, otherwise null.
	 * @param gameName The name of the game.
	 * @return the GameController or null.
	 */
	public static GameController getGameByName(String gameName) {
		for (int i = 0; i < runningGameList.size(); i++) {
			if (runningGameList.get(i).getGameName().equals(gameName)) {
				return runningGameList.get(i);
			}
		}
		GameController waitingGame = mainChatWindow.getWaitingGameByName(gameName);
		if (waitingGame != null) {
			return waitingGame;
		}
		return null;
	}
	
	/**
	 * TODO: Gets the Game from the list of running Games(also todo).
	 */
	public static ClientGameRunningController getRunningGameByName(String gameName) {
		return null;
	}
	
	/**
	 * Sends a new message (command) to the server.
	 * @param message the message text.
	 */
	public static void sendMessageToServer (String message) {
		try {
			serverOutputStream.write((message + "\r\n").getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	public static void main(String[] args){
        try{
        	// default values
        	String hostIP = "127.0.0.1";
        	int port = 1030;
        	// read in the command line arguments
        	if (args.length == 2) {
        		port = Integer.parseInt(args[1]);
	        } else if (args.length >= 3) {
        		hostIP = args[1];
        		port = Integer.parseInt(args[2]);
		        if (args.length >= 4) {
        			Client.commandLineUsername = args[3];
		        }
	        }
	        
	        System.out.println("Trying to connect to server with ip " + hostIP + " on port " + port);
	        serverSocket = new Socket(hostIP, port);//starts a new socket that connects to server hosted locally
	        serverInputStream = serverSocket.getInputStream();
	        serverOutputStream = serverSocket.getOutputStream();
	        serverSocket.setSoTimeout(200);
            ClientThread th = new ClientThread(serverSocket);
            th.start();
            startClient();
            BufferedReader commandlineInput = new BufferedReader(new InputStreamReader(System.in));
            String line = "";
            while (true){
                line = commandlineInput.readLine();
                serverOutputStream.write(line.getBytes());
                serverOutputStream.write('\r');
                serverOutputStream.write('\n');
                if(line.equalsIgnoreCase("cquit"))break;
            }
            //stop program
            System.out.println("terminating");
            th.requestStop();
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e){}
            serverInputStream.close();
            serverOutputStream.close();
	        serverSocket.close();
        }catch (IOException e){
            System.err.println();
            System.exit(1);
        }
    }
	
	
}
