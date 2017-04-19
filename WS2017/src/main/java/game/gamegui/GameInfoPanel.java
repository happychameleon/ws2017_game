package game.gamegui;

import game.engine.World;

import javax.swing.*;
import java.awt.*;

/**
 * This is used below the main game panel to display info about the selected Tile and the game in general.
 *
 * Created by flavia on 13.04.17.
 */
public class GameInfoPanel extends JPanel {
	
	private final World world;
	
	public GameInfoPanel(World world, Window window) {
		this.world = world;
		
		setMinimumSize(new Dimension(15, 100));
		
	}
}
