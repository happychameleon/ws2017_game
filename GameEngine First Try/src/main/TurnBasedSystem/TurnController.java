package main.TurnBasedSystem;

import main.Engine.Player;
import main.Engine.Team;

import java.util.ArrayList;

/**
 * Created by flavia on 09.03.17.
 */
public class TurnController {
	
	//region Data
	/**
	 * All the players Playing the game. The order of the Players in here represents the turn order
	 * (players[0] is first, then players[1] etc.).
	 * @see #getCurrentPlayer()
	 */
	private final ArrayList<Player> players;
	
	public ArrayList<Player> getPlayers() {
		return players;
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
	
	public void endTurn () {
		getCurrentPlayer().endCurrentTurn();
		currentPlayerIndex++;
		if (players.size() <= currentPlayerIndex) { // == should also work, but just to be sure.
			currentPlayerIndex = 0;
		}
		System.out.println(getCurrentPlayer().getName() + " started their turn!");
		getCurrentPlayer().startNewTurn();
	}
	//endregion
	
	
	public TurnController (int playerCount) {
		if (playerCount > 4) {
			playerCount = 4;
			System.out.println("Maximum 4 Players!");
		}
		
		players = new ArrayList<>(playerCount);
		
		// TODO: Add the real Players instead of just generic ones.
		for (int i = 0; i < playerCount; i++) {
			Team newTeam = new Team("Team " + (i + 1));
			Player newPlayer = new Player(newTeam, "Player " + (i + 1));
			players.add(newPlayer);
			newTeam.addPlayerToTeam(newPlayer);
		}
	}
	
	
}
