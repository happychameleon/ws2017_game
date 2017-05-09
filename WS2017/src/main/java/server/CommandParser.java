package server;


import game.ServerGameController;
import serverclient.User;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * CommandParser parses the bytestream sent by a client and parses commands from it and separates
 * the commands in keywords and argument. The CommandParser can also write back to a client connected
 * to the server.
 *
 * Created by m on 3/14/17.
 */
public class CommandParser {
	
	/**
	 * The receivingUser from which this CommandParser reads in the commands.
	 */
	private final ServerUser receivingUser;
	
	/**
	 * The {@link InputStreamReader} of this {@link #receivingUser}
	 */
	private InputStreamReader in;
	
	public InputStreamReader getIn() {
		return in;
	}
	
	/**
	 * The {@link OutputStream} of this {@link #receivingUser}
	 */
    private OutputStream out;
    private Socket socket;
	
	public Socket getSocket() {
		return socket;
	}
	
	private StringBuffer command = new StringBuffer("");
    private String keyword = "";
    private String argument = "";
	
	/**
	 * Creates a new PingSender so that a ping can be sent as soon as the validateProtocol
	 * function is called.
	 */
	private PingSender pingSender;
	
	public PingSender getPingSender() {
		return pingSender;
	}
	
	/**
	 * Whenever the client should quit after a command (e.g. via the cquit command)
	 * this variable has to be set to true.
	 */
	public boolean shouldQuit = false;

    /**
     * Constructor, gives the class access to input and output to and from client.
     * @param socket {@link #socket}.
     * @param user {@link #receivingUser}.
     */
    public CommandParser(Socket socket, ServerUser user){
        try {
            in = new InputStreamReader(socket.getInputStream(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.socket = socket;
        this.receivingUser = user;
    }
	
	/**
	 * @return {@link #receivingUser}
	 */
	public ServerUser getReceivingUser() {
		return receivingUser;
	}

	/**
	 * Calls necessary functions to validate and execute commands.
	 */
    public void validateProtocol(){
        pingSender = new PingSender(this);
        pingSender.sendPing();
	    int c;
	    try {
		    c = in.read();
	    } catch (IOException e) {
	    	c = -1;
	    }
        while (c != -1){
            if (shouldQuit) {
	            break;
            }
            
            inputTranslate(in, c);

            inputToCommandArgument();

            KeywordParser keywordParser = new KeywordParser(keyword, argument, pingSender, this);

            if(isValidCommand()){
                keywordParser.compareKeyword();
            }
            
	        //prints all commands from server except cpong
	        if (keyword.equals("cpong") == false)
		        System.out.println(command.toString());
            //clears all global variables
            command.delete(0, command.length());
            keyword = "";
            argument = "";
            
            try {
                c = in.read();
            } catch (IOException e) {
            	c = -1;
            }
        }
        pingSender.terminatePingThread();
		    
    }
	
	/**
	 * Checks if command is correctly formatted
	 */
    private boolean isValidCommand() {
        if(command.length() == 5){
            return true;
        }
        else if(keyword.length() != 5){
            return false;
        }
        return true;
    }
	
	/**
	 * Puts input in a {@link StringBuffer} until the termination signal (\r \n) is reached.
	 */
    private void inputTranslate(InputStreamReader in, int c){
        int terminate = 0;
        try {
            while (true){
                if(c == '\r'){
                    terminate++;
                }else if(c == '\n'&& terminate == 1){
                    break;
                }
                else{
                    terminate = 0; //reset termination variable
                    command.append((char)c);
                }
                c = in.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	/**
	 * Separates the command into keyword and argument.
	 */
    private void inputToCommandArgument() {
        //checks for space between keyword and argument in command string
        int keywordEnd = command.indexOf(" ");
        if(keywordEnd > 0){
            keyword = command.substring(0, keywordEnd);
        }
        //puts the keyword bit of the command in to the variable 'keyword'
        else if(command.length() == 5){
            keyword = command.toString();
        }
        //puts the argument part of the command in to the variable 'argument'
        if(keywordEnd > 0 && command.length() > keywordEnd){
            argument = command.substring(keywordEnd+1, command.length());
        }
        //makes sure the keyword is the proper length and if not tells the client that wrong
        if(keyword.length() != 5){
            writeBackToClient("-ERR " + command + " is not a properly formatted command");
            keyword = "";
            argument = "";
        }
    }
	
	/**
	 * This can be called to write directly to the client who sent the command.
	 * @param output The message to write.
	 */
    public void writeBackToClient(String output){
        try {
            out.write((output + "\r\n").getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	/**
	 * This gets the User for the given username and calls {@link #writeToSpecificClient(String, ServerUser)}
	 * @param output The message to write.
	 * @param username The name to translate into the User. Check first if the username exists!
	 */
	public void writeToSpecificClient(String output, String username) {
		writeToSpecificClient(output, Server.getUserByName(username));
	}
	
	/**
	 * This writes a message to the specific receivingUser.
	 * @param output The message to write.
	 * @param user The User to write to.
	 */
	public void writeToSpecificClient(String output, ServerUser user) {
		if (user == null) {
			System.err.println("CommandParser#writeToSpecificClient - Must enter a valid receivingUser!");
			return;
		}
		
		try {
			OutputStream outputStream = user.getSocket().getOutputStream();
			outputStream.write((output + "\r\n").getBytes("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes the output message to all clients except to the one who sent the command.
	 * @param output the message
	 */
	public void writeToAllOtherClients(String output) {
		for (ServerUser user : Server.getAllUsers()) {
			if (user == this.receivingUser) {
				continue;
			}
			try {
				OutputStream outputStream = user.getSocket().getOutputStream();
				outputStream.write((output + "\r\n").getBytes("UTF-8"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Writes a message to all Clients playing and watching the specified game.
	 * @param message The message to send.
	 * @param gameController The game the Clients are from.
	 */
	public void writeToAllGamingClients(String message, ServerGameController gameController) {
		ArrayList<User> usersToSend = new ArrayList<>();
		usersToSend.addAll(gameController.getAllUsers());
		usersToSend.addAll(gameController.getWatchingUsers());
		for (User u : usersToSend) {
			try {
				writeToSpecificClient(message, (ServerUser) u);
			} catch (ClassCastException e) {
				e.printStackTrace(); //just to be sure
			}
		}
	}
}
