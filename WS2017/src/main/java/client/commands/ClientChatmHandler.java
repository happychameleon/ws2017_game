package client.commands;

import client.Client;

/**
 * Created by flavia on 26.03.17.
 */
public class ClientChatmHandler extends CommandHandler {
	
	
	@Override
	public void handleCommand() {
		if (Client.isLoggedIn() && Client.getChatWindow() != null) {
			Client.getChatWindow().receiveMessage(argument);
		}
	}
	
	@Override
	public void handleAnswer(boolean isOK) {
		// NOT CURRENTLY USED
	}
}
