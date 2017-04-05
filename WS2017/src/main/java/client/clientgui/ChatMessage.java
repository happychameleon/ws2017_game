package client.clientgui;

import client.ClientUser;

import java.util.ArrayList;

/**
 * This represents a single Chat message sent between the specified users.
 *
 * Created by flavia on 24.03.17.
 */
public class ChatMessage {
	
	private String message;
	
	public String getMessage() {
		return message;
	}
	
	private ClientUser sender;
	
	public ClientUser getSender() {
		return sender;
	}
	
	private ArrayList<ClientUser> receivers;
	
	public ArrayList<ClientUser> getReceivers() {
		return receivers;
	}
	
	/**
	 * This constructor is for incoming messages.
	 * It just stores all the values received.
	 * @param message The message body.
	 * @param sender The sender of the message.
	 * @param receivers The receiver of the message.
	 */
	public ChatMessage(String message, ClientUser sender, ArrayList<ClientUser> receivers) {
		this.message = message;
		this.sender = sender;
		this.receivers = receivers;
	}
	
	
}
