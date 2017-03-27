package client;

import client.commands.ClientCpongSender;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * TODO: write a good comment for javadoc
 *
 * Created by m on 3/23/17
 */
public class ClientCommandParser {
    public Socket serverSocket;
    InputStream in;
    OutputStream out;
    boolean stopreaquest;

    private StringBuffer command = new StringBuffer("");
    private String keyword = "";
    private String argument = "";

    private ClientCpongSender cpongSender = new ClientCpongSender(this);

    /**
     * TODO: write a good comment for javadoc
     * @param serverSocket
     * @param stopreaquest
     */
    public ClientCommandParser(Socket serverSocket, boolean stopreaquest) {
        this.serverSocket = serverSocket;

        try {
            in = serverSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            out = serverSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.stopreaquest = stopreaquest;
        validateCommand();
    }

    public void getClientCpongSender(){
        cpongSender.sendCpong();
    }

    /**
     *TODO: write a good comment for javadoc
     * @param stopreaquest
     */
    public void stopValidatingCommand(boolean stopreaquest){
        this.stopreaquest = stopreaquest;
    }

    /**
     *TODO: write a good comment for javadoc
     */
    private void validateCommand() {
        int len;
        try {
            while (!stopreaquest) {
                try {
                    if ((len = in.read()) == -1) {
                        System.out.println("stooped with -1");
                        break;
                    }
                    inputTranslate(in, len);
                    inputToCommandArgument();
                    if(isValidCommand()){
                        ClientKeywordParser keywordParser = new ClientKeywordParser(keyword, argument, this);
                        keywordParser.compareKeyword();
                    } else if (isValidAnswer()) {
                        ClientAnswerParser answerParser = new ClientAnswerParser(keyword, argument, this);
		                answerParser.compareAnswer();
                    }
                    System.out.println(command.toString());
                    //clears all global variables
                    command.delete(0, command.length());
                    keyword = "";
                    argument = "";
                } catch (InterruptedIOException e) {
                    //try again
                }
            }
        }catch (IOException e) {
            System.err.println("ClientThread: ValidateCommand" + e.toString());
            //e.printStackTrace();
        }
    }

    /**
     *TODO: write a good comment for javadoc
     * @param in
     * @param c
     */
	private void inputTranslate(InputStream in, int c){
        int terminate = 0;
        try {
            while (true){
                try {
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
                } catch (InterruptedIOException e) {
                    //try again
                }
            }
        } catch (IOException e) {
            System.err.println("ClientThread: inputTranslate " + e.toString());
        }
    }

    /**
     *TODO: write a good comment for javadoc
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
        // NOT NEEDED, because it could also be an answer where the keyword is -OK or -ERR.
        //makes sure the keyword is the proper length and if not tells the client that wrong
        //if(keyword.length() != 5){
        //    keyword = "";
        //    argument = "";
        //}
    }

    /**
     * @return <code>true</code> when the received message from the server is a new command,
     * <code>false</code> otherwise.
     */
    private boolean isValidCommand() {
        if (command.charAt(0) == '-') {
            return false;
        }
        if(command.length() == 5){
            return true;
        }
        else if(keyword.length() != 5){
            return false;
        }
        return true;
    }

    /**
     * @return <code>true</code> when the received message from the server is an answer to a command sent from this client,
     * <code>false</code> otherwise.
     */
	private boolean isValidAnswer() {
		if (command.charAt(0) == '-' || command.charAt(0) == '+') {
			//System.out.println("it's an answer!");
			return true;
		}
		return false;
	}
	
}
