package client;

/**
 * Created by m on 3/23/17.
 */
public class ClientKeywordParser {
    private final ClientCommandParser commandParser;
    private final String keyword;
    private final String argument;


    public ClientKeywordParser(String keyword, String argument, ClientCommandParser commandParser){
        this.keyword = keyword;
        this.argument = argument;
        this.commandParser = commandParser;
    }

    /**
     * This compares the command and if it is an existing command it creates the correct parser to execute the command.
     */
    public void compareKeyword(){
        switch (keyword){
            case "cping" :
                ClientCpingParser ping = new ClientCpingParser(commandParser);
                ping.sendPong();
                break;

            default:
                //commandParser.writeBackToServer("-ERR entered command does not exist");
                break;
        }
    }
}
