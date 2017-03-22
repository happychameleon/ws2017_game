package server;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by m on 3/14/17.
 */
public class CommandParser {
    private InputStream in;
    private OutputStream out;
    private StringBuffer command = new StringBuffer("");
    private String keyword = "";
    private String argument = "";
	
    
    /**
     * Constructor, gives the class access to input and output to and from client.
     */
    public CommandParser(InputStream in, OutputStream out){
        this.in = in;
        this.out = out;
    }
	
	/**
	 * Calls necessary functions to validate and execute commands.
	 */
    public void validateProtocol(){
        int c;
        try {
            while ((c = in.read()) != -1){

                inputTranslate(in, c);

                inputToCommandArgument();

                KeywordParser keywordParser = new KeywordParser(keyword, argument, this);

                if(isValidCommand()){
                    keywordParser.comparKeyword();
                }
                //clears all global variables
                command.delete(0, command.length());
                keyword = "";
                argument = "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	/**
	 * checks if command is correctly formatted
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
	 * Puts input in an stringbuffer till the termination signal (\r \n)
	 */
    private void inputTranslate(InputStream in, int c){
        int terminat = 0;
        try {
            while (true){
                if(c == '\r'){
                    terminat++;
                }else if(c == '\n'&& terminat == 1){
                    break;
                }
                else{
                    terminat = 0; //reset termination variable
                    command.append((char)c);
                }
                c = in.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	/**
	 * Separates command in to keyword and argument
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
            writeBackToClient(command + " is not a properly formatted command ");
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
	
	
	public void writeToSpecificClient(String output, String username) {
		writeToSpecificClient(output, server.getUserByName(username));
	}
	
	public void writeToSpecificClient(String output, User user) {
		if (user == null) {
			System.err.println("writeToSpecificClient ");
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
	 * Writes the output message to all clients.
	 * @param output
	 */
	public void writeToAllClients(String output) {
		for (User user : server.getAllUsers()) {
			try {
				OutputStream outputStream = user.getSocket().getOutputStream();
				outputStream.write((output + "\r\n").getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
