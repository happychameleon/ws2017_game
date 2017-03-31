package server.parser;

/**
 * Created by flavia on 31.03.17.
 */
public class ReadyHandler extends CommandHandler {
	
	@Override
	public void handleCommand(String argument) {
		String username = getFirstWord(argument);
		String characterString = getCharacterString(argument);
		
	}
	
	private String getFirstWord(String argument) {
		int index = argument.indexOf(" ");
		return argument.substring(0, index);
	}
	
	private String getCharacterString(String argument) {
		int index = argument.indexOf("[");
		return argument.substring(index);
	}
	
}
