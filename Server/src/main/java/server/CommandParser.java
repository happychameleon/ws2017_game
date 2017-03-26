package server;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * TODO: description
 *
 * Created by m on 3/14/17.
 */
public class CommandParser {
	/**
	 * The receivingUser from which this CommandParser reads in the commands.
	 */
	private final User receivingUser;
	/**
	 * The {@link InputStream} of this {@link #receivingUser}
	 */
	private InputStream in;
	/**
	 * The {@link OutputStream} of this {@link #receivingUser}
	 */
    private OutputStream out;
    private StringBuffer command = new StringBuffer("");
    private String keyword = "";
    private String argument = "";
	
	private CpingSender cpingSender;
	
	public CpingSender getCpingSender() {
		return cpingSender;
	}
	
	/**
	 * Whenever the client should quit after a command (e.g. via the cquit command) this variable has to be set to true.
	 */
	public boolean shouldQuit = false;

    /**
     * Constructor, gives the class access to input and output to and from client.
     */
    public CommandParser(InputStream in, OutputStream out, User user){
        this.in = in;
        this.out = out;
        this.receivingUser = user;
    }
	
	/**
	 * @return {@link #receivingUser}
	 */
	public User getReceivingUser() {
		return receivingUser;
	}

	/**
	 * Calls necessary functions to validate and execute commands.
	 */
    public void validateProtocol(){
        cpingSender = new CpingSender(this);
        cpingSender.sendPing();
        int c;
        try {
            while ((c = in.read()) != -1){
                inputTranslate(in, c);

                inputToCommandArgument();

                KeywordParser keywordParser = new KeywordParser(keyword, argument, cpingSender, this);

                if(isValidCommand()){
                    keywordParser.compareKeyword();
                }
                if (shouldQuit) {
                	break;
                }
                //clears all global variables
                command.delete(0, command.length());
                keyword = "";
                argument = "";
            }
            cpingSender.terminatePingThread();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    private void inputTranslate(InputStream in, int c){
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
	 */
    public void writeBackToClient(String output){
        try {
            out.write((output + "\r\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	/**
	 * This gets the User for the given username and calls {@link #writeToSpecificClient(String, User)}
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
	public void writeToSpecificClient(String output, User user) {
		if (user == null) {
			System.err.println("CommandParser#writeToSpecificClient - Must enter a valid receivingUser!");
			return;
		}
		
		try {
			OutputStream outputStream = user.getSocket().getOutputStream();
			outputStream.write((output + "\r\n").getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes the output message to all clients except to the one who sent the command.
	 * @param output the message
	 */
	public void writeToAllOtherClients(String output) {
		for (User user : Server.getAllUsers()) {
			if (user == this.receivingUser) {
				continue;
			}
			try {
				OutputStream outputStream = user.getSocket().getOutputStream();
				outputStream.write((output + "\r\n").getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}