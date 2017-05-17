package game.gamegui;

import game.engine.World;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * This is used below the main game panel to display info about the selected
 * Tile and the game in general.
 *
 * Created by patrick on 13.04.17.
 */
public class GameInfoPanel extends JPanel {
	
	/**
	 * The world of this panel.
	 */
	private final World world;
	
	/**
	 * The window the panel is in.
	 */
	private final Window window;
	
	/**
	 * This label indicate that no character was chosen.
	 */
	private JLabel forinfo;
	
	private JLabel wetness;
	private JLabel weapon;
	private JLabel weaponPicLabel;
	private JLabel turn;
	private JLabel actionPoints;
	
	/**
	 * The user's color.
	 */
	private Color color;
	
	private BufferedImage weaponPic;
	
	/**
	 * Creates a new <code>JPanel</code> with a grid layout.
	 * @param world of this panel.
	 * @param window The window this is in.
	 */
	public GameInfoPanel(World world, Window window) {

		this.world = world;
		this.window = window;
		
		setMinimumSize(new Dimension(15, 100));
		this.setLayout(new GridLayout(4,2));
		
		// If the user is only watching this would otherwise get a NullPointerException because window.getThisUsersPlayer() would then return null.
		if (window.getThisUsersPlayer() != null) { 
			color =  window.getThisUsersPlayer().getColor().getAWTColor();
			this.setBackground(color);
		}
		turn = new JLabel(String.valueOf(world.getCurrentPlayer().getName()) + "'s turn");
		forinfo = new JLabel("(Click on a Character for informations)");
		this.add(forinfo);
		this.add(turn);

	}
	
	/**
	 * This method update the values of
	 * the panel if another Tile is selected.
	 */
	public void updatevalue() {
		
		// TODO: Add either key description, which Key does what or buttons for the actions (or both).
		// TODO: Actions to add: Selecting Character (LMB), Walking (RMB), Selecting Weapon (Space), Shooting (RMB), End Turn (Enter).
		
		// Remove the old values and labels.
		this.removeAll();
		
		// If the tile selected  contains no character this would otherwise get a NullPointerException because world.getSelectedTile().getCharacter() would then return null.
		if (world.getSelectionType() == SelectionType.CHARACTER) { 
			wetness = new JLabel("Wetness: " + String.valueOf(world.getSelectedTile().getCharacter().getWetness()));
			weapon = new JLabel("Weapon: " + String.valueOf(world.getSelectedTile().getCharacter().getWeapon().getName()));
			actionPoints = new JLabel("Action Points: " + String.valueOf(world.getSelectedTile().getCharacter().getActionPoints()));
			this.add(wetness);
			this.add(weapon);
			this.add(actionPoints);

				try {
					weaponPic = ImageIO.read(getClass().getResource("/images/weapons/"+world.getSelectedTile().getCharacter().getWeapon().getName()+ ".png"));
					weaponPicLabel = new JLabel(new ImageIcon(weaponPic));
					weaponPicLabel.setOpaque(true);
					this.add(weaponPicLabel);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
			forinfo = new JLabel("(Click on a Character for informations)");
			this.add(forinfo);
		}
		if (window.getThisUsersPlayer() != null) {
			this.setBackground(color);
		}
		turn = new JLabel(String.valueOf(world.getCurrentPlayer().getName()) + "'s turn");
		this.add(turn);
		
		// display the modifications.
		this.validate();
		this.repaint();
	}

}

