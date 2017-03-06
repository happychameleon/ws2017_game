package Engine;

import java.awt.*;
import java.util.Random;

/**
 * Created by flavia on 02.03.17.
 */
public class World {

    /**
     * All the Tiles on the current Map. Tile (0/0) is at the top left corner!
     * Get a specific Tile with {@link #getTileAt(int, int)}.
     */
    private Tile[][] tiles;
    
    /**
     * @return The map width in Tiles.
     */
    public int getMapWidth() {
        return tiles.length;
    }
    
    /**
     * @return The map height in Tiles.
     */
    public int getMapHeight() {
        return tiles[0].length;
    }

    
    
    public World (int width, int height) {
        tiles = new Tile[width][height];
        Random random = new Random();
        // Generate all the Tiles and randomly set the tileType.
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int type = random.nextInt(2);
                TileType tileType;
                switch (type) {
                    case 0:
                        tileType = TileType.GRASS;
                        break;
                    case 1:
                        tileType = TileType.WATER;
                        break;
                    default:
                        tileType = TileType.GRASS;
                }
                tiles[x][y] = new Tile(this, x, y, tileType);
            }
        }
    }
    
    


    public Tile getTileAt(int x, int y) {
        if (x < 0 || x >= getMapWidth()) {
            System.out.println("ERROR: Requested Engine.Tile out of Map range!");
            return null;
        }
        if (y < 0 || y >= getMapHeight()) {
            System.out.println("ERROR: Requested Engine.Tile out of Map range!");
            return null;
        }
        return tiles[x][y];
    }



}
