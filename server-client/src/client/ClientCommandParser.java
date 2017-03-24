package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.lang.invoke.MethodType;

/**
 * Created by m on 3/23/17
 */
public class ClientCommandParser {
    InputStream in;
    OutputStream out;
    boolean stopreaquest;

    private StringBuffer command = new StringBuffer("");
    private String keyword = "";
    private String argument = "";

    public ClientCommandParser(InputStream in, OutputStream out, boolean stopreaquest) {
        this.in = in;
        this.out = out;
        this.stopreaquest = stopreaquest;
        validateCommand();
    }

    public void stopValidateingCommand(boolean stopreaquest){
        this.stopreaquest = stopreaquest;
    }

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
                    ClientKeywordParser keywordParser = new ClientKeywordParser(keyword, argument, this);
                    if(isValidCommand()){
                        keywordParser.compareKeyword();
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
            //writeBackToServer("-ERR " + command + " is not a properly formatted command");
            keyword = "";
            argument = "";
        }
    }

    private boolean isValidCommand() {
        if(command.length() == 5){
            System.out.println("length 5");
            return true;
        }
        else if(keyword.length() != 5){
            System.out.println("return false, length not 5");
            return false;
        }
        return true;
    }

    public void writeBackToServer(String output){
        try {
            out.write((output + "\r\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
