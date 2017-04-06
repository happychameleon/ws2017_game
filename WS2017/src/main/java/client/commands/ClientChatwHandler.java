package client.commands;

import client.Client;
import client.ClientUser;
import client.clientgui.ChatMessage;
import client.clientgui.ChatPanel;
import client.clientgui.MainChatWindow;

/**
 * Displays the message in the correct whisper tab at the {@link MainChatWindow}
 * Opens a new tab if the chat with the user hasn't been opened yet.
 *
 * Created by flavia on 05.04.17.
 */
public class ClientChatwHandler extends ClientChatmHandler {
	
	@Override
	public void handleCommand() {
		if (Client.isLoggedIn() && Client.getMainChatWindow() != null) {
			
			handleWhisperChat();
			
		}
	}

	/**
	 * Displays the message in the correct whisper tab at the {@link MainChatWindow}
	 * Opens a new tab if the chat with the user hasn't been opened yet.
	 */
	private void handleWhisperChat() {
		ChatMessage chatMessage = getChatMessageFromArgument();
		MainChatWindow mainChatWindow = Client.getMainChatWindow();
		
		ChatPanel whisperChatForUser = null;
		if (chatMessage.getSender() != Client.getThisUser()) {
			// The message was sent from another user to this user.
			whisperChatForUser = mainChatWindow.getWhisperChatForUser(chatMessage.getSender());
			if (whisperChatForUser == null) {
				whisperChatForUser = mainChatWindow.openWhisperChat(chatMessage.getSender());
			}
			
		} else if (chatMessage.getSender() == Client.getThisUser()) {
			// The message was sent from this user to the receiver.
			ClientUser receiver = chatMessage.getReceivers().get(0); // There should only be one receiver.
			whisperChatForUser = mainChatWindow.getWhisperChatForUser(receiver);
			if (whisperChatForUser == null) {
				// Just for the unlikely case when the user closed the tab while the message was on the way.
				whisperChatForUser = mainChatWindow.openWhisperChat(receiver);
			}
			
		}
		
		assert whisperChatForUser != null;
		whisperChatForUser.addNewMessage(chatMessage);
	}
}
