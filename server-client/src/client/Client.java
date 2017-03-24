package client;

import login.Chat;
import login.Login;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by m on 3/10/17.
 */

public class Client {
	
	private static Login loginWindow;
	
	private static Chat chatWindow;
	
	private static InputStream serverInputStream;
	private static OutputStream serverOutputStream;
	private static Socket serverSocket;
	
	private static final ClientUser thisUser = new ClientUser();
	
	public static ClientUser getThisUser() {
		return thisUser;
	}
	
	static ArrayList<ClientUser> users = new ArrayList<>();
	
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
			if (chatWindow == null) {
				sendMessageToServer("cgetu");
			}
		}
		if (chatWindow != null) {
			chatWindow.setTitle("Username: " + username);
		}
	}
	
	public static void proposeUsername(String proposedUsername) {
		if (loginWindow != null) {
			loginWindow.proposeUsername(proposedUsername);
		} else if (chatWindow != null) {
			chatWindow.proposeUsername(proposedUsername);
		} else {
			System.err.println("Why is there no window open?");
		}
	}
	
	public static void readInAllUsernames(ArrayList<String> usernames) {
		for (String username : usernames) {
			if (username.equals(thisUser.getName())) {
				// We already added ourselves at the start.
				//System.out.println("Received our own name, seems to be working.");
				continue;
			}
			ClientUser user = new ClientUser(username);
			users.add(user);
		}
		if (chatWindow != null) {
			System.err.println("Chat Window should be null before receiving all usernames!");
		}
		chatWindow = new Chat();
	}
	
	
	public static void sendMessageToServer (String message) {
		try {
			serverOutputStream.write((message + "\r\n").getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	public static void main(String[] args){
        try{
	        serverSocket = new Socket("127.0.0.1", 1030);//starts a new socket that connects to server hosted locally
	        serverInputStream = serverSocket.getInputStream();
	        serverOutputStream = serverSocket.getOutputStream();
	        serverSocket.setSoTimeout(200);
            ClientThread th = new ClientThread(serverInputStream, serverOutputStream);
            th.start();
            startClient();
            BufferedReader comandlinInput = new BufferedReader(new InputStreamReader(System.in));
            String line = "";
            while (true){
                line = comandlinInput.readLine();
                // TODO: are those three lines necessary?
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


class ClientThread extends Thread{
    InputStream in;
    OutputStream out;
    boolean stopreaquest;
    public ClientThread(InputStream in, OutputStream out){
        super();
        this.in = in;
        this.out = out;
        stopreaquest = false;
    }

    public synchronized void requestStop(){
        stopreaquest = true;
    }


    public void run(){
        ClientCommandParser commandParser = new ClientCommandParser(in, out, stopreaquest);
	    commandParser.stopValidateingCommand(stopreaquest);
    }
}