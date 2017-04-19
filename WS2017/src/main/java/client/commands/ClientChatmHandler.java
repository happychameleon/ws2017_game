package client.commands;

import client.Client;
import client.ClientUser;
import client.clientgui.ChatMessage;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Parses the command and displays the newly received Message on the MainChatWindow Window.
 *
 * Created by flavia on 26.03.17.
 */
public class ClientChatmHandler extends ClientCommandHandler {
	
	
	@Override
	public void handleCommand() {
		if (Client.isLoggedIn() && Client.getMainWindow() != null) {
			ChatMessage chatMessage = getChatMessageFromArgument();
			Client.getMainWindow().getMainChatPanel().addNewMessage(chatMessage);
		}
	}
	
	/**
	 * Creates the {@link ChatMessage} from the argument.
	 *
	 * @return the new ChatMessage
	 */
	protected ChatMessage getChatMessageFromArgument() {
		String senderName = getAndRemoveNextArgumentWord();
		String receiverName = getAndRemoveNextArgumentWord();
		String message = argument;
		
		ClientUser sender = Client.getUserByName(senderName);
		ClientUser receiver = Client.getUserByName(receiverName);
		
		// To get rid of the '
		message = message.substring(1, message.length() - 1);
		
		return new ChatMessage(message, sender, new ArrayList<>(Arrays.asList(receiver)));
	}
	
	@Override
	public void handleAnswer(boolean isOK) {
		// NOT CURRENTLY USED
	}
	
	
}
