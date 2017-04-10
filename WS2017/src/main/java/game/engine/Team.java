package game.engine;

import java.util.ArrayList;

/**
 * Represents one Team consisting of one or more Players.
 *
 * Created by flavia on 09.03.17.
 */
public class Team {
	
	private final String name;
	
	private ArrayList<Player> members;
	
	/**
	 * Adds a new Player to the team.
	 * @param player the Player to add.
	 */
	public void addPlayerToTeam(Player player) {
		members.add(player);
	}
	
	public Team(String name) {
		members = new ArrayList<>();
		
		this.name = name;
	}
	
	
}
