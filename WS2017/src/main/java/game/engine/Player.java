package game.engine;

import java.util.ArrayList;

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
	
	final String name;
	
	public String getName() {
		return name;
	}
	
	private String color;
	
	public String getColor() {
		System.out.println("Color: " + color);
		return color;
	}
	
	public void setAColor() {
		if (availableColors.size() == 0) {
			System.out.println("Player::setAColor - ERROR: Not enough Player Colors available!");
			return;
		}
		color = availableColors.remove(0);
	}
	
	// TODO: Replace with PlayerColor!
	private static ArrayList<String> availableColors;
	
	
	
	public Player (Team team, String name, World world) {
		this.team = team;
		this.name = name;
		this.world = world;
		
		if (availableColors == null) {
			availableColors = new ArrayList<>();
			availableColors.add("yellow"); availableColors.add("red"); availableColors.add("orange"); availableColors.add("pink");
		}
		setAColor();
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
