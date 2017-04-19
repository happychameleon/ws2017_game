package game.engine;

import java.util.ArrayList;

/**
 * Represents one Team consisting of one or more Players.
 *
 * TODO? Currently unused, maybe add possibility to play in same team. Currently there is one Team per Player.
 *
 * Created by flavia on 09.03.17.
 */
public class Team {
	
	/**
	 * The name of this team. Should not contain space characters.
	 */
	private final String name;
	
	/**
	 * @return {@link #name}.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * All the Players in this Team.
	 */
	private ArrayList<Player> members;
	
	/**
	 * @return A shallow clone of {@link #members}.
	 */
	public ArrayList<Player> getMembers() {
		return (ArrayList<Player>) members.clone();
	}
	
	/**
	 * Adds a new Player to the team.
	 * @param player the Player to add.
	 */
	public void addPlayerToTeam(Player player) {
		members.add(player);
	}
	
	/**
	 * Removes a Player from the team.
	 * @param player the Player to remove.
	 */
	public void removePlayerFromTeam(Player player) {members.remove(player);}
	
	/**
	 * Creates a new Team with the given name. If the name contains space characters they are removed.
	 * @param name {@link #name}.
	 */
	public Team(String name) {
		members = new ArrayList<>();
		
		if (name.contains(" ")) {
			name.replaceAll(" ", "");
		}
		this.name = name;
	}
	
	
}
