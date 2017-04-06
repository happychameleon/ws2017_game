package client.commands;

import client.Client;
import client.clientgui.ChatMessage;
import game.GameController;

/**
 * Created by flavia on 06.04.17.
 */
public class ClientChatlHandler extends ClientChatmHandler {
	
	@Override
	public void handleCommand() {
		if (Client.isLoggedIn()) {
			System.out.println("ClientChatlHandler#handleCommand - argument: " + argument);
			
			String gameName = getAndRemoveNextArgumentWord();
			GameController game = Client.getGameByName(gameName);
			if (game == null) {
				System.err.println("No game found with name " + gameName);
				return;
			}
			
			ChatMessage chatMessage = getChatMessageFromArgument();
			
			game.getGameLobby().getLobbyChat().addNewMessage(chatMessage);
		}
	}
	
}
