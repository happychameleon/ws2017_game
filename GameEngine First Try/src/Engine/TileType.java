package Engine;

import java.awt.*;

/**
 * The Types a Engine.Tile can be and it's properties. (eg Grass, Forest etc.)
 * Created by flavia on 02.03.17.
 */
public enum TileType {
	
	GRASS (Color.green),
	WATER (Color.blue);
	
	final Color[][] pixels;
	
	TileType(Color color) {
		
		pixels = new Color[Tile.tileSizeInPixels][Tile.tileSizeInPixels];
		
		//TODO: Read the pixels from a File instead of this here!
		for (int x = 0; x < pixels.length; x++) {
			for (int y = 0; y < pixels[0].length; y++) {
				pixels[x][y] = color;
			}
		}
		
		
	}
	
}
