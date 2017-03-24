package login;

import client.Client;
import client.ClientUser;

import java.util.ArrayList;

/**
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
	
	/**
	 * This constructor is used to send new Messages to other users.
	 * It creates the message and sends it via the server to the user(s).
	 * @param message the message text.
	 * @param receivers the users to receive the message.
	 */
	public ChatMessage(String message, ArrayList<ClientUser> receivers) {
		this.message = message;
		this.sender = Client.getThisUser();
		this.receivers = receivers;
		
		for (ClientUser reveiver : receivers) {
			Client.sendMessageToServer("chatm " + sender.getName() + " " + reveiver.getName() + " '" + message + "'");
		}
	}
}
