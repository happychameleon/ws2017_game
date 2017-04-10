package server.parser;

/**
 * Created by flavia on 05.04.17.
 */
public class ChatwHandler extends ChatmHandler {
	
	/**
	 * Executes the chatw command from the client.
	 */
	@Override
	public void handleCommand() {
		System.out.println("ChatwHandler#handleCommand");
		
		command = "chatw";
		
		handleChatMessage();
		
		
	}
	
	/**
	 * Sends the whisper message along to the receiver and also back to the sender.
	 */
	@Override
	void handleChatMessage() {
		
		String wholeArgument = argument;
		super.handleChatMessage();
		
		String message = command + " " + wholeArgument;
		commandParser.writeToSpecificClient(message, senderName);
	}
}
