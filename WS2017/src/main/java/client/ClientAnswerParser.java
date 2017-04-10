package client;

import client.commands.Answer;

/**
 * Gets the answer and after checking it for validity it sends the answer to the correct {@link client.commands.CommandHandler} (via {@link Answer}.
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
				if (Client.getMainChatWindow() != null) {
					Client.getMainChatWindow().getMainChatPanel().displayError("Answer wrongly formatted!");
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
		
		Answer answer;
		try {
			answer = Enum.valueOf(Answer.class, keyword);
		} catch (IllegalArgumentException iae) {
			if (Client.getMainChatWindow() != null) {
				Client.getMainChatWindow().getMainChatPanel().displayError("Received answer does not exist!");
			} else {
				System.err.println("Received answer does not exist!");
			}
			return;
		}
		
		answer.handleAnswer(commandParser, argument, isOK);
		
	}
	
	
}
