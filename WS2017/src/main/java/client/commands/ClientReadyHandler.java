package client.commands;

import client.Client;
import client.ClientUser;
import game.startscreen.ClientGameStartController;

/**
 * Created by flavia on 03.04.17.
 */
public class ClientReadyHandler extends CommandHandler {
	
	@Override
	public void handleCommand() {
		String username = getAndRemoveNextArgumentWord();
		String gameName = getAndRemoveNextArgumentWord();
		String characterString = getCharacterString();
		
		if (characterString == null ||characterString.isEmpty()) {
			System.err.println("ReadyHandler#handleCommand - Character String empty?");
			return;
		}
		
		ClientGameStartController waitingGameByName = Client.getChatWindow().getWaitingGameByName(gameName);
		if (waitingGameByName == null) {
			System.err.println("ReadyHandler#handleCommand - No game found with name: " + gameName);
			return;
		}
		
		ClientUser user = Client.getUserByName(username);
		if (user == null) {
			System.err.println("ReadyHandler#handleCommand - No user found with name: " + username);
			return;
		}
		
		waitingGameByName.moveUserToWaiting(user, characterString);
		
		
	}
	
	@Override
	public void handleAnswer(boolean isOK) {
	
	}
	
	
	private String getCharacterString() {
		
		int index = argument.indexOf("[");
		if (index != 0)
			System.err.println("ReadyHandler#getCharacterString - Something went wrong!");
		return argument.substring(index);
	}
	
}
