package server;

/**
 * Created by m on 3/20/17.
 */
public class CpingParser {
    CommandParser commandParser;
	
    CpingParser(CommandParser commandParser){
        this.commandParser = commandParser;
    }
    
    public void pingConfirmation(){
    	// TODO: send a message back to the client so the client knows this too?
        System.out.println("ping has been confirmed by client and is still connected to server");
    }
}
