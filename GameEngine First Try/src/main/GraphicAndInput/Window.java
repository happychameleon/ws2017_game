package main.GraphicAndInput;

import com.sun.istack.internal.Nullable;
import main.Engine.Character;
import main.Engine.Tile;
import main.Engine.Weapon;
import main.Engine.World;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * The Window containing the general info about how to display the Game.
 *
 * Created by flavia on 04.03.17.
 */
public class Window extends JFrame implements MouseInputListener, KeyListener {
	
	//region Data
	/**
	 * The World this Window displays.
	 */
	World world;
	
	/**
	 * How big one Pixel in the Game is in Pixels on the Screen.
	 */
	private static int pixelSize = 3;
	/**
	 *
	 * @return {@link #pixelSize}.
	 */
	public static int getPixelSize() {
		return pixelSize;
	}
	
	private int topInset;
	
	/**
	 * The image on where the world is first drawn and then copied from onto the window.
	 */
	private BufferedImage image;
	
	
	private BufferedImage attackRangeSprite;
	private BufferedImage walkRangeSprite;
	private BufferedImage selectedTileSprite;
	
	private BufferedImage[] characterWetnessSprites = new BufferedImage[10];
	
	/**
	 * These Tiles represent the movement Range if a Character is selected.
	 * The keys are the Tiles, the values are the distance from the starting Tile.
	 */
	@Nullable
	private HashMap<Tile, Integer> walkRangeTiles;
	
	private Set<Tile> attackRangeTiles;
	//endregion
	
	
	public Window(World world, String title) {
		this.world = world;
		
		try {
			attackRangeSprite = ImageIO.read(getClass().getResource("/resources/images/tiles/attackRangeSprite.png"));
			walkRangeSprite = ImageIO.read(getClass().getResource("/resources/images/tiles/walkRangeSprite.png"));
			selectedTileSprite = ImageIO.read(getClass().getResource("/resources/images/tiles/selectedTileSprite.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < 10; i++) {
			try {
				characterWetnessSprites[i] = ImageIO.read(getClass().getResource("/resources/images/characters/character__topDown_wetness" + i + "0.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		pack(); // So the Insets are set correctly.
		topInset = getInsets().top;
		setSize(pixelSize * Tile.tileSizeInPixels * world.getMapWidth(), topInset + pixelSize * Tile.tileSizeInPixels * world.getMapHeight());
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle(title);
		addMouseListener(this);
		addKeyListener(this);
		setFocusable(true);
		requestFocus();
		
		setVisible(true);
		
	}
	
	//region Painting
	public void repaintImage() {
		image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = image.createGraphics();
		
		// recalculate the image.
		for (int x = 0; x < world.getMapWidth(); x++) {
			for (int y = 0; y < world.getMapHeight(); y++) {
				paintOneTile(g2d, world.getTileAt(x, y));
			}
		}
		//repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		repaintImage();
		super.paint(g);
		g.drawImage(image, 0, 0, null);
	}
	
	/**
	 * This method paints one Tile and the Character, if there is one.
	 * @param g The Graphics element.
	 * @param tile The Tile to paint.
	 */
	private void paintOneTile(Graphics2D g, Tile tile) {
		int tileX = tile.getXPosition();
		int tileY = tile.getYPosition();
		
		int leftX = (tileX * Tile.tileSizeInPixels * pixelSize);
		int topY = (tileY * Tile.tileSizeInPixels * pixelSize) + topInset;
		int tileSize = Tile.tileSizeInPixels * pixelSize;
		
		BufferedImage sprite = tile.getSprite();
		
		g.drawImage(sprite, leftX, topY, tileSize, tileSize, null);
		
		Character character = tile.getCharacter();
		if (character != null) {
			// Draw the Character on the Tile and show it's wetness.
			g.drawImage(character.getSprite(), leftX, topY, tileSize, tileSize, null);
			int imageIndex = character.getWetness()/10;
			if (imageIndex > 9)
				imageIndex = 9;
			BufferedImage wetnessSprite = characterWetnessSprites[imageIndex];
			g.drawImage(wetnessSprite, leftX, topY, tileSize, tileSize, null);
		}
		
		if (world.getSelectedTile() == tile) {
			g.drawImage(selectedTileSprite, leftX, topY, tileSize, tileSize,null);
		} else if (world.getSelectionType() == SelectionType.CHARACTER && walkRangeTiles != null && walkRangeTiles.containsKey(tile)) {
			g.drawImage(walkRangeSprite, leftX, topY, tileSize, tileSize,null);
		} else if (world.getSelectionType() == SelectionType.WEAPON && attackRangeTiles != null && attackRangeTiles.contains(tile)) {
			g.drawImage(attackRangeSprite, leftX, topY, tileSize, tileSize,null);
		}
		
	}
	//endregion
	
	
	//region Mouse
	@Override
	public void mouseClicked(MouseEvent e) {
		Tile tileUnderMouse = getTileForMouseCoordinates(e.getX(), e.getY());
		
		switch (e.getButton()) {
			case MouseEvent.BUTTON1:
				System.out.println("left mouseClicked!");
				selectTile(tileUnderMouse);
				repaint();
				break;
			case MouseEvent.BUTTON3:
				System.out.println("right mouseClicked!");
				switch (world.getSelectionType()) {
					case CHARACTER:
						// We have currently selected a Tile with a Character on it. RMB now moves the Character if possible.
						moveCharacter(tileUnderMouse);
						break;
					case WEAPON:
						attackCharacter(tileUnderMouse);
						break;
					default:
						break;
				}
				repaint();
				break;
			default:
				break;
		}
	}
	
	/**
	 * This returns the Tile which is at the given Window-Pixel-Coordinates.
	 * @param x The x coordinate of the mouse.
	 * @param y The y coordinate of the mouse.
	 * @return The Tile which is under the mouse coordinates.
	 */
	private Tile getTileForMouseCoordinates(int x, int y) {
		x /= (pixelSize * Tile.tileSizeInPixels);
		y -= topInset;
		y /= (pixelSize * Tile.tileSizeInPixels);
		
		return world.getTileAt(x, y);
	}
	
	/**
	 * This selects the given Tile.
	 * When there is a Character under the Tile it highlights their movement Range.
	 * @param tileUnderMouse The newly selected Tile.
	 */
	private void selectTile (Tile tileUnderMouse) {
		world.setSelectedTile(tileUnderMouse);
		if (tileUnderMouse.getCharacter() != null && tileUnderMouse.getCharacter().getOwner().hasTurn()) {
			// We have selected a Character. Highlight their movement Range.
			walkRangeTiles = tileUnderMouse.getAllTilesInRange(tileUnderMouse.getCharacter().getMoveRange(), true);
			world.setSelectionType(SelectionType.CHARACTER);
		} else {
			walkRangeTiles = null;
			world.setSelectionType(SelectionType.TILE);
		}
	}
	
	/**
	 * This method moves the selected Character to the given Tile, IF it is in range.
	 * @param tileUnderMouse The destination Tile.
	 */
	private void moveCharacter (Tile tileUnderMouse) {
		if (walkRangeTiles != null && walkRangeTiles.keySet().contains(tileUnderMouse)) {
			System.out.println("Character should be moved.");
			if (world.getSelectedTile().getCharacter().moveCharacterTo(tileUnderMouse, walkRangeTiles.get(tileUnderMouse))) {
				walkRangeTiles = null;
				world.setSelectedTile(null);
			} else {
				System.err.println("Window#mouseClicked - ERROR: Why can't we move the Character?");
			}
		}
	}
	
	/**
	 * This Method carries out an Attack by the selected Character against the Character standing on the given Tile
	 * @param attackedTile The Tile where the possible targeted Character stands on.
	 */
	private void attackCharacter(Tile attackedTile) {
		System.out.println("Window.attackCharacter");
		if (attackedTile.getCharacter() != null
				&& world.getSelectedTile() != null
				&& world.getSelectedTile().getCharacter() != null
				&& world.getSelectedTile().getCharacter().getWeapon() != null
				&& world.getSelectionType() == SelectionType.WEAPON) {
			Character targetedCharacter = attackedTile.getCharacter();
			Character attackingCharacter = world.getSelectedTile().getCharacter();
			System.out.println("Targeting");
			if (attackingCharacter.getAllEnemyCharactersInRange().contains(targetedCharacter)) {
				// The targeted Character is in range from the attacking Character
				if (attackingCharacter.removeActionPoints(attackingCharacter.getWeapon().getActionPointPerShot())) {
					targetedCharacter.changeWetness(attackingCharacter.getWeapon().getDamage());
					System.out.println(attackingCharacter + " ATTACKED " + targetedCharacter + "!");
				} else {
					System.out.println("Not enough actionPoints for Attack! (ATTACKER: " + attackingCharacter + "; TARGET: " + targetedCharacter + ")");
				}
			} else {
				System.out.println("Character not in Range! (ATTACKER: " + attackingCharacter + "; TARGET: " + targetedCharacter + ")");
			}
		}
	}
	
	
	@Override
	public void mousePressed(MouseEvent e) {
		
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		
	}
	//endregion
	
	//region Keyboard
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_SPACE:
				System.out.println("SPACE typed!");
				if (world.getSelectedTile() != null && world.getSelectedTile().getCharacter() != null) {
					if (World.instance.getSelectionType() == SelectionType.CHARACTER
							&& world.getSelectedTile().getCharacter().getWeapon() != null) {
						selectWeapon();
					}
				} else {
					System.err.println("Window#keyPressed - ERROR: world.getSelectionType() == CHARACTER, but there is no selected Tile or Character on the selected Tile");
				}
				repaint();
				break;
			case KeyEvent.VK_ENTER:
				world.setSelectionType(SelectionType.NOTHING);
				world.endTurn();
				if (world.getCurrentPlayer().hasCharactersLeft() == false) {
					world.endTurn();
				}
				repaint();
				break;
			default:
				System.out.println("Key Typed: " + e.getKeyCode());
				break;
		}
	}
	
	/**
	 * This method selects the Weapon carried by the selected Character. The selected Character has to carry a weapon!
	 * TODO: Cycle between Weapons if the Character has multiple Weapons.
	 */
	private void selectWeapon() {
		world.setSelectionType(SelectionType.WEAPON);
		Weapon selectedWeapon = world.getSelectedTile().getCharacter().getWeapon();
		if (selectedWeapon == null) {
			System.err.println("Window#selectWeapon - ERROR: selected Character carries no Weapon!");
		}
		attackRangeTiles = world.getSelectedTile().getAllTilesInRange(selectedWeapon.getRange(), false).keySet();
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	//endregion
	
	
}
