package server;

/**
 * Created by m on 3/20/17.
 */
public class CquitParser {
    public String argument;

    public CquitParser(String argument, CommandParser commandParser){
        this.argument = argument;
        
        if (argument != null && argument.length() != 0) {
            commandParser.writeBackToClient("-ERROR: cquit command takes no argument(s)!");
            return;
        }
	    commandParser.writeBackToClient("+OK ’terminating tasks and disconnecting’");
        commandParser.shouldQuit = true;
    }
}
