package login;

import client.ClientUser;

/**
 * Created by flavia on 24.03.17.
 */
public class ChatMessage {
	
	private String message;
	
	private ClientUser sender;
	
	public ChatMessage(String message, ClientUser sender) {
		this.message = message;
		this.sender = sender;
	}
}
