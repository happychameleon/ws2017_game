package Graphics;

import Engine.Tile;
import Engine.World;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * The Window containing the general info about how to display the Game.
 *
 * Created by flavia on 04.03.17.
 */
public class Window extends JFrame {
	
	//FIXME: Just for testing.
	Random random = new Random();
	
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
	
	public Window(World world, String title) {
		this.world = world;
		
		pack(); // So the Insets are set correctly.
		topInset = getInsets().top;
		setSize(pixelSize * Tile.tileSizeInPixels * world.getMapWidth(), topInset + pixelSize * Tile.tileSizeInPixels * world.getMapHeight());
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle(title);
		
		setVisible(true);
		
		
	}
	
	
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
	 * This method paints one Tile with all it's Pixels via {@link #paintOnePixel(Graphics, int, int, int, int, Color)}.
	 * @param g The Graphics element.
	 * @param tile The Tile to paint.
	 */
	private void paintOneTile(Graphics g, Tile tile) {
		int tileX = tile.getXPosition();
		int tileY = tile.getYPosition();
		
		// TODO: For better performance this should only be called when something changed on the Tile.
		tile.RecalculateCurrentPixels();
		
		for (int x = 0; x < Tile.tileSizeInPixels; x++) {
			for (int y = 0; y < Tile.tileSizeInPixels; y++) {
				paintOnePixel(g, tileX, tileY, x, y, tile.getCurrentPixelAt(x, y));
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
	private void paintOnePixel(Graphics g, int tileX, int tileY, int pixelX, int pixelY, Color color) {
		int leftX = (tileX * Tile.tileSizeInPixels * pixelSize) + (pixelX * pixelSize);
		int topY = (tileY * Tile.tileSizeInPixels * pixelSize) + (pixelY * pixelSize);
		
		topY += topInset; // because of the Title Bar.

		//FIXME: This is only for testing. As soon as we read in the correct pixels in the TileType enum only the else part is needed
		if (pixelX == 0 || pixelY == 0) {
			g.setColor(color.darker());
		} else {
			g.setColor(color);
		}
		
		g.fillRect(leftX, topY, pixelSize, pixelSize);
	}
}
