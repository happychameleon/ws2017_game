package game.engine;

import client.Client;
import game.ClientGameController;
import serverclient.User;

import java.util.ArrayList;

/**
 * The Player represents the "Real Life" Person playing the game.
 *
 * Created by flavia on 02.03.17.
 */
public class Player {
	
	private final World world;
	
	/**
	 * This Player's Team.
	 */
	final Team team;
	
	/**
	 * @return {@link #team}.
	 */
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
	 * This is preferred over directly calling Player.getUser().getName() to be more flexible (e.g. if we want users to have the ability to have separate names each game).
	 * @return The name of the {@link #user}.
	 */
	public String getName() {
		return user.getName();
	}
	
	/**
	 * The color of this player.
	 */
	private PlayerColor color;
	
	/**
	 * @return {@link #color}
	 */
	public PlayerColor getColor() {
		return color;
	}
	
	
	/**
	 * Stores all the Names of the Characters this Player's Characters have 'killed'.
	 */
	private final ArrayList<String> killedCharacterNames;
	
	/**
	 * Stores all the Names of the Characters of this Player who have been 'killed'.
	 */
	private final ArrayList<String> deathCharacterNames;
	
	/**
	 * @return How many Characters were 'killed' by this Player.
	 */
	public int getKillCount() {
		return killedCharacterNames.size();
	}
	
	/**
	 * @return How many Characters from this Player have been 'killed'.
	 */
	public int getDeathCount() {
		return deathCharacterNames.size();
	}
	
	/**
	 * Called whenever this Player('s Character) kills another Team's Character.
	 * @param name The name of the killed Character.
	 */
	public void addKilledCharacter(String name) {
		killedCharacterNames.add(name);
	}
	
	/**
	 * Called whenever a Character of this Player has been killed.
	 * @param name The name of the dead Character.
	 */
	public void addDeadCharacter(String name) {
		deathCharacterNames.add(name);
	}
	
	
	/**
	 * Generates a new Player with the given value.
	 * @param team {@link #team}.
	 * @param user {@link #user}.
	 * @param color {@link #color}.
	 * @param world {@link #world}.
	 */
	public Player (Team team, User user, PlayerColor color, World world) {
		this.team = team;
		this.user = user;
		this.color = color;
		this.world = world;
		
		killedCharacterNames = new ArrayList<>();
		deathCharacterNames = new ArrayList<>();
	}
	
	/**
	 * This method is called by {@link TurnController} to inform the Player that it's their turn now.
	 * Restores all of the actionPoints of this Player's Characters.
	 * (Future): Process all effects that happen in this Player's turn.
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
