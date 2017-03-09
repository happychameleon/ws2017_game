package GraphicAndInput;

import Engine.Character;
import Engine.Tile;
import Engine.Weapon;
import Engine.World;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
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
	 * These Tiles represent the movement Range if a Character is selected.
	 * TODO: Add shooting Range when a Weapon/AttackType is selected and other things.
	 * Can be null!
	 */
	private Set<Tile> highlightedTiles;
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
	
	
	//region Painting
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		for (int x = 0; x < world.getMapWidth(); x++) {
			for (int y = 0; y < world.getMapHeight(); y++) {
				paintOneTile(g, world.getTileAt(x, y));
			}
		}
	}
	
	/**
	 * This method paints one Tile with all it's Pixels via {@link #paintOnePixel(Graphics, int, int, int, int, Color, Tile)}.
	 * @param g The Graphics element.
	 * @param tile The Tile to paint.
	 */
	private void paintOneTile(Graphics g, Tile tile) {
		int tileX = tile.getXPosition();
		int tileY = tile.getYPosition();
		
		// TODO: For better performance this should only be called when something changed on the Tile.
		// tile.RecalculateCurrentPixels();
		
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
	private void paintOnePixel(Graphics g, int tileX, int tileY, int pixelX, int pixelY, Color color, Tile tile) {
		int leftX = (tileX * Tile.tileSizeInPixels * pixelSize) + (pixelX * pixelSize);
		int topY = (tileY * Tile.tileSizeInPixels * pixelSize) + (pixelY * pixelSize);
		
		topY += topInset; // because of the Title Bar.

		//FIXME: This whole painting System is only for testing. Should be replaced with a solid sprite painting system.
		
		if (pixelX == 0 || pixelY == 0) {
			g.setColor(color.darker()); // To make the edges of the Tiles visible.
		} else {
			g.setColor(color);
		}
		
		if (highlightedTiles != null && highlightedTiles.contains(tile)) {
			g.setColor(g.getColor().darker().darker());
		}
		
		if (tile == world.getSelectedTile()) {
			g.setColor(g.getColor().darker().darker().darker());
		}
		
		if (tile.getCharacter() != null && (pixelX == Tile.tileSizeInPixels/2 && pixelY == Tile.tileSizeInPixels/2)) {
			g.setColor(Color.yellow);
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
						//TODO: Attack with Weapon.
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
		if (tileUnderMouse.getCharacter() != null) {
			// We have selected a Character. Highlight their movement Range.
			highlightedTiles = tileUnderMouse.getAllTilesInRange(tileUnderMouse.getCharacter().getMoveRange(), true);
			world.setSelectionType(SelectionType.CHARACTER);
		} else {
			highlightedTiles = null;
			world.setSelectionType(SelectionType.TILE);
		}
	}
	
	private void moveCharacter (Tile tileUnderMouse) {
		if (highlightedTiles != null && highlightedTiles.contains(tileUnderMouse)) {
			System.out.println("Character should be moved.");
			if (world.getSelectedTile().getCharacter().moveCharacterTo(tileUnderMouse)) {
				highlightedTiles = null;
				world.setSelectedTile(null);
			} else {
				System.out.println("Window#mouseClicked - ERROR: Why can't we move the Character?");
			}
		}
	}
	
	private void attackCharacter(Tile attackedTile) {
		System.out.println("Window::attackCharacter");
		if (attackedTile.getCharacter() != null
				&& world.getSelectedTile() != null
				&& world.getSelectedTile().getCharacter() != null
				&& world.getSelectedTile().getCharacter().getWeapon() != null) {
			Character targetedCharacter = attackedTile.getCharacter();
			Character attackingCharacter = world.getSelectedTile().getCharacter();
			System.out.println("Targeting");
			if (attackingCharacter.getAllCharactersInRange().contains(targetedCharacter)) {
				// The attacking Character can attack the targeted Character.
				targetedCharacter.changeWetness(attackingCharacter.getWeapon().getDamage());
				System.out.println(attackingCharacter + " ATTACKED " + targetedCharacter + "!");
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
				if (world.getSelectedTile() != null
						&& world.getSelectedTile().getCharacter() != null
						&& world.getSelectedTile().getCharacter().getWeapon() != null) {
					selectWeapon();
				}
				repaint();
				break;
			default:
				System.out.println("Key Typed: " + e.getKeyCode());
				break;
		}
	}
	
	private void selectWeapon() {
		world.setSelectionType(SelectionType.WEAPON);
		Weapon selectedWeapon = world.getSelectedTile().getCharacter().getWeapon();
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
