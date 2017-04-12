package game.engine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * The Types a main.game.engine.Tile can be and it's properties. (eg Grass, Forest etc.)
 * Created by flavia on 02.03.17.
 */
public enum TileType {
	
	GRASS ("grass.png", 'G', true),
	WATER ("water.png", 'W', false);
	
	private BufferedImage tileSprite;
	
	public BufferedImage getTileSprite() {
		return tileSprite;
	}
	
	/**
	 * The char representing this TileType in the map files.
	 */
	private final char charRepresentation;
	
	/**
	 * Can a Character ever enter this tile?
	 */
	private final boolean isWalkable;
	
	/**
	 * @return {@link #isWalkable}.
	 */
	public boolean getIsWalkable() {
		return isWalkable;
	}
	
	TileType(String tileFileName, char charRepresentation, boolean isWalkable) {
		this.charRepresentation = charRepresentation;
		
		this.isWalkable = isWalkable;
		
		try {
			tileSprite = ImageIO.read(getClass().getResource("/images/tiles/" + tileFileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Gets the correct TileType for the char from the MapFile.
	 * @param c The char representing a TileType.
	 * @return The TileType.
	 */
	public static TileType getTypeForChar(char c) {
		for (TileType tileType : values()) {
			if (tileType.charRepresentation == c) {
				return tileType;
			}
		}
		return null;
	}
}
