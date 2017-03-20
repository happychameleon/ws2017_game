package Engine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * The Types a main.Engine.Tile can be and it's properties. (eg Grass, Forest etc.)
 * Created by flavia on 02.03.17.
 */
public enum TileType {
	
	GRASS ("grass.png", true),
	WATER ("water.png", false);
	
	private BufferedImage tileSprite;
	
	public BufferedImage getTileSprite() {
		return tileSprite;
	}
	
	/**
	 * Can a Character ever enter this tile?
	 */
	private final boolean isWalkable;
	public boolean isWalkable() {
		return isWalkable;
	}
	
	TileType(String tileFileName, boolean isWalkable) {
		
		this.isWalkable = isWalkable;
		
		
		try {
			System.out.println("tileSprite initialised START!");
			tileSprite = ImageIO.read(getClass().getResource("/images/tiles/" + tileFileName));
			System.out.println("tileSprite initialised!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
}
