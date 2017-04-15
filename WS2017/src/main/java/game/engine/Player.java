package game.engine;

import client.Client;
import game.ClientGameController;
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
	 * UNUSED at the moment, since there's nothing to do yet.
	 */
	public void endCurrentTurn() {
		
	}
	
	/**
	 * Whether this user has the current turn or not.
	 * @return true if this user has the current turn, otherwise false.
	 */
	public boolean hasTurn() {
		return world.getCurrentPlayer() == this;
	}
	
	/**
	 * Whether this user still has characters on the map.
	 * @return true if this user has characters, false if all characters have been 'killed'.
	 */
	public boolean hasCharactersLeft() {
		return world.getAllCharacterOfOwner(this).size() > 0;
	}
	
	/**
	 * Whether this player represents this Client. Should only be called on the Client.
	 * @return true if {@link #user} == {@link Client#getThisUser}, otherwise false. Always false when called on the server.
	 */
	public boolean isThisClient() {
		if (world.getGameController() instanceof ClientGameController) {
			return Client.getThisUser() == this.user;
		} else {
			System.err.println("Player#isThisClient - The Server shold never call this method!");
			return false;
		}
	}
}
