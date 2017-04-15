package server.parser;

import game.ServerGameController;
import server.Server;

import java.util.HashMap;

/**
 * Created by m on 10/04/17.
 */
public class UhighHandler extends CommandHandler{
	
    @Override
    public void handleCommand() {
    
    }
	
	/**
	 * Sends a message to the Clients about the Game which has ended.
	 * @param serverGameController The Game which has ended.
	 * @param teamName The name of the winning Team.
	 * @param playerScore The highscore with playername as key and their score as the value.
	 */
	public static void sendHighscoresToPlayers(ServerGameController serverGameController, String teamName, HashMap<String, Integer> playerScore) {
		System.out.println("UhighHandler#sendHighscoresToPlayers");
		
		String gameName = serverGameController.getGameName();
		
		String playerScoreString = "";
		for (String playerName : playerScore.keySet()) {
			playerScoreString += playerName + " ";
			playerScoreString += playerScore.get(playerName) + " ";
		}
		playerScoreString = playerScoreString.substring(0, playerScoreString.length() - 1); // To remove last space
		
		Server.writeToAllClients(String.format("uhigh %s %s %s", gameName, teamName, playerScoreString));
	}
}
