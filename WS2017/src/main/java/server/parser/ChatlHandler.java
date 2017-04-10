package server.parser;

/**
 * Created by flavia on 06.04.17.
 */
public class ChatlHandler extends ChatmHandler {
	
	/**
	 * Executes the chatw command from the client.
	 */
	@Override
	public void handleCommand() {
		System.out.println("ChatlHandler#handleCommand");
		
		command = "chatl";
		
		handleChatMessage();
		
		
	}
	
	/**
	 * This method takes away the first word (gameName) which is not needed by the server.
	 * Then it just calls the super method because it looks the same as a chatm command.
	 */
	@Override
	protected void separateArgument() {
		// The first word is the name of the game. The server doesn't need to know this.
		String gameName = getAndRemoveNextArgumentWord();
		super.separateArgument();
	}
}
