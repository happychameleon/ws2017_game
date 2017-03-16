package main.Engine;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

/**
 * Created by flavia on 02.03.17.
 */
public class Tile {
	
	//region TileData
	
	
    private final int x;
	/**
	 *  The X position on the game map.
	 */
	public int getXPosition() {
		return x;
	}
	
	private final int y;
	/**
	 *  The Y position on the game map.
	 */
	public int getYPosition() {
		return y;
	}
	
	private TileType tileType;
	
	public TileType getTileType() {
		return tileType;
	}
	
	public BufferedImage getSprite() {
		return  tileType.getTileSprite();
	}
	
	/**
	 * This is the size of one Tile in pixels for visualisation. It doesn't represent the pixels on the screen, which
	 * depend on the Zoom Level.
	 */
	public static final int tileSizeInPixels = 16;
	
	/**
     * The world this Tile belongs to.
     */
	private World world;

    /**
     * Most Tiles are walkable. Others (e.g. Trees, Hedges, Walls etc.) aren't.
     * Whether they are walkable depends on the TileType (undone: and on whether something on the Tile is blocking it).
     * @param considerCharacters If <code>true</code> Tiles with a Character considered non-walkable.
     */
    public boolean isWalkable(boolean considerCharacters) {
    	//TODO: Add check for stuff blocking the Tile.
	    if (considerCharacters && getCharacter() != null) {
	    	return false;
	    }
    	return tileType.isWalkable();
    }

    private boolean canShootThrough = true;
    /**
     * All walkable tiles can be shot through,
     * most non-walkable Tiles can't be shot through (like Trees, Walls)
     * but some can (like Water).
     */
    public boolean canShootThrough() {
        return canShootThrough;
    }
	
	/**
	 * The Character currently standing on this Tile. <code>null</code> if there is no Character on this Tile.
	 */
    private Character character;
	
	public Character getCharacter() { return character; }
	
	public void setCharacter(Character character) { this.character = character; }
	//endregion
    
    
    
    

    public Tile (World world, int x, int y, TileType tileType) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.tileType = tileType;
        
	    //VisualisationStart();
    }
	
	
	
	
	//region General Tile Methods
	
	public Tile getNorthTile() { return world.getTileAt(x, y - 1); }
    public Tile getEastTile() { return world.getTileAt(x + 1, y); }
    public Tile getSouthTile() { return world.getTileAt(x, y + 1); }
    public Tile getWestTile() { return world.getTileAt(x - 1, y); }
    public Tile getNETile() { return world.getTileAt(x + 1, y - 1); }
    public Tile getSETile() { return world.getTileAt(x + 1, y + 1); }
    public Tile getSWTile() { return world.getTileAt(x - 1, y + 1); }
    public Tile getNWTile() { return world.getTileAt(x - 1, y - 1); }
    
    
    /**
     * Returns all the Neighbours of this main.Engine.Tile in an Array.
     * Some Neighbours might be null! (At the end of the map).
     * @param includeDiagonals Whether the 4 diagonal Tiles should be included.
     * @return The Array with all the neighbouring Tiles. Depending on includeDiagonals the Array contains 4 or 8 Tiles. Some Neighbours might be null!
     */
    public Tile[] getNeighbours(boolean includeDiagonals) {
        Tile[] neighbours;
        if (includeDiagonals) {
            neighbours = new Tile[8];   // N, E, S, W, NE, SE, SW, NW
        } else {
            neighbours = new Tile[4];   // N, E, S, W
        }
        neighbours [0] = getNorthTile();
        neighbours [1] = getEastTile();
        neighbours [2] = getSouthTile();
        neighbours [3] = getWestTile();
        if (includeDiagonals) {
            neighbours [4] = getNETile();
            neighbours [5] = getSETile();
            neighbours [6] = getSWTile();
            neighbours [7] = getNWTile();
        }
        return neighbours;
    }
    
    
    /**
     * Returns all the Tiles in the specified range from this Tile.
     * @param range The range in Tiles.
     * @param withWalking True if the Tiles should be reachable by walking.
     * @return An Array of all the Tiles in range. Is <code>null</code> if this Tile isn't walkable! If not <code>null</code> then should never be empty.
     */
    public HashMap<Tile, Integer> getAllTilesInRange(int range, boolean withWalking) {
    	
    	if (this.isWalkable(false) == false || range <= 0) {
    		return null;
	    }
        
        // All the tile of which we have to check the neighbours.
        ArrayList<Tile> tilesToCheck = new ArrayList<>();
        // All the tiles we know are in range from this tile.
        HashMap<Tile, Integer> tilesInRange = new HashMap<>();
        
        tilesToCheck.add(this);
        tilesInRange.put(this, 0);
        while (tilesToCheck.isEmpty() == false) {
            Tile t = tilesToCheck.get(0);
            tilesToCheck.remove(t);
            int currentTileRange = tilesInRange.get(t);
            if (currentTileRange == range) {
                continue;
            }
            if (currentTileRange > range) {
                System.out.println("ERROR: A tile too far away was wrongly added to the tilesInRange!");
            }
            for (Tile n : t.getNeighbours(false)) {
                if (n != null && (tilesInRange.containsKey(n) == false ||
                        tilesInRange.containsKey(n) && tilesInRange.get(n) > currentTileRange)) {
                    if (withWalking && n.isWalkable(true) == false) //TODO: Should a Character be able to walk through Characters in the same Team?
                        continue;
                    tilesInRange.put(n, currentTileRange + 1);
                    tilesToCheck.add(n);
                }
            }
        }
        
        if (tilesInRange.isEmpty()) {
        	System.out.println("ERROR: Tiles In Range is empty but starting Tile wasn't! Is Character stuck?");
        	return null;
        }
	
	    System.out.println("tilesInRange: " + tilesInRange);
        
        return tilesInRange;
    }
	
	/**
	 * @return The Tile's coordinates as "(x/y)".
	 */
	@Override
	public String toString() {
		return "(" + x + "/" + y + ")";
	}
	//endregion
	
	
//	//region Visualisation
//	/**
//	 * This method prepares the visualisation of this Tile.
//	 */
//	private void VisualisationStart() {
//
//    	currentPixels = new Color[tileSizeInPixels][tileSizeInPixels];
//
//		RecalculateCurrentPixels();
//
//	}
//
//	/**
//	 * This is the size of one Tile in pixels for visualisation. It doesn't represent the pixels on the screen.
//	 * These depend on the Zoom Level.
//	 */
//	public static final int tileSizeInPixels = 16;
//
//	/**
//	 * This array represents the current Pixels of this Tile and how they should be displayed at the moment.
//	 * It is recalculated every time something on this Tile changes.
//	 */
//	private Color[][] currentPixels;
//
//	public Color getCurrentPixelAt(int x, int y) {
//		return currentPixels[x][y];
//	}
//
//	/**
//	 * This method updates the currentPixels.
//	 * All the pixels for the base tile, item and Character are recalculated.
//	 * It should be called every time something changed on the Tile.
//	 */
//	public void RecalculateCurrentPixels() {
//		calculatePixelsBaseTile();
//		calculatePixelsItem();
//		calculatePixelsCharacter();
//	}
//
//	private void calculatePixelsBaseTile() {
//		for (int x = 0; x < tileSizeInPixels; x++) {
//			for (int y = 0; y < tileSizeInPixels; y++) {
//				currentPixels[x][y] = tileType.pixels[x][y];
//			}
//		}
//	}
//
//	private void calculatePixelsItem() {
//		//TODO: Item Visualisation.
//	}
//
//	private void calculatePixelsCharacter() {
//		if (character == null) {
//			// There is no character on this Tile.
//			return;
//		}
//		//TODO: Character visualisation.
//	}
//
//
//
//
//	//endregion
	
}
