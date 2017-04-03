package client.commands;

/**
 * TODO: write a good comment for javadoc
 *
 * Created by m on 3/23/17.
 */
public class ClientCpingHandler extends CommandHandler {
	
	/**
	 * Handles the cping by sending cpong back.
	 * For testing also prints all the currently registered user by this client.
	 */
	@Override
	public void handleCommand() {
		//System.out.println();
		commandParser.getClientCpongSender();
		
		// Just for testing:
		//System.out.println("Current users:");
		//for (ClientUser user : Client.getAllUsers()) {
		//	System.out.println(" " + user.getName());
		//}
	}
	
	@Override
	public void handleAnswer(boolean isOK) {
		// NOT CURRENTLY USED
	}
	
}
