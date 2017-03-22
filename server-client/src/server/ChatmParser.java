package server;



/**
 * Created by m on 3/21/17.
 */
public class ChatmParser {
    private String argument;
	
	public String getArgument() {
		return argument;
	}
	
	private String senderName;
	private String recipientName;
	private String message;
	
	public ChatmParser(String argument){
        this.argument = argument;
        if (validateArgument() == false) {
        	System.err.println("Argument for command CHATM not properly formatted! Please use format chatm <sender_name> <recipient_name> <message>");
	        // TODO: send this message back to the client.
        	return;
        }
        handleChat();
    }
	
	private boolean validateArgument() {
		// TODO
		return false;
	}
	
	private void handleChat() {
    	separateArgument();
    	
	}
	
	/**
	 * This method separates the given arguments into the three arguments needed for the Chat.
	 */
	private void separateArgument() {
    	char[] argumentChars = argument.toCharArray();
    	StringBuffer senderName = new StringBuffer();
    	StringBuffer recipientName = new StringBuffer();
    	StringBuffer message = new StringBuffer();
		
    	int argumentNr = 1;
		
		for (int i = 0; i < argumentChars.length; i++) {
			if (argumentChars[i] == ' ' && argumentNr < 3) {
				argumentNr++;
				continue;
			}
			if (argumentNr == 3) {
				message.append(argumentChars[i]);
				continue;
			}
		}
		
		if (message.charAt(0) == '\'' && message.charAt(message.length() - 1) == '\'') {
			message.deleteCharAt(message.length() - 1);
			message.deleteCharAt(0);
		} else {
			System.err.println("Message not properly formatted! validateArgument() went wrong!");
		}
	}
}
