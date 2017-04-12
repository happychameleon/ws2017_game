package client.commands;

import client.Client;
import game.ClientGameController;
import game.GameMap;
import game.GameState;

import java.util.HashMap;

/**
 * This command is received when a client has opened a new game.
 * It is added to the list of games to choose from where this client can now join it.
 * It also handles the Answer for when a proposed name for a new game is already taken.
 *
 * Created by flavia on 31.03.17.
 */
public class ClientNewgmHandler extends CommandHandler {
	
	
	@Override
	public void handleCommand() {
		if (Client.isLoggedIn() == false)
			return;
		
		int maxPoints = Integer.parseInt(getAndRemoveNextArgumentWord());
		String gameName = getAndRemoveNextArgumentWord();
		String mapName = getAndRemoveNextArgumentWord();
		System.out.printf("maxPoints: %d gameName: %s mapName: %s%n", maxPoints, gameName, mapName);
		
		GameMap map = GameMap.getMapForName(mapName);
		
		ClientGameController newGame = new ClientGameController(GameState.STARTING, maxPoints, gameName, new HashMap<>(), map);
		
		Client.getMainWindow().addGameToList(newGame);
		
		Client.getMainWindow().getMainChatPanel().displayInfo("A new game called " + newGame.getGameName() + " has been created.");
		
	}
	
	@Override
	public void handleAnswer(boolean isOK) {
		if (isOK == false && argument.startsWith("game name taken")) {
			Client.getMainWindow().getMainChatPanel().displayError("The name for the new Game already exists. Please choose a new one!");
		}
	}
	
	/**
	 * Sends the message to ask the server to create the game.
	 * @param maxPoints The max points to choose the team from.
	 * @param gameName The name of the game.
	 * @param gameMap The map of the game.
	 */
	public static void sendGameCreationMessage(int maxPoints, String gameName, GameMap gameMap) {
		Client.sendMessageToServer(String.format("newgm %d %s %s", maxPoints, gameName, gameMap.getName()));
		
	}
}
