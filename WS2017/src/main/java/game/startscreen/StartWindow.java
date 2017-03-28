package game.startscreen;

import game.engine.PlayerColor;

import javax.swing.*;

/**
 * This window handles the selection of the team at the start of the game.
 *
 * Created by flavia on 28.03.17.
 */
public class StartWindow extends JFrame {
	
	/**
	 * The amount of points to spend for Children and Equipment.
	 */
	private final int startingPoints;
	
	/**
	 * The Color this player was assigned to from the server. Should be displayed somewhere here.
	 */
	private final PlayerColor playerColor;
	
	/**
	 * This button adds a new child to the Team.
	 */
	JButton newChildButton = new JButton("Add Child");
	
	
	
	
	public StartWindow(int startingPoints, PlayerColor playerColor) {
		this.startingPoints = startingPoints;
		this.playerColor = playerColor;
	}
	
	
	
	
}
