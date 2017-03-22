package server;

/**
 * Created by m on 3/21/17.
 */
public class CnameParser {
    private String argument;
    private CommandParser commandParser;

    public CnameParser(String argument, CommandParser commandParser){
        this.argument = argument;
	    this.commandParser = commandParser;
	    
	    handleCname();
    }
	
	private void handleCname() {
    	
	}
	
	
}
