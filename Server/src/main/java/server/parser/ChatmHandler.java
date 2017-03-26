package server.parser;


import server.Server;
import server.User;

/**
 * Created by m on 3/21/17.
 */
public class ChatmHandler extends CommandHandler {
	
	private String senderName;
	private String recipientName;
	
	
	@Override
	public void handleCommand(String argument) {
		if (separateArgument(argument) == false) {
			commandParser.writeBackToClient("Argument for command CHATM not properly formatted!\n" +
					"Please use format: 'chatm <sender_name> <recipient_name> <message>'!\n" +
					"Don't forget to surround the <message> with 'apostrophes'!");
			// TODO: send this message back to the client.
			return;
		}
		
		User sender = Server.getUserByName(senderName);
		User recipient = Server.getUserByName(recipientName);
		
		if (recipient == null) {
			commandParser.writeBackToClient("ERROR: The entered recipient name '" + recipientName + "' doesn't exist!");
			return;
		} else if (sender == null) {
			commandParser.writeBackToClient("ERROR: The entered sender name '" + senderName + "' doesn't exist!");
			return;
		}
		
		String toReceiver = "chatm " + argument;
		String toSender = "+OK 'message relayed'";
		
		commandParser.writeToSpecificClient(toReceiver, recipientName);
		commandParser.writeBackToClient(toSender);
	}
	
	
	/**
	 * This method checks the argument and separates the given arguments into the three arguments needed for the Chat.
	 * @return <code>false</code> when the argument isn't formatted correctly, <code>true</code> when everything's ok with the argument.
	 */
	private boolean separateArgument(String argument) {
    	char[] argumentChars = argument.toCharArray();
    	StringBuffer senderName = new StringBuffer();
    	StringBuffer recipientName = new StringBuffer();
    	StringBuffer message = new StringBuffer();
		
		int argumentNr = 1;
		
		// separates the arguments into the three arguments.
		for (int i = 0; i < argumentChars.length; i++) {
			if (argumentChars[i] == ' ' && argumentNr < 3) {
				argumentNr++;
				continue;
			}
			if (argumentNr == 1) {
				senderName.append(argumentChars[i]);
			} else if (argumentNr == 2) {
				recipientName.append(argumentChars[i]);
			}
			if (argumentNr == 3) {
				message.append(argumentChars[i]);
				continue;
			}
			
		}
		
		// Checks if argument is valid.
		if (senderName.length() == 0)
			return false;
		
		if (senderName.charAt(0) == '\'' && senderName.charAt(senderName.length() - 1) == '\'') {
			senderName.deleteCharAt(senderName.length() - 1);
			senderName.deleteCharAt(0);
		}
		
		if (recipientName.length() == 0)
			return false;
		
		if (recipientName.charAt(0) == '\'' && recipientName.charAt(recipientName.length() - 1) == '\'') {
			recipientName.deleteCharAt(recipientName.length() - 1);
			recipientName.deleteCharAt(0);
		}
		
		if (message.length() == 0)
			return false;
		
		if (message.charAt(0) == '\'' && message.charAt(message.length() - 1) == '\'') {
			message.deleteCharAt(message.length() - 1);
			message.deleteCharAt(0);
		} else {
			return false;
		}
		
		this.senderName = senderName.toString();
		this.recipientName = recipientName.toString();
		
		return true;
	}
	
	
}
