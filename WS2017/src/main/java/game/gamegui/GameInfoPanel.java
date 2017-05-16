package game.gamegui;

//import game.engine.Player;
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
	
	private final World world;
	private final Window window;
	
	private JLabel forinfo1;
	private JLabel forinfo2;
	private JLabel wetness;
	private JLabel weapon;
	private JLabel weaponPicLabel;
	private JLabel turn;

	private JLabel actionPoints;
	private Color color;
	private BufferedImage weaponPic;
	
	public GameInfoPanel(World world, Window window) {
		
		this.world = world;
		this.window = window;
		
		setMinimumSize(new Dimension(15, 100));
		this.setLayout(new BorderLayout());
        
		forinfo1 = new JLabel(" ");
		forinfo2 = new JLabel("(Click on a Character for informations)");
		this.add(forinfo1,BorderLayout.CENTER);
		this.add(forinfo2,BorderLayout.SOUTH);
        
		if (window.getThisUsersPlayer() != null) { // If the user is only watching this would otherwise get a NullPointerException because window.getThisUsersPlayer() would then return null.
			color =  window.getThisUsersPlayer().getColor().getAWTColor();
			System.out.println(color);
			this.setBackground(color);
		}
		turn = new JLabel(String.valueOf(world.getCurrentPlayer().getName()) + "'s turn");
		this.add(turn, BorderLayout.NORTH);

	}
	
	/**
	 * TODO Write Javadoc!
	 */
	public void updatevalue() {
		
		this.removeAll();
		
		// TODO: Add either key description, which Key does what or buttons for the actions (or both).
		// TODO: Actions to add: Selecting Character (LMB), Walking (RMB), Selecting Weapon (Space), Shooting (RMB), End Turn (Enter).
		
		if (world.getSelectionType() == SelectionType.CHARACTER) {
			wetness = new JLabel("Wetness: " + String.valueOf(world.getSelectedTile().getCharacter().getWetness()));
			weapon = new JLabel("Weapon: " + String.valueOf(world.getSelectedTile().getCharacter().getWeapon().getName()));
			actionPoints = new JLabel("Action Points: " + String.valueOf(world.getSelectedTile().getCharacter().getActionPoints()));
			this.add(wetness, BorderLayout.LINE_START);
			this.add(weapon, BorderLayout.LINE_END);

			this.add(actionPoints, BorderLayout.SOUTH);

				try {
					weaponPic = ImageIO.read(getClass().getResource("/images/weapons/"+world.getSelectedTile().getCharacter().getWeapon().getName()+ ".png"));
					weaponPicLabel = new JLabel(new ImageIcon(weaponPic));
					this.add(weaponPicLabel, BorderLayout.CENTER);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
			forinfo2 = new JLabel("(Click on a Character for informations)");
			this.add(forinfo2, BorderLayout.CENTER);

		}
		if (window.getThisUsersPlayer() != null) {
			this.setBackground(color);
		}
		turn = new JLabel(String.valueOf(world.getCurrentPlayer().getName()) + "'s turn");
		this.add(turn, BorderLayout.NORTH);
		
		this.validate();
		this.repaint();
	}
	
}

