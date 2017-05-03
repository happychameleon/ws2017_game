package client.commands;

import client.Client;

import java.util.ArrayList;

/**
 * Created by flavia on 17.04.17.
 */
public class ClientCgethHandler extends ClientCommandHandler {
	/**
	 * UNUSED Executes the command from the server.
	 */
	@Override
	public void handleCommand() {
	
	}
	
	/**
	 * Extracts the different highscoreStrings with {@link #getAndRemoveNextHighscoreString()} and processes them with {@link #getHigscoreTeamList()}.
	 * Then opens a new Dialog displaying the highscores for all the games.
	 * If the Answer was negative because there haven't been any finished games, the Client gets informed about it in the MainChatWindow instead.
	 *
	 * @param isOK <code>true</code> if the answer started with "+OK", <code>false</code> otherwise (answer started with "-ERR").
	 */
	@Override
	public void handleAnswer(boolean isOK) {
		if (isOK == false && argument.startsWith("no highscores yet")) {
			Client.getMainWindow().getMainChatPanel().displayError("There are no highscores yet! Play a game first.");
			return;
		}
		
		if (isOK) {
			System.out.println("ClientCgethHandler#handleAnswer - argument: " + argument);
			
			ArrayList<ArrayList<String>> highscoreTeams = getHigscoreTeamList();
			
			new HighscoreDialog(highscoreTeams);
		}
	}
	
	/**
	 * @return An ArrayList which contains an ArrayList of Strings for all the highscore relevant Data.
	 * The inner Arraylist contains at index
	 * 0: The game name
	 * 1: The winning Team name.
	 * 2...: The users who won.
	 */
	private ArrayList<ArrayList<String>> getHigscoreTeamList() {
		ArrayList<ArrayList<String>> highscoreStrings = new ArrayList<>();
		
		while (argument.isEmpty() == false) {
			ArrayList <String> singleHigscoreString = new ArrayList<>();
			
			String highscoreString = getAndRemoveNextHighscoreString();
			
			String gameName = highscoreString.substring(0, highscoreString.indexOf(" "));
			highscoreString = highscoreString.substring(highscoreString.indexOf(" ") + 1);
			
			String winningTeam = highscoreString.substring(0, highscoreString.indexOf(" "));
			highscoreString = highscoreString.substring(highscoreString.indexOf(" ") + 1);
			
			singleHigscoreString.add(gameName);
			singleHigscoreString.add(winningTeam);
			
			while (highscoreString.isEmpty() == false) {
				String username = highscoreString.substring(0, highscoreString.indexOf(" "));
				highscoreString = highscoreString.substring(highscoreString.indexOf(" ") + 1);
				String points;
				if (highscoreString.contains(" ")) {
					points = highscoreString.substring(0, highscoreString.indexOf(" "));
					highscoreString = highscoreString.substring(highscoreString.indexOf(" ") + 1);
				} else {
					points = highscoreString; // If it's the last user.
					highscoreString = "";
				}
				String playerString = String.format("%s %s", username, points);
				singleHigscoreString.add(playerString);
			}
			highscoreStrings.add(singleHigscoreString);
		}
		return highscoreStrings;
	}
	
	/**
	 * Similar wo {@link #getAndRemoveNextArgumentWord()}.
	 * @return The next String in the Argument between two '.
	 */
	private String getAndRemoveNextHighscoreString() {
		if (argument.charAt(0) != '\'' && argument.charAt(1) != '\'') {
			System.err.println("highscoreString wrongly formated.");
			argument = "";
			return "";
		}
		int appostropheIndex = argument.indexOf("'");
		argument = argument.substring(appostropheIndex + 1);
		appostropheIndex = argument.indexOf("'");
		String highscoreString = argument.substring(0, appostropheIndex);
		argument = argument.substring(appostropheIndex + 1);
		if (argument.isEmpty() == false)
			argument = argument.substring(1);
		return highscoreString;
	}
	
	public static void sendHighscoreRequest() {
		Client.sendMessageToServer("cgeth");
	}
}
