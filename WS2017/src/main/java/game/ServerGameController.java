package game;

import game.engine.CreateHighscoreXML;
import game.engine.Player;
import game.engine.Team;
import game.engine.World;
import server.Server;
import server.parser.LeavgHandler;
import server.parser.UhighHandler;
import serverclient.User;

import java.io.*;
import java.util.HashMap;

/**
 * Created by flavia on 11.04.17.
 */
public class ServerGameController extends GameController {
	
	
	private static File highscoreFile = new File("./highscore.txt");
	
	
	
	/**
	 * Creates the Game Controller in the given state.
	 * @param gameState      {@link #gameState}.
	 * @param startingPoints {@link #startingPoints}.
	 * @param gameName       {@link #gameName}.
	 * @param users          {@link #users}.
	 * @param map            {@link #gameMap}
	 */
	public ServerGameController(GameState gameState, int startingPoints, String gameName, HashMap<User, String> users, GameMap map) {
		super(gameState, startingPoints, gameName, users, map);
	}
	
	
	
	/**
	 * Removes the user from this game on the server.
	 * Informs the clients about the user leaving.
	 * If the game now is empty, it is removed and the clients are informed.
	 */
	@Override
	public void removeUser(User user) {
		LeavgHandler.writeLeavgToClients(gameName, user.getName());
		
		super.removeUser(user);
		
		if (getAllUsers().isEmpty()) {
			Server.removeGame(this);
			Server.writeToAllClients(String.format("rmgam %s", gameName));
		}
	}
	
	
	/**
	 * Starts the game on the server. Called by a Client with the stgam command.
	 */
	@Override
	public void startGame() {
		super.startGame();
		
		world = new World(gameMap, this);
	}
	
	
	/**
	 * Called by {@link World#checkWinningCondition} when there is a Team which has won.
	 * Calculates the Points of the Team members and tells the Clients.
	 * Currently the points are just the KillCount. Could be changed.
	 * @param winningTeam The Team which has won the Game.
	 */
	public void teamHasWon(Team winningTeam) {
		System.out.println("ServerGameController#teamHasWon");
		
		HashMap<String, Integer> playerScore = new HashMap<>();
		for (Player player : winningTeam.getMembers()) {
			int score = player.getKillCount();
			playerScore.put(player.getName(), score);
		}
		UhighHandler.sendHighscoresToPlayers(this, winningTeam.getName(), playerScore);
		endGame(playerScore, winningTeam.getName());
	}
	
	/**
	 * Ends the game and writes the highscore into a file.
	 * @param playerScore Stores the player's names with their score.
	 * @param winningTeamName The name of the winning Team.
	 */
	@Override
	public void endGame(HashMap<String, Integer> playerScore, String winningTeamName) {
		super.endGame(playerScore, winningTeamName);
		
		// Write the highscore into a file.
		CreateHighscoreXML.createHighscoreXML(playerScore, winningTeamName);
		
		/*
		try {
			if (highscoreFile.exists() == false) {
				highscoreFile.createNewFile();
				System.out.println("ServerGameController#endGame - new highscoreFile created");
			}
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(highscoreFile, true));
			
			
			bufferedWriter.write(winningTeamName + " ");
			for (String playerName : playerScore.keySet()) {
				bufferedWriter.write(String.format("%s %d ", playerName, playerScore.get(playerName)));
			}
			bufferedWriter.newLine();
			bufferedWriter.flush();
			bufferedWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		
	}
	
}
