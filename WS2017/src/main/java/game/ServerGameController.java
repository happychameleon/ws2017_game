package game;

import game.engine.Player;
import game.engine.Team;
import game.engine.World;
import server.Server;
import server.parser.UhighHandler;
import serverclient.User;

import java.util.HashMap;

/**
 * Created by flavia on 11.04.17.
 */
public class ServerGameController extends GameController {
	
	
	/**
	 * Creates the Game Controller in the given state.
	 * @param gameState      {@link #gameState}.
	 * @param startingPoints {@link #startingPoints}.
	 * @param gameName       {@link #gameName}.
	 * @param users          {@link #users}.
	 * @param map
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
		Server.writeToAllClients(String.format("leavg %s %s", gameName, user.getName()));
		
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
	 * @param playerScore
	 * @param winningTeamName
	 */
	@Override
	public void endGame(HashMap<String, Integer> playerScore, String winningTeamName) {
		super.endGame(playerScore, winningTeamName);

		// TODO: writes the highscore into a file.
		
		Server.removeGame(this);
	}
}
