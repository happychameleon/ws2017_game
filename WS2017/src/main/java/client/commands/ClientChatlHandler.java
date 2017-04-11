package client.commands;

import client.Client;
import client.clientgui.ChatMessage;
import game.ClientGameController;

/**
 * Created by flavia on 06.04.17.
 */
public class ClientChatlHandler extends ClientChatmHandler {
	
	/**
	 * Handles the chatl command similar to the chatm command.
	 * Adds the chat message to the correct lobby chat instead of the main chat.
	 *
	 * @see ClientChatmHandler
	 */
	@Override
	public void handleCommand() {
		if (Client.isLoggedIn()) {
			System.out.println("ClientChatlHandler#handleCommand - argument: " + argument);
			
			String gameName = getAndRemoveNextArgumentWord();
			ClientGameController game = Client.getGameByName(gameName);
			if (game == null) {
				System.err.println("No game found with name " + gameName);
				return;
			}
			
			ChatMessage chatMessage = getChatMessageFromArgument();
			
			game.getGameLobby().getLobbyChat().addNewMessage(chatMessage);
		}
	}
	
}
