package client.commands;

import client.Client;
import game.ClientGameController;

import java.util.ArrayList;

/**
 * Handles the requests by the Clients to start watching a game.
 *
 * Created by flavia on 27.04.17.
 */
public class ClientWtchgHandler extends ClientCommandHandler {
	/**
	 * Executes the command from the server.
	 */
	@Override
	public void handleCommand() {
	
	}
	
	/**
	 * Executes the answer received from the server to the specific command this class is for.
	 *
	 * @param isOK <code>true</code> if the answer started with "+OK", <code>false</code> otherwise (answer started with "-ERR").
	 */
	@Override
	public void handleAnswer(boolean isOK) {
		String gameName = getAndRemoveNextArgumentWord();
		ArrayList<String> characterStrings = new ArrayList<>();
		while (argument.isEmpty() == false) {
			characterStrings.add(getAndRemoveNextCharacterString());
		}
		ClientGameController game = Client.getGameByName(gameName);
		
		assert game != null;
		
		game.addWatchingUser(Client.getThisUser());
		
		game.startGameForWatching(characterStrings);
	}
	
	/**
	 * Returns the next CharacterString from the argument.
	 * @return The [CharacterString].
	 */
	private String getAndRemoveNextCharacterString() {
		int firstIndex = argument.indexOf("[");
		int lastIndex = argument.indexOf("]");
		String characterString = argument.substring(firstIndex, lastIndex + 1);
		argument = argument.substring(lastIndex + 1);
		if (argument == null || argument.isEmpty()) {
			argument = "";
		} else {
			argument = argument.substring(1); // for the space.
		}
		return characterString;
	}
	
	
	/**
	 * Tells the Server that this user wants to watch a game.
	 * @param gameController The game to watch.
	 */
	public static void askToWatchGame(ClientGameController gameController) {
		String username = Client.getThisUser().getName();
		String gamename = gameController.getGameName();
		
		Client.sendMessageToServer(String.format("wtchg %s %s", gamename, username));
	}
}
