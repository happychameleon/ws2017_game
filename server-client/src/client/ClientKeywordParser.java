package client;

import java.util.ArrayList;

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

            // TODO: Chat command
            
            default:
                //commandParser.writeBackToServer("-ERR entered command does not exist");
                break;
        }
    }
	
	public void compareAnswer() {
     
    	// UNAME Answers
        if (keyword.equals("+OK") && argument.startsWith("you are ")) {
        	String username = argument.substring(8);
        	Client.setUsername(username);
        } else if (keyword.equals("-ERR") && argument.equals("same username entered")) {
        	// just ignore this. Maybe add message later?
		} else if (keyword.equals("-ERR") && argument.startsWith("uname ")) {
        	String proposedUsername = argument.substring(6);
        	Client.proposeUsername(proposedUsername);
		}
		
		// CGETU Answers
		else if (keyword.equals("-OK") && argument.startsWith("cgetu ")) {
        	parseAllUsernames(argument.toCharArray());
        }
        
        // CHATM Answers
		
		
		
		// TODO: "-ERR entered command does not exist"
	}
	
	/**
	 * This is for receiving all usernames when logging in as an answer to the cgetu command.
	 * @param argument
	 */
	private void parseAllUsernames(char[] argument) {
    	String usernameSeparator = " ";
		ArrayList<String> usernames = new ArrayList<>();
		String currentUsername = "";
		for (int i = 6; i < argument.length; i++) {
			if (argument[i] == ' ') {
				usernames.add(currentUsername);
				currentUsername = "";
			} else {
				currentUsername += argument[i];
				if (i == argument.length - 1) {
					// last user isn't recognised with a space, has to be added here.
					usernames.add(currentUsername);
				}
			}
		}
		Client.readInAllUsernames(usernames);
	}
}
