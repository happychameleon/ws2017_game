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

    public void comparKeyword(){
        switch (keyword){
            case "uname" :
                UnameParser name = new UnameParser(argument, commandParser);
                break;

            case "cpong" :
                CpingParser ping = new CpingParser(commandParser);
                ping.pingConfermation();
                break;

            case "cname" :
                CnameParser namechange = new CnameParser(argument, commandParser);
                break;

            case "chatm" :
                ChatmParser chat = new ChatmParser(argument, commandParser);
                break;

            case "cquit" :
                CquitParser quit = new CquitParser(argument, commandParser);
                break;

            default:
                System.out.println("The entered keyword is not a valid input!");
                break;
        }
    }
}
