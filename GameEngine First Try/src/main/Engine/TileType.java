package main.Engine;

import java.awt.*;

/**
 * The Types a main.Engine.Tile can be and it's properties. (eg Grass, Forest etc.)
 * Created by flavia on 02.03.17.
 */
public enum TileType {
	
	GRASS (Color.green, true),
	WATER (Color.blue, false);
	
	final Color[][] pixels;
	
	/**
	 * Can a Character ever enter this tile?
	 */
	private final boolean isWalkable;
	public boolean isWalkable() {
		return isWalkable;
	}
	
	TileType(Color color, boolean isWalkable) {
		
		this.isWalkable = isWalkable;
		
		pixels = new Color[Tile.tileSizeInPixels][Tile.tileSizeInPixels];
		
		//TODO: Read the pixels from a File instead of this here!
		for (int x = 0; x < pixels.length; x++) {
			for (int y = 0; y < pixels[0].length; y++) {
				pixels[x][y] = color;
			}
		}
		
		
	}
	
}
