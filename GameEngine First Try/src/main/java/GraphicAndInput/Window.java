package GraphicAndInput;

import Engine.Character;
import Engine.Tile;
import Engine.Weapon;
import Engine.World;

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
	
	private Camera camera;
	
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
	
	public int getMapWidthInPixels() {
		return pixelSize * Tile.tileSizeInPixels * world.getMapWidth();
	}
	
	public int getMapHeightInPixels() {
		return pixelSize * Tile.tileSizeInPixels * world.getMapHeight();
	}
	
	public int getHeightWithoutTitlebar() {
		return getHeight() - topInset;
	}
	
	/**
	 * The previewImage on where the world is first drawn and then copied from onto the window.
	 */
	private BufferedImage previewImage;
	
	
	private BufferedImage attackRangeSprite;
	private BufferedImage walkRangeSprite;
	private BufferedImage selectedTileSprite;
	
	private BufferedImage[] characterWetnessSprites = new BufferedImage[10];
	
	/**
	 * These Tiles represent the movement Range if a Character is selected.
	 * The keys are the Tiles, the values are the distance from the starting Tile.
	 */
	private HashMap<Tile, Integer> walkRangeTiles;
	
	private Set<Tile> attackRangeTiles;
	//endregion
	
	
	public Window(World world, String title) {
		this.world = world;
		
		this.camera = new Camera(this);
		
		try {
			attackRangeSprite = ImageIO.read(getClass().getResource("/images/tiles/attackRangeSprite.png"));
			walkRangeSprite = ImageIO.read(getClass().getResource("/images/tiles/walkRangeSprite.png"));
			selectedTileSprite = ImageIO.read(getClass().getResource("/images/tiles/selectedTileSprite.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < 10; i++) {
			try {
				characterWetnessSprites[i] = ImageIO.read(getClass().getResource("/images/characters/character__topDown_wetness" + i + "0.png"));
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
		
		repaintImage();
		
	}
	
	//region Painting
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(previewImage, -((int) camera.getXPosition()), -((int) camera.getYPosition()) + topInset, null);
	}
	
	/**
	 * Recalculates the {@link #previewImage}.
	 */
	public void repaintImage() {
		if (previewImage == null) {
			previewImage = new BufferedImage(getMapWidthInPixels(), getMapHeightInPixels(), BufferedImage.TYPE_INT_ARGB);
			for (int x = 0; x < world.getMapWidth(); x++) {
				for (int y = 0; y < world.getMapHeight(); y++) {
					// Since we had to create a new Image, we have to draw all the Tiles.
					world.getTileAt(x, y).setNeedsGraphicsUpdate();
				}
			}
		}
		Graphics2D g2d = previewImage.createGraphics();
		
		// recalculate the previewImage.
		for (int x = 0; x < world.getMapWidth(); x++) {
			for (int y = 0; y < world.getMapHeight(); y++) {
				Tile tile = world.getTileAt(x, y);
				if (tile.needsGraphicsUpdate()) {
					paintOneTile(g2d, tile); // Update this Tile, if something has changed on it.
					tile.setNeedsGraphicsUpdate(false);
				}
			}
		}
		
		previewImage = toCompatibleImage(previewImage);
		
		repaint();
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
		int topY = (tileY * Tile.tileSizeInPixels * pixelSize);
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
	
	/**
	 * This sets all the currently selected Tiles to be redrawn (so they can be unselected)
	 */
	private void updateGraphicsForUnselection() {
		if (world.getSelectedTile() != null) {
			world.getSelectedTile().setNeedsGraphicsUpdate();
		}
		if (walkRangeTiles != null && walkRangeTiles.keySet() != null) {
			for (Tile tile : walkRangeTiles.keySet()) {
				tile.setNeedsGraphicsUpdate();
			}
		}
		if (attackRangeTiles != null) {
			for (Tile tile : attackRangeTiles) {
				tile.setNeedsGraphicsUpdate();
			}
		}
	}
	
	/**
	 * This returns a copy of the given image which is much(!) faster to draw than the original image.
	 * Code copied from <a href="http://stackoverflow.com/questions/196890/java2d-performance-issues">Stackoverflow</a>.
	 * @param image The original image.
	 * @return A faster-to-draw copy of the original image.
	 */
	private BufferedImage toCompatibleImage(BufferedImage image) {
		// obtain the current system graphical settings
		GraphicsConfiguration gfx_config = GraphicsEnvironment.
				getLocalGraphicsEnvironment().getDefaultScreenDevice().
				getDefaultConfiguration();
		
		
        // if image is already compatible and optimized for current system settings, simply return it
		if (image.getColorModel().equals(gfx_config.getColorModel()))
			return image;
		
		// image is not optimized, so create a new image that is
		BufferedImage new_image = gfx_config.createCompatibleImage(image.getWidth(), image.getHeight(), image.getTransparency());
		
		// get the graphics context of the new image to draw the old image on
		Graphics2D g2d = (Graphics2D) new_image.getGraphics();
		
		// actually draw the image and dispose of context no longer needed
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();
		
		// return the new optimized image
		return new_image;
	}
	//endregion
	
	
	//region Mouse
	@Override
	public void mouseClicked(MouseEvent e) {
		Tile tileUnderMouse = getTileForMouseCoordinates(e.getX(), e.getY());
		
		updateGraphicsForUnselection();
		
		switch (e.getButton()) {
			case MouseEvent.BUTTON1:
				System.out.println("left mouseClicked!");
				selectTile(tileUnderMouse);
				repaintImage();
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
				repaintImage();
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
		// TODO: fix bug where on the y-axis the tile is chosen with a slight offset (about 3 screen-pixels)
		x += (int) camera.getXPosition();
		y += (int) camera.getYPosition();
		
		y -= pixelSize; // Fix for a tiny bug, where the y axis is slightly offset by one pixelSize. Reason not found.
		
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
		if (tileUnderMouse == null) {
			return;
		}
		world.setSelectedTile(tileUnderMouse);
		tileUnderMouse.setNeedsGraphicsUpdate();
		if (tileUnderMouse.getCharacter() != null && tileUnderMouse.getCharacter().getOwner().hasTurn()) {
			// We have selected a Character. Highlight their movement Range.
			walkRangeTiles = tileUnderMouse.getAllTilesInRange(tileUnderMouse.getCharacter().getMoveRange(), true);
			world.setSelectionType(SelectionType.CHARACTER);
			for (Tile tile : walkRangeTiles.keySet()) {
				tile.setNeedsGraphicsUpdate();
			}
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
		updateGraphicsForUnselection();
		
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
				repaintImage();
				break;
			case KeyEvent.VK_ENTER:
				world.setSelectionType(SelectionType.NOTHING);
				world.setSelectedTile(null);
				world.endTurn();
				if (world.getCurrentPlayer().hasCharactersLeft() == false) {
					world.endTurn();
				}
				repaintImage();
				break;
			case KeyEvent.VK_LEFT: case KeyEvent.VK_A:
				camera.moveLeft();
				repaintImage();
				break;
			case KeyEvent.VK_RIGHT: case KeyEvent.VK_D:
				camera.moveRight();
				repaintImage();
				break;
			case KeyEvent.VK_UP: case KeyEvent.VK_W:
				camera.moveUp();
				repaintImage();
				break;
			case KeyEvent.VK_DOWN: case KeyEvent.VK_S:
				camera.moveDown();
				repaintImage();
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
		for (Tile tile : attackRangeTiles) {
			tile.setNeedsGraphicsUpdate();
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	//endregion
	
	
}
