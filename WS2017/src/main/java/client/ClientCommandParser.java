package client;

import client.commands.ClientCpingHandler;

import java.io.*;
import java.net.Socket;

/**
 * ClientCommandParser parses the bytestream sent by the server, parses commands from it and separates
 * the commands in keywords and argument.
 *
 * Created by m on 3/23/17
 */
public class ClientCommandParser {
    public Socket serverSocket;
    InputStreamReader in;

    public InputStreamReader getIn() {
        return in;
    }

    OutputStream out;
    boolean stopreaquest;

    private StringBuffer command = new StringBuffer("");
    private String keyword = "";
    private String argument = "";

    private ClientCpingHandler cpongSender = new ClientCpingHandler();

    /**
     * Constructor for ClientCommandParser creates an inputstreamreader and outputstream from the serversocket.
     * @param serverSocket The ServerSocket from which the input and output stream are taken.
     * @param stopRequest The StopRequest.
     */
    public ClientCommandParser(Socket serverSocket, boolean stopRequest) {
        this.serverSocket = serverSocket;

        try {
            in = new InputStreamReader(serverSocket.getInputStream(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            out = serverSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.stopreaquest = stopRequest;
        validateCommand();
    }
    
    public void getClientCpongSender(){
        cpongSender.sendCpong();
    }

    /**
     * stopValidatingCommand can stop the while loop in validateCommand() by setting stoprequest to true
     * which causes the ClientThread to stop.
     * @param stoprequest the StopRequest.
     */
    public void stopValidatingCommand(boolean stoprequest){
        this.stopreaquest = stoprequest;
    }

    /**
     * validateCommand() is called by ClientThread to parse and validate input from the server.
     */
    private void validateCommand() {
        int len;
        try {
            while (!stopreaquest) {
                try {
                    if ((len = in.read()) == -1) {
                        System.out.println("stopped with -1");
                        break;
                    }
                    inputTranslate(in, len);
                    inputToKeywordArgument();
                    if(isValidCommand()){
                        ClientKeywordParser keywordParser = new ClientKeywordParser(keyword, argument, this);
                        keywordParser.compareKeyword();
                    } else if (isValidAnswer()) {
                        ClientAnswerParser answerParser = new ClientAnswerParser(keyword, argument, this);
		                answerParser.compareAnswer();
                    }
                    //prints all commands from server except cping
                    if (keyword.equals("cping") == false)
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
     * Takes the InputStreamReader from the server and buffers it in the command StringBuffer till the
     * function reads the terminating signal for a command.
     * @param in InputStreamReader for input from server.
     * @param c first character from InputStream represented in integer form.
     */
	private void inputTranslate(InputStreamReader in, int c){
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
     * inputToKeywordArgument splits the command in to the keyword and the argument.
     */
    private void inputToKeywordArgument() {
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
        if (command.length() == 0) {
	        System.err.println("Command Empty?!");
	        return false;
        }
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
		if (command.length() == 0) {
			//System.err.println("Command Empty?!");
			return false;
		}
		if (command.charAt(0) == '-' || command.charAt(0) == '+') {
			//System.out.println("it's an answer!");
			return true;
		}
		return false;
	}
	
}
