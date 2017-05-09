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
 * Created by flavia on 13.04.17.
 */
public class GameInfoPanel extends JPanel {

	private final World world;

	private String weaponname;
	private JLabel forinfo;
	private JLabel wetness;
	private JLabel weapon;
	private JLabel weaponPicLabel;
	private JLabel color;
	private JLabel turn;

	private BufferedImage weaponPic;

	public GameInfoPanel(World world, Window window) {

		this.world = world;

		setMinimumSize(new Dimension(15, 100));
		this.setLayout(new BorderLayout());

		forinfo = new JLabel("(Click on a Character for informations)");
		this.add(forinfo, BorderLayout.CENTER);
		color = new JLabel("Your Color is: " + String.valueOf(window.getThisUsersPlayer().getColor()));
		this.add(color, BorderLayout.SOUTH);
		turn = new JLabel(String.valueOf(world.getCurrentPlayer().getName()) + "'s turn");
		this.add(turn, BorderLayout.NORTH);

	}

	public void updatevalue() {

		this.removeAll();

		if (world.getSelectionType() == SelectionType.CHARACTER) {
			wetness = new JLabel("Wetness: " + String.valueOf(world.getSelectedTile().getCharacter().getWetness()));
			weapon = new JLabel("Weapon: " + String.valueOf(world.getSelectedTile().getCharacter().getWeapon().getName()));
			this.add(wetness, BorderLayout.LINE_START);
			this.add(weapon, BorderLayout.LINE_END);

				try {
					weaponPic = ImageIO.read(getClass().getResource("/images/weapons/"+world.getSelectedTile().getCharacter().getWeapon().getName()+ ".png"));
					weaponPicLabel = new JLabel(new ImageIcon(weaponPic));
					this.add(weaponPicLabel, BorderLayout.CENTER);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
			forinfo = new JLabel("(Click on a Character for informations)");
			this.add(forinfo, BorderLayout.CENTER);
		}
		this.add(color, BorderLayout.SOUTH);
		turn = new JLabel(String.valueOf(world.getCurrentPlayer().getName()) + "'s turn");
		this.add(turn, BorderLayout.NORTH);

		this.validate();
		this.repaint();
	}

}

