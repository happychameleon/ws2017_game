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

    //constructur, gives the class acess to input and output to and from client
    public CommandParser(InputStream in, OutputStream out){
        this.in = in;
        this.out = out;
    }

    //calls necessary functions to validate and execute commands
    public void validateProtocol(){
        int c;
        try {
            while ((c = in.read()) != -1){

                inputTranslate(in, c);

                inputToCommandArgument();

                KeywordParser keywordParser = new KeywordParser(keyword, argument);

                if(isValidCommand()){
                    keywordParser.comparKeyword();
                }
                //clears all gloable veriables
                command.delete(0, command.length());
                keyword = "";
                argument = "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //checks if command is correctly formated
    private boolean isValidCommand() {
        if(command.length() == 5){
            return true;
        }
        else if(keyword.length() != 5){
            return false;
        }
        return true;
    }

    //puts input in an stringbuffer till the termination signal(\r \n)
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

    //seperates command in to keyword and argument
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
            wirteToClient(command + " is not a properly formated command ");
            keyword = "";
            argument = "";
        }
    }

    //function can be called to write directly to client
    private void wirteToClient(String output){
        try {
            out.write((output + "\r\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
