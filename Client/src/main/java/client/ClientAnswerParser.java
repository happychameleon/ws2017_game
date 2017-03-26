package client;

import client.commands.Answer;

/**
 * Created by flavia on 26.03.17.
 */
public class ClientAnswerParser {
	private final ClientCommandParser commandParser;
	private final boolean isOK;
	private String keyword = "";
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
				if (Client.getChatWindow() != null) {
					Client.getChatWindow().displayError("Answer wrongly formatted!");
				}
				break;
		}
		separateAnswer(argument, keyword);
		this.commandParser = commandParser;
	}
	
	/**
	 * Separates the answer argument into the keyword and the actual argument.
	 * @param argument
	 * @param keyword
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
			if (Client.getChatWindow() != null) {
				Client.getChatWindow().displayError("Answer formatted incorrectly: '" + keyword + " " + argument + "'");
			}
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
			if (Client.getChatWindow() != null) {
				Client.getChatWindow().displayError("Received answer does not exist!");
			} else {
				System.err.println("Received answer does not exist!");
			}
			return;
		}
		
		answer.handleAnswer(commandParser, argument, isOK);
		
	}
	
	
}
