package client.commands;

/**
 * Handles the cping by sending cpong back.
 *
 * Created by m on 3/23/17.
 */
public class ClientCpingHandler extends ClientCommandHandler {
	
	/**
	 * Handles the cping by sending cpong back.
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
