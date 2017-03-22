package server;

/**
 * Created by m on 3/20/17.
 */
public class KeywordParser {
	private final CommandParser commandParser;
	private final String keyword;
    private final String argument;
    
	
    public KeywordParser(String keyword, String argument, CommandParser commandParser){
        this.keyword = keyword;
        this.argument = argument;
        this.commandParser = commandParser;
    }
	
	/**
	 * This compares the command and if it is an existing command it creates the correct parser to execute the command.
	 */
	public void compareKeyword(){
        switch (keyword){
            case "uname" :
                UnameParser name = new UnameParser(argument, commandParser);
                break;

            case "cpong" :
                CpingParser ping = new CpingParser(commandParser);
                ping.pingConfirmation();
                break;

            case "chatm" :
                ChatmParser chat = new ChatmParser(argument, commandParser);
                break;

            case "cquit" :
                CquitParser quit = new CquitParser(argument, commandParser);
                break;

            default:
            	// TODO: Add this answer to the protocol.
                commandParser.writeBackToClient("-ERR entered command does not exist");
                break;
        }
    }
}
