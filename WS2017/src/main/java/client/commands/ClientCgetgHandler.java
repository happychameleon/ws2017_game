package client.commands;

import client.Client;
import game.ClientGameController;
import game.GameMap;
import game.GameState;
import serverclient.User;

import java.util.HashMap;

/**
 * Reads the answer from the Server after sending the cgetg request.
 * Creates all the currently existing games on the server.
 *
 * Created by flavia on 02.04.17.
 */
public class ClientCgetgHandler extends CommandHandler {
	
	@Override
	public void handleCommand() {
	
	}
	
	
	@Override
	public void handleAnswer(boolean isOK) {
		if (argument.startsWith("waiting ")) {
			getAndRemoveNextArgumentWord();
			parseWaitingGameAnswer();
		} else if (argument.startsWith("running ")) {
			getAndRemoveNextArgumentWord();
			parseRunningGameAnswer();
		} else {
			System.err.println("ClientCgetgHandler - argument wrongly formatted");
		}
	}
	
	/**
	 * If the game is already running, this reads in the needed info and creates the {@link ClientGameController}
	 */
	private void parseRunningGameAnswer() {
		
		String gameName = getAndRemoveNextArgumentWord();
		int startingPoints = Integer.parseInt(getAndRemoveNextArgumentWord());
		String mapName = getAndRemoveNextArgumentWord();
		GameMap map = GameMap.getMapForName(mapName);
		
		HashMap<User, String> users = new HashMap<>();
		String nextUser = getAndRemoveNextArgumentWord();
		while (nextUser.isEmpty() == false) {
			User user = Client.getUserByName(nextUser);
			if (user != null) {
				users.put(user, null);
			} else {
				System.err.println("User with name '" + nextUser + "' is not registered!");
			}
			nextUser = getAndRemoveNextArgumentWord();
		}
		
		ClientGameController game = new ClientGameController(GameState.RUNNING, startingPoints, gameName, users, map);
		
		Client.getMainWindow().addGameToList(game);
	}
	
	/**
	 * If the game is not yet running, this reads in the needed info and creates the {@link ClientGameController}
	 */
	private void parseWaitingGameAnswer() {
		
		String gameName = getAndRemoveNextArgumentWord();
		int maxPoints = Integer.parseInt(getAndRemoveNextArgumentWord());
		String mapName = getAndRemoveNextArgumentWord();
		GameMap map = GameMap.getMapForName(mapName);
		
		HashMap<User, String> users = new HashMap<>();
		String nextUsername = getAndRemoveNextArgumentWord();
		while (nextUsername.isEmpty() == false) {
			User user = Client.getUserByName(nextUsername);
			if (getAndRemoveNextArgumentWord().equals("ready")) {
				// User is ready and has a characterString following
				String characterString = getAndRemoveCharacterString();
				users.put(user, characterString);
			} else {
				// User is still choosing and hasn't got a characterString yet.
				users.put(user, null);
			}
			nextUsername = getAndRemoveNextArgumentWord();
		}
		
		ClientGameController game = new ClientGameController(GameState.STARTING, maxPoints, gameName, users, map);
		
		Client.getMainWindow().addGameToList(game);
	}
	
	
	/**
	 * Separates the characterString from the Argument.
	 * The characterString has to begin at index 0 of the argument (meaning the values before must already be read)
	 * @return the characterString.
	 */
	private String getAndRemoveCharacterString() {
		if (argument.charAt(0) != '[') {
			System.err.println("ClientCgetgHandler#getAndRemoveCharacterString - There was no characterString beginning where one should have been.");
			return null;
		}
		String characterString = argument.substring(0, argument.indexOf("]"));
		argument = argument.substring(argument.indexOf("]") + 1); // remove up to the ]
		if (argument.isEmpty() == false)
			argument = argument.substring(1); // remove the space if the argument continues.
		return characterString;
	}
	
}
