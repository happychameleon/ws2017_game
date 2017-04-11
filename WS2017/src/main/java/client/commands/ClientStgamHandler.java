package client.commands;

import client.Client;

/**
 * Created by flavia on 11.04.17.
 */
public class ClientStgamHandler extends CommandHandler {
	/**
	 * Executes the command from the server.
	 */
	@Override
	public void handleCommand() {
		//TODO stgam
	}
	
	/**
	 * Executes the answer received from the server to the specific command this class is for.
	 *
	 * @param isOK <code>true</code> if the answer started with "+OK", <code>false</code> otherwise (answer started with "-ERR").
	 */
	@Override
	public void handleAnswer(boolean isOK) {
		//TODO stgam answer
	}
	
	/**
	 * Sends a message telling the server to start the game.
	 * @param gameName The name of the game to start.
	 */
	public static void sendStartGame(String gameName) {
		Client.sendMessageToServer(String.format("stgam %s %s", gameName, Client.getThisUser().getName()));
	}
}
