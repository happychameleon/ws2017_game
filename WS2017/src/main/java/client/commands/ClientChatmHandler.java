package client.commands;

import client.Client;

/**
 * Created by flavia on 26.03.17.
 */
public class ClientChatmHandler extends CommandHandler {
	
	
	@Override
	public void handleCommand(String argument) {
		if (Client.isLoggedIn() && Client.getChatWindow() != null) {
			Client.getChatWindow().receiveMessage(argument);
		}
	}
	
	@Override
	public void handleAnswer(String argument, boolean isOK) {
		// NOT CURRENTLY USED
	}
}
