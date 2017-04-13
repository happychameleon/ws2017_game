package server.parser;

import game.ServerGameController;
import server.Server;

/**
 * Starts the game on the server and then tells the Clients about the newly created game.
 *
 * Created by flavia on 12.04.17.
 */
public class StgamHandler extends CommandHandler {
	
	/**
	 * Executes the command from the client.
	 */
	@Override
	public void handleCommand() {
		String wholeArgument = argument;
		
		String gameName = getAndRemoveNextArgumentWord();
		ServerGameController game = Server.getGameByName(gameName);
		
		System.out.println("StgamHandler#handleCommand - telling game to start on server.");
		game.startGame();
		
		Server.writeToAllClients(String.format("stgam %s", wholeArgument));
		
		sendAllCharacterStartPositions(game);
		
	}
	
	/**
	 * TODO: Sends all the starting positions of the characters to all the players.
	 * @param game The game which has just started.
	 */
	private void sendAllCharacterStartPositions(ServerGameController game) {
		System.out.println("StgamHandler#sendAllCharacterStartPositions - UNDONE");
	}
	
	
}
