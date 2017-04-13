package game.engine;

import serverclient.User;

/**
 * The Player represents the "Real Life" Person playing the game.
 *
 * Created by flavia on 02.03.17.
 */
public class Player {
	
	private final World world;
	
	final Team team;
	
	public Team getTeam() {
		return team;
	}
	
	/**
	 * The User representing this Player.
	 */
	private final User user;
	
	/**
	 * @return {@link #user}.
	 */
	public User getUser() {
		return user;
	}
	
	/**
	 * @return The name of the {@link #user}.
	 */
	public String getName() {
		return user.getName();
	}
	
	/**
	 * The color of this player.
	 */
	private PlayerColor color;
	
	public PlayerColor getColor() {
		return color;
	}
	
	
	
	public Player (Team team, User user, PlayerColor color, World world) {
		this.team = team;
		this.user = user;
		this.color = color;
		this.world = world;
		
	}
	
	/**
	 * This method is called by {@link TurnController} to inform the Player that it's their turn now.
	 * TODO: It should restore all of the actionPoints of this Player's Characters, Process all effects that happen in this Player's turn etc.
	 */
	public void startNewTurn() {
		for (Character character : world.getAllCharacterOfOwner(this)) {
			character.resetForNewTurn();
		}
	}
	
	/**
	 * This method is called by {@link TurnController} to inform the Player that it's not their turn anymore.
	 * It is called before {@link Player#startNewTurn()} of the next Player.
	 * TODO: Does nothing atm.
	 */
	public void endCurrentTurn() {
		
	}
	
	public boolean hasTurn() {
		return world.getCurrentPlayer() == this;
	}
	
	public boolean hasCharactersLeft() {
		return world.getAllCharacterOfOwner(this).size() > 0;
	}
}
