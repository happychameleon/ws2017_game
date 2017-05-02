package game.gamegui;

import game.engine.World;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
		
		
		JLabel wetness = new JLabel("Wetness: " + String.valueOf(6));
		this.add(wetness);
		JLabel weapon = new JLabel("Weapon: ");
		this.add(weapon);
		JLabel color = new JLabel("Your Color is: " + String.valueOf(world.getCurrentPlayer().getColor()));
		this.add(color);
		JLabel turn = new JLabel(String.valueOf(world.getCurrentPlayer().getName())+"'s turn");
		this.add(turn);
		
		
		
		BufferedImage weaponPic;
		try {
			weaponPic = ImageIO.read(getClass().getResource("/images/tiles/attackRangeSprite.png"));
			JLabel weaponPicLabel = new JLabel(new ImageIcon(weaponPic));
			this.add(weaponPicLabel);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		
	}
	
}
