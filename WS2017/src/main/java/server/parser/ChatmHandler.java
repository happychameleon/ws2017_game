package server.parser;


import server.Server;
import server.ServerUser;

/**
 * Handles an incoming chat message and sends it to the correct Client(s).
 *
 * Created by m on 3/21/17.
 */
public class ChatmHandler extends CommandHandler {
	
	protected String senderName = "";
	protected String recipientName = "";
	
	String command;
	
	@Override
	public void handleCommand() {
		System.out.println("ChatmHandler#handleCommand");
		
		command = "chatm";
		
		handleChatMessage();
	}
	
	/**
	 * Sends the chat message along to the receiver.
	 */
	void handleChatMessage() {
		String wholeArgument = argument;
		
		separateArgument();
		if (senderName.isEmpty() || recipientName.isEmpty()) {
			System.err.println("Argument for command CHATM not properly formatted!\n" +
					"Please use format: " + command + " <sender_name> <recipient_name> '<message>'!\n" +
					"Don't forget to surround the <message> with 'apostrophes'!");
			return;
		}
		
		ServerUser sender = Server.getUserByName(senderName);
		ServerUser recipient = Server.getUserByName(recipientName);
		
		if (recipient == null) {
			commandParser.writeBackToClient("-ERR " + command + " entered recipient name '" + recipientName + "' doesn't exist!");
			return;
		} else if (sender == null) {
			commandParser.writeBackToClient("-ERR " + command + " entered sender name '" + senderName + "' doesn't exist!");
			return;
		}
		
		String message = command + " " + wholeArgument;
		
		commandParser.writeToSpecificClient(message, recipientName);
		
	}
	
	
	/**
	 * This method checks the argument and separates the given arguments into the arguments needed for the Chat.
	 */
	protected void separateArgument() {
		senderName = getAndRemoveNextArgumentWord();
		recipientName = getAndRemoveNextArgumentWord();
		
	}
	
	
}
