package main.GraphicAndInput;

import com.sun.istack.internal.Nullable;
import main.Engine.Character;
import main.Engine.Tile;
import main.Engine.Weapon;
import main.Engine.World;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;

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
	
	/**
	 * These Tiles represent the movement Range if a Character is selected.
	 * The keys are the Tiles, the values are the distance from the starting Tile.
	 * TODO: Move this to the World? Or just Selection and highlightedTiles to the same place.
	 */
	@Nullable
	private HashMap<Tile, Integer> highlightedTiles;
	//endregion
	
	
	public Window(World world, String title) {
		this.world = world;
		
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
	
	public void repaintImage() {
		image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = image.createGraphics();
		
		// recalculate the image.
		for (int x = 0; x < world.getMapWidth(); x++) {
			for (int y = 0; y < world.getMapHeight(); y++) {
				paintOneTile(g2d, world.getTileAt(x, y));
			}
		}
		
		//System.out.println("repaintImage");
	}
	
	
	//region Painting
	@Override
	public void paint(Graphics g) {
		repaintImage();
		super.paint(g);
		g.drawImage(image, 0, 0, null);
	}
	
	/**
	 * This method paints one Tile with all it's Pixels via {@link #paintOnePixel}.
	 * @param g The Graphics element.
	 * @param tile The Tile to paint.
	 */
	private void paintOneTile(Graphics2D g, Tile tile) {
		int tileX = tile.getXPosition();
		int tileY = tile.getYPosition();
		
		for (int x = 0; x < Tile.tileSizeInPixels; x++) {
			for (int y = 0; y < Tile.tileSizeInPixels; y++) {
				paintOnePixel(g, tileX, tileY, x, y, tile.getCurrentPixelAt(x, y), tile);
			}
		}
	}
	
	/**
	 * This method paints one single square representing a Pixel in the game.
	 * @param g The Graphics element.
	 * @param tileX The x-position of the Tile in the World.
	 * @param tileY The y-position of the Tile in the World.
	 * @param pixelX The x-position of the Pixel in the Tile.
	 * @param pixelY The y-position of the Pixel in the Tile.
	 * @param color The Color of this Pixel.
	 */
	private void paintOnePixel(Graphics2D g, int tileX, int tileY, int pixelX, int pixelY, Color color, Tile tile) {
		int leftX = (tileX * Tile.tileSizeInPixels * pixelSize) + (pixelX * pixelSize);
		int topY = (tileY * Tile.tileSizeInPixels * pixelSize) + (pixelY * pixelSize);
		
		topY += topInset; // because of the Title Bar.

		//FIXME: This whole painting System is only for testing. Should be replaced with a solid sprite painting system.
		
		if (pixelX == 0 || pixelY == 0) {
			g.setColor(color.darker()); // To make the edges of the Tiles visible.
		} else {
			g.setColor(color);
		}
		
		// Paint the highlighted Tiles
		if (highlightedTiles != null && highlightedTiles.keySet().contains(tile)) {
			g.setColor(g.getColor().darker().darker());
		}
		
		// Paint the selected Tile
		if (tile == world.getSelectedTile()) {
			g.setColor(g.getColor().darker().darker().darker());
		}
		
		// Paint the Character
		if (tile.getCharacter() != null && (pixelX == Tile.tileSizeInPixels/2 && pixelY == Tile.tileSizeInPixels/2)) {
			g.setColor(tile.getCharacter().getOwner().getColor());
		}
		
		g.fillRect(leftX, topY, pixelSize, pixelSize);
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
			highlightedTiles = tileUnderMouse.getAllTilesInRange(tileUnderMouse.getCharacter().getMoveRange(), true);
			world.setSelectionType(SelectionType.CHARACTER);
		} else {
			highlightedTiles = null;
			world.setSelectionType(SelectionType.TILE);
		}
	}
	
	/**
	 * This method moves the selected Character to the given Tile, IF it is in range.
	 * @param tileUnderMouse The destination Tile.
	 */
	private void moveCharacter (Tile tileUnderMouse) {
		if (highlightedTiles != null && highlightedTiles.keySet().contains(tileUnderMouse)) {
			System.out.println("Character should be moved.");
			if (world.getSelectedTile().getCharacter().moveCharacterTo(tileUnderMouse, highlightedTiles.get(tileUnderMouse))) {
				highlightedTiles = null;
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
				world.endTurn();
				break;
			default:
				System.out.println("Key Typed: " + e.getKeyCode());
				break;
		}
	}
	
	/**
	 * This method selects the Weapon carried by the selected Player. The selected Player has to carry a weapon!
	 */
	private void selectWeapon() {
		world.setSelectionType(SelectionType.WEAPON);
		Weapon selectedWeapon = world.getSelectedTile().getCharacter().getWeapon();
		if (selectedWeapon == null) {
			System.err.println("Window#selectWeapon - ERROR: selected Character carries no Weapon!");
		}
		highlightedTiles = world.getSelectedTile().getAllTilesInRange(selectedWeapon.getRange(), false);
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	//endregion
	
	
}
