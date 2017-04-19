package client.commands;

import client.Client;
import client.ClientUser;
import game.ClientGameController;

/**
 * Informs the ClientGameStartController from the given game about the given user which has chosen their team.
 *
 * Created by flavia on 03.04.17.
 */
public class ClientReadyHandler extends ClientCommandHandler {
	
	@Override
	public void handleCommand() {
		if (Client.isLoggedIn() == false)
			return;
		
		String username = getAndRemoveNextArgumentWord();
		String gameName = getAndRemoveNextArgumentWord();
		String characterString = getCharacterString();
		
		if (characterString == null ||characterString.isEmpty()) {
			System.err.println("ReadyHandler#handleCommand - Character String empty?");
			return;
		}
		
		ClientGameController waitingGameByName = Client.getMainWindow().getWaitingGameByName(gameName);
		if (waitingGameByName == null) {
			System.err.println("ReadyHandler#handleCommand - No game found with name: " + gameName);
			return;
		}
		
		ClientUser user = Client.getUserByName(username);
		if (user == null) {
			System.err.println("ReadyHandler#handleCommand - No user found with name: " + username);
			return;
		}
		
		waitingGameByName.setUserAsWaiting(user, characterString);
		
		
	}
	
	@Override
	public void handleAnswer(boolean isOK) {
	
	}
	
	/**
	 * Separates the characterString from the argument.
	 * @return the characterString.
	 */
	private String getCharacterString() {
		
		int index = argument.indexOf("[");
		if (index != 0)
			System.err.println("ReadyHandler#getCharacterString - Something went wrong!");
		return argument.substring(index);
	}
	
}
