package server.parser;


import game.startscreen.ServerGameStartController;
import server.Server;
import server.ServerUser;

/**
 * Calls the {@link ServerGameStartController#moveUserToWaiting} method of the specified game.
 *
 * Created by flavia on 31.03.17.
 */
public class ReadyHandler extends CommandHandler {
	
	@Override
	public void handleCommand() {
		String username = getAndRemoveNextArgumentWord();
		String gameName = getAndRemoveNextArgumentWord();
		String characterString = getCharacterString();
		
		if (characterString == null ||characterString.isEmpty()) {
			System.err.println("ReadyHandler#handleCommand - Character String empty?");
			return;
		}
		
		ServerGameStartController waitingGame = Server.getWaitingGameByName(gameName);
		if (waitingGame == null) {
			System.err.println("ReadyHandler#handleCommand - No game found with name: " + gameName);
			return;
		}
		
		ServerUser user = Server.getUserByName(username);
		if (user == null) {
			System.err.println("ReadyHandler#handleCommand - No user found with name: " + username);
			return;
		}
		
		waitingGame.moveUserToWaiting(user, characterString);
		
		String message = String.format("ready %s %s %s", username, gameName, characterString);
		Server.writeToAllClients(message);
		System.out.println("ReadyHandler#handleCommand - Done");
	}
	
	/**
	 * @return the characterString from the argument.
	 */
	private String getCharacterString() {
		
		int index = argument.indexOf("[");
		if (index != 0)
			System.err.println("ReadyHandler#getCharacterString - Something went wrong!");
		return argument.substring(index);
	}
	
}
