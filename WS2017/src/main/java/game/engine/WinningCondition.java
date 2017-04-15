package game.engine;

import java.util.ArrayList;

/**
 * These are all the winning conditions. It checks whether a Team has won or the game should still be played.
 * TODO? (optionally) They can be chosen at the start of the game.
 * It uses the correct Check to determine whether a Team has won and if so returns the Team.
 *
 * Created by flavia on 15.04.17.
 */
public enum WinningCondition {
	
	LAST_TEAM_STANDING("A Team has won when it is the last Team with Characters left.");
	
	
	
	/**
	 * Creates the Winning Condition.
	 * @param description {@link #description}.
	 */
	WinningCondition(String description) {
		this.description = description;
	}
	
	
	
	/**
	 * A description of this winning condition
	 */
	String description;
	
	/**
	 * @return {@link #description}.
	 */
	public String getDescription() {
		return description;
	}
	
	
	/**
	 * Uses the correct method to determine whether a Team has won and if so which one.
	 * @param world The world of which we have to check the winning condition.
	 * @return The winning Team, if a Team has won, otherwise false;
	 */
	public Team checkForWinningCondition(World world) {
		switch (this) {
			case LAST_TEAM_STANDING:
				return lastTeamStanding(world);
			default:
				return null;
		}
	}
	
	//region Winning Condition Check Methods
	/**
	 * A Team has won when it is the last Team with Characters left.
	 * @param world The world of which we have to check the winning condition.
	 * @return The winning Team, if a Team has won, otherwise false;
	 */
	private Team lastTeamStanding(World world) {
		System.out.println("WinningCondition#lastTeamStanding");
		ArrayList<Team> teamsWithCharactersLeft = new ArrayList<>();
		for (Player player : world.getTurnController().getPlayers()) {
			if (player.hasCharactersLeft()) {
				teamsWithCharactersLeft.add(player.getTeam());
			}
		}
		if (teamsWithCharactersLeft.size() < 1) {
			System.err.println("WinningCondition#lastTeamStanding - There is no Team with Characters left!?");
			return null;
		}
		if (teamsWithCharactersLeft.size() == 1) {
			return teamsWithCharactersLeft.get(0); // There is only one Team left. It has won!
		}
		return null;
	}
	//endregion
	
	/**
	 * @return {@link #description}.
	 */
	@Override
	public String toString() {
		return description;
	}
}
