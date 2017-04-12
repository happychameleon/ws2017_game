package game.engine;

import serverclient.User;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by flavia on 09.03.17.
 */
public class TurnController {
	
	//region Data
	private final World world;
	
	/**
	 * All the players Playing the game. The order of the Players in here represents the turn order
	 * (players.get(0) is first, then players.get(1) etc.).
	 * @see #getCurrentPlayer()
	 */
	private final ArrayList<Player> players;
	
	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	/**
	 * The total number of played turns.
	 */
	private int turnCount = 1;
	
	/**
	 * @return {@link #turnCount}
	 */
	public int getTurnCount() {
		return turnCount;
	}
	
	/**
	 * The index of the Player from {@link #players} which's turn it is.
	 */
	private int currentPlayerIndex = 0;
	
	/**
	 * This returns the Player from {@link #players} at index {@link #currentPlayerIndex}.
	 * @return The player which's turn it is.
	 */
	public Player getCurrentPlayer() {
		return players.get(currentPlayerIndex);
	}
	//endregion
	
	
	public TurnController(Set<User> userSet, World world) {
		this.world = world;
		
		User[] users = new User[userSet.size()];
		int j = 0;
		for (User user : userSet) {
			users[j] = user;
			j++;
		}
		
		if (users.length > 4) {
			System.err.println("TurnController#TurnController - There should never be more than 4 Players!");
		}
		
		players = new ArrayList<>();
		for (int i = 0; i < users.length; i++) {
			User user = users[i];
			Team newTeam = new Team("Team " + (players.size() + 1));
			PlayerColor color = PlayerColor.values()[i];
			Player newPlayer = new Player(newTeam, user, color, world);
			players.add(newPlayer);
			newTeam.addPlayerToTeam(newPlayer);
		}
	}
	
	
	public void endTurn () {
		getCurrentPlayer().endCurrentTurn();
		currentPlayerIndex++;
		if (players.size() <= currentPlayerIndex) { // == should also work, but just to be sure.
			turnCount++;
			currentPlayerIndex = 0;
		}
		System.out.println(getCurrentPlayer().getName() + " started their turn!");
		getCurrentPlayer().startNewTurn();
	}
	
	
}
