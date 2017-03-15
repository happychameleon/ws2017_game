package main.Engine;

import java.util.ArrayList;

/**
 * Created by flavia on 09.03.17.
 */
public class Team {
	
	private final String name;
	
	private ArrayList<Player> members;
	
	public void addPlayerToTeam(Player player) {
		members.add(player);
	}
	
	public Team(String name) {
		members = new ArrayList<>();
		
		this.name = name;
	}
	
	
}
