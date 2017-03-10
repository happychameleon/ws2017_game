package Engine;

import java.awt.*;
import java.util.ArrayList;

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
	
	final String name;
	
	public String getName() {
		return name;
	}
	
	private Color color;
	
	public Color getColor() {
		return color;
	}
	
	public void setAColor() {
		if (availableColors.size() == 0) {
			System.out.println("Player::setAColor - ERROR: Not enough Player Colors available!");
			return;
		}
		color = availableColors.remove(0);
	}
	
	private static ArrayList<Color> availableColors;
	
	
	
	public Player (Team team, String name) {
		this.team = team;
		this.name = name;
		
		if (availableColors == null) {
			availableColors = new ArrayList<>();
			availableColors.add(Color.yellow); availableColors.add(Color.magenta); availableColors.add(Color.orange); availableColors.add(Color.pink);
		}
		setAColor();
	}
	
	/**
	 * This method is called by {@link TurnBasedSystem.TurnController} to inform the Player that it's their turn now.
	 * TODO: It should restore all of the actionPoints of this Player's Characters, Process all effects that happen in this Player's turn etc.
	 */
	public void startNewTurn() {
		
	}
	
	/**
	 * This method is called by {@link TurnBasedSystem.TurnController} to inform the Player that it's not their turn anymore.
	 * It is called before {@link Player#startNewTurn()} of the next Player.
	 * TODO: Does nothing atm.
	 */
	public void endCurrentTurn() {
		
	}
	
	
	public boolean hasTurn() {
		return World.instance.getCurrentPlayer() == this;
	}
}
