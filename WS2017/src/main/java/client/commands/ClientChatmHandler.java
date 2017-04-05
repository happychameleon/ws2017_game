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
public class ClientChatmHandler extends CommandHandler {
	
	
	@Override
	public void handleCommand() {
		if (Client.isLoggedIn() && Client.getChatWindow() != null) {
			ChatMessage chatMessage = getChatMessageFromArgument();
			Client.getChatWindow().getMainChatPanel().addNewMessage(chatMessage);
		}
	}
	
	/**
	 * Creates the {@link ChatMessage} from the argument.
	 *
	 * @return the new ChatMessage
	 */
	protected ChatMessage getChatMessageFromArgument() {
		char[] argumentChars = argument.toCharArray();
		String senderName = "";
		String receiverName = "";
		String message = "";
		int argumentNr = 1;
		// split the whole argument into the three argument parts
		for (char c : argumentChars) {
			if (argumentNr < 3 && c == ' ') {
				argumentNr++;
				continue;
			}
			if (argumentNr == 1)
				senderName += c;
			else if (argumentNr == 2)
				receiverName +=c;
			else
				message += c;
		}
		ClientUser sender = Client.getUserByName(senderName);
		ClientUser receiver = Client.getUserByName(receiverName);
		if (receiver != Client.getThisUser()) {
			System.err.println("WARNING: This message isn't intended for this client! Or did someone miss a namechange?");
		}
		// To get rid of the '
		message = message.substring(1, message.length() - 1);
		
		return new ChatMessage(message, sender, new ArrayList<>(Arrays.asList(receiver)));
	}
	
	@Override
	public void handleAnswer(boolean isOK) {
		// NOT CURRENTLY USED
	}
	
	
}
