package client.commands;

import client.Client;

import java.util.ArrayList;
import java.util.HashMap;

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
			
			ArrayList<HashMap<String, Integer>> highscoreTeams = getHigscoreTeamList();
			
			new HighscoreDialog(highscoreTeams);
		}
	}
	
	/**
	 * @return A list of HashMaps mapping each playername with their highscore. The Teamname is stored with a negative score.
	 */
	private ArrayList<HashMap<String,Integer>> getHigscoreTeamList() {
		ArrayList<HashMap<String, Integer>> highscoreTeams = new ArrayList<>();
		while (argument.isEmpty() == false) {
			String highscoreString = getAndRemoveNextHighscoreString();
			String winningTeam = highscoreString.substring(0, highscoreString.indexOf(" "));
			HashMap playerScoreMap = new HashMap();
			playerScoreMap.put(winningTeam, -1); // The winning Team name gets added with negative points to distinguish.
			highscoreTeams.add(playerScoreMap);
			highscoreString = highscoreString.substring(highscoreString.indexOf(" ") + 1);
			while (highscoreString.isEmpty() == false) {
				String username = highscoreString.substring(0, highscoreString.indexOf(" "));
				highscoreString = highscoreString.substring(highscoreString.indexOf(" ") + 1);
				int points;
				if (highscoreString.contains(" ")) {
					points = Integer.parseInt(highscoreString.substring(0, highscoreString.indexOf(" ")));
					highscoreString = highscoreString.substring(highscoreString.indexOf(" ") + 1);
				} else {
					points = Integer.parseInt(highscoreString);
					playerScoreMap.put(username, points);
					System.out.println("ClientCgethHandler#handleAnswer - highscore added: " + winningTeam + " " + username + " " + points);
					break;
				}
			}
		}
		return highscoreTeams;
	}
	
	
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
		if (argument != null && argument.isEmpty() == false)
			argument = argument.substring(1);
		return highscoreString;
	}
	
	public static void sendHighscoreRequest() {
		Client.sendMessageToServer("cgeth");
	}
}
