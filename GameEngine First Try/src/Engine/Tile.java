package Engine;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by flavia on 02.03.17.
 */
public class Tile {
	
	//region TileData
	
	/**
     *  The X position on the game map.
     */
    int x;
    /**
     *  The Y position on the game map.
     */
    int y;

    TileType tileType;

    /**
     * The world this Engine.Tile belongs to.
     */
    World world;

    boolean isWalkable = true;
    /**
     * Most Tiles are walkable. E.g. Trees, Hedges, Walls etc. aren't.
     */
    public boolean isWalkable() {
        return isWalkable;
    }

    boolean canShootThrough = true;
    /**
     * All walkable tiles can be shot through,
     * most non-walkable Tiles can't be shot through (like Trees, Walls)
     * but some can (like Water).
     */
    public boolean canShootThrough() {
        return canShootThrough;
    }
    
    Character character;
	/**
	 * The Character currently standing on this Tile. <code>null</code> if there is no Character on this Tile.
	 */
	public Character getCharacter() { return character; }
	
	//endregion
    
    
    
    

    public Tile (World world, int x, int y, TileType tileType) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.tileType = tileType;
        
        VisualisationStart();
    }
	
	
	
	
	//region General Tile Methods
	
	public Tile getNorthTile() { return world.getTileAt(x, y - 1); }
    public Tile getEastTile() { return world.getTileAt(x + 1, y); }
    public Tile getSouthTile() { return world.getTileAt(x, y + 1); }
    public Tile getWestTile() { return world.getTileAt(x + 1, y); }
    public Tile getNETile() { return world.getTileAt(x + 1, y - 1); }
    public Tile getSETile() { return world.getTileAt(x + 1, y + 1); }
    public Tile getSWTile() { return world.getTileAt(x - 1, y + 1); }
    public Tile getNWTile() { return world.getTileAt(x - 1, y - 1); }
    
    
    /**
     * Returns all the Neighbours of this Engine.Tile in an Array.
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
     * Returns all the Tiles in the specified range from this Engine.Tile.
     * @param range The range in Tiles.
     * @param withWalking True if the Tiles should be reachable by walking.
     * @return An Array of all the Tiles in range.
     */
    public Tile[] getAllTilesInRange(int range, boolean withWalking) {
        
        // All the tile of which we have to check the neighbours.
        ArrayList<Tile> tilesToCheck = new ArrayList<>();
        // All the tiles we know are in range from this tile.
        HashMap<Tile, Integer> tilesInRange = new HashMap<>();
        
        tilesToCheck.add(this);
        tilesInRange.put(this, 0);
        for (int i = 0; i < range; i++) {
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
                        if (withWalking && n.isWalkable() == false)
                            continue;
                        tilesInRange.put(n, currentTileRange + 1);
                        tilesToCheck.add(n);
                    }
                }
            }
        }
    
        // We can't shoot on a Engine.Tile which isn't walkable.
        for (Tile t : tilesInRange.keySet()) {
            if (t.isWalkable == false) {
                tilesInRange.remove(t);
            }
        }
        
        return (Tile[]) tilesInRange.keySet().toArray();
    }
    
	//endregion
	
	
	//region Visualisation
	
	/**
	 * This method prepares the visualisation of this Tile.
	 */
	private void VisualisationStart() {
		
    	currentPixels = new Color[tileSizeInPixels][tileSizeInPixels];
		
	}
	
	/**
	 * This is the size of one Tile in pixels for visualisation. It doesn't represent the pixels on the screen.
	 * These depend on the Zoom Level.
	 */
	public static final int tileSizeInPixels = 32;
	
	/**
	 * This array represents the current Pixels of this Tile and how they should be displayed at the moment.
	 * It is recalculated every time something on this Tile changes.
	 */
	Color[][] currentPixels;
	
	public Color[][] getCurrentPixels() {
		return currentPixels;
	}
	
	/**
	 * This method updates the currentPixels.
	 * All the pixels for the base tile, item and Character are recalculated.
	 * It should be called every time something changed on the Tile.
	 */
	public void RecalculateCurrentPixels() {
		calculatePixelsBaseTile();
		calculatePixelsItem();
		calculatePixelsCharacter();
	}
	
	void calculatePixelsBaseTile() {
		for (int x = 0; x < tileSizeInPixels; x++) {
			for (int y = 0; y < tileSizeInPixels; y++) {
				currentPixels[x][y] = tileType.pixels[x][y];
			}
		}
	}
	
	void calculatePixelsItem() {
		//TODO: Item Visualisation.
	}
	
	void calculatePixelsCharacter() {
		if (character == null) {
			// There is no character on this Tile.
			return;
		}
		//TODO: Character visualisation.
	}
	
	
	
	//endregion

}
