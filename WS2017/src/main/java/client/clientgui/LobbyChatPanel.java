package client.clientgui;

import client.Client;
import client.ClientUser;
import game.GameController;

import java.util.ArrayList;

/**
 * Created by flavia on 06.04.17.
 */
public class LobbyChatPanel extends ChatPanel {
	
	/**
	 * The name of the game this lobby belongs to.
	 */
	private final GameController game;
	
	/**
	 * Creates a Chat Panel where users can Chat.
	 *
	 * @param chatUsers   The Users in this Chat.
	 * @param chatCommand The command to send the chat message with (eg chatm or chatw).
	 * @param game The name of the game this lobby is for.
	 */
	public LobbyChatPanel(ArrayList<ClientUser> chatUsers, String chatCommand, GameController game) {
		super(chatUsers, chatCommand);
		this.game = game;
	}
	
	
	/**
	 * Sends the text in the chat text field {@link #chatInputTextField} as a chat message to all the connected clients.
	 */
	@Override
	protected void sendMessage() {
		String message = chatInputTextField.getText();
		if (message.isEmpty())
			return;
		
		ArrayList<ClientUser> receivers = new ArrayList<>();
		for (ClientUser user : chatUsers) {
			receivers.add(user);
		}
		ClientUser sender = Client.getThisUser();
		for (ClientUser receiver : receivers) {
			Client.sendMessageToServer(chatCommand + " " + game.getGameName() + " " + sender.getName() + " " + receiver.getName() + " '" + message + "'");
		}
		chatInputTextField.setText("");
	}
	
	
}
