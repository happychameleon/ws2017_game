package client.commands;

import client.Client;
import game.ClientGameController;
import game.startscreen.ClientGameStartController;
import serverclient.User;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by flavia on 02.04.17.
 */
public class ClientCgetgHandler extends CommandHandler {
	
	@Override
	public void handleCommand() {
	
	}
	
	
	@Override
	public void handleAnswer(boolean isOK) {
		if (argument.startsWith("waiting ")) {
			getAndRemoveNextArgumentWord();
			parseWaitingGameAnswer();
		} else if (argument.startsWith("running ")) {
			getAndRemoveNextArgumentWord();
			parseRunningGameAnswer();
			// TODO: Parse Running Games.
		} else {
			System.err.println("ClientCgetgHandler - argument wrongly formatted");
		}
	}
	
	private void parseRunningGameAnswer() {
		String gameName = getAndRemoveNextArgumentWord();
		HashSet<User> users = new HashSet<>();
		String nextUser = getAndRemoveNextArgumentWord();
		while (nextUser.isEmpty() == false) {
			User user = Client.getUserByName(nextUser);
			if (user != null) {
				users.add(user);
			} else {
				System.err.println("User with name '" + nextUser + "' is not registered!");
			}
			nextUser = getAndRemoveNextArgumentWord();
		}
		
		ClientGameController game = new ClientGameController(users, gameName);
		
		Client.getChatWindow().addRunningGameToList(game);
	}
	
	private void parseWaitingGameAnswer() {
		HashMap<User, String> waitingUsers = new HashMap<>();
		HashSet<User> choosingUsers = new HashSet<>();
		String gameName;
		int maxPoints;
		
		gameName = getAndRemoveNextArgumentWord();
		maxPoints = Integer.parseInt(getAndRemoveNextArgumentWord());
		
		String nextUsername = getAndRemoveNextArgumentWord();
		while (nextUsername.isEmpty() == false) {
			User user = Client.getUserByName(nextUsername);
			if (getAndRemoveNextArgumentWord().equals("ready")) {
				// User is ready and has a characterString following
				String characterString = getAndRemoveCharacterString();
				waitingUsers.put(user, characterString);
			} else {
				// User is still choosing and hasn't got a characterString yet.
				choosingUsers.add(user);
			}
			nextUsername = getAndRemoveNextArgumentWord();
		}
		
		
		ClientGameStartController game = new ClientGameStartController(waitingUsers, choosingUsers, gameName, maxPoints);
		
		Client.getChatWindow().addNewGameToList(game);
	}
	
	
	
	private String getAndRemoveCharacterString() {
		if (argument.charAt(0) != '[') {
			System.err.println("ClientCgetgHandler#getAndRemoveCharacterString - There was no characterString beginning where one should have been.");
			return null;
		}
		String characterString = argument.substring(0, argument.indexOf("]"));
		argument = argument.substring(argument.indexOf("]") + 1 + 1); // One for "]", one for " ".
		return characterString;
	}
	
}
