package client;

import client.commands.ClientAnswer;
import client.commands.ClientCommandHandler;

/**
 * Gets the answer and after checking it for validity it sends the answer to the correct {@link ClientCommandHandler} (via {@link ClientAnswer}.
 *
 * Created by flavia on 26.03.17.
 */
public class ClientAnswerParser {
	
	private final ClientCommandParser commandParser;
	
	/**
	 * Whether the answer was positive or negative.
	 */
	private final boolean isOK;
	
	/**
	 * The command used this answer is intended for.
	 */
	private String keyword = "";
	
	/**
	 * The argument to the answer. Could be empty.
	 */
	private String argument = "";
	
	
	public ClientAnswerParser(String keyword, String argument, ClientCommandParser commandParser){
		switch (keyword) {
			case "+OK":
				isOK = true;
				break;
			case "-ERR":
				isOK = false;
				break;
			default:
				isOK = false;
				if (Client.getMainWindow() != null) {
					Client.getMainWindow().getMainChatPanel().displayError("Answer wrongly formatted!");
				}
				break;
		}
		separateAnswer(argument, keyword);
		this.commandParser = commandParser;
	}
	
	/**
	 * Separates the answer argument into the keyword and the actual argument.
	 */
	private void separateAnswer(String argument, String keyword) {
		if (argument.contains(" ") == false) {
			this.keyword = argument;
			this.argument = "";
			return;
		}
		if (argument.charAt(5) == ' ') {
			this.keyword = argument.substring(0, 5);
			this.argument = argument.substring(6);
		} else {
			System.err.println("ClientAnswerParser#separateAnswer - Answer formatted incorrectly: '" + keyword + " " + argument + "'");
		}
	}
	
	
	/**
	 * This compares the answer to one of the possible answers defined in the protocol.
	 */
	public void compareAnswer() {
		
		if (keyword.isEmpty())
			return;
		
		ClientAnswer answer;
		try {
			answer = Enum.valueOf(ClientAnswer.class, keyword);
		} catch (IllegalArgumentException iae) {
			if (Client.getMainWindow() != null) {
				Client.getMainWindow().getMainChatPanel().displayError("Received answer does not exist!");
			} else {
				System.err.println("Received answer does not exist!");
			}
			return;
		}
		
		answer.handleAnswer(commandParser, argument, isOK);
		
	}
	
	
}
