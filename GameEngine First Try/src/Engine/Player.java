package Engine;

import java.time.temporal.TemporalAmount;

/**
 * The Engine.Player represents the "Real Life" Person playing the game.
 *
 * Created by flavia on 02.03.17.
 */
public class Player {
	
	final Team team;
	
	public Team getTeam() {
	
		return team;
	}
	
	public Player (Team team) {
		this.team = team;
	}
	
	public Player () {
		this.team = new Team();
	}

}
