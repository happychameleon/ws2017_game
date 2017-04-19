package game.engine;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by flavia on 02.03.17.
 */
public class Tile {
	
	//region TileData
	/**
	 * Whether the Tile Graphic needs an update because something changed.
	 */
	private boolean needsGraphicsUpdate;
	
	/**
	 * @return {@link #needsGraphicsUpdate}
	 */
	public boolean getNeedsGraphicsUpdate() {
		return needsGraphicsUpdate;
	}
	
	/**
	 * Sets {@link #needsGraphicsUpdate} to true.
	 */
	public void setNeedsGraphicsUpdate() {
		this.needsGraphicsUpdate = true;
	}
	
	/**
	 * Sets {@link #needsGraphicsUpdate} to the given value.
	 * @param b the given value.
	 */
	public void setNeedsGraphicsUpdate(boolean b) {
		this.needsGraphicsUpdate = b;
	}
	
	/**
	 * The X position on the game map.
	 */
	private final int x;
	
	/**
	 * @return {@link #x}
	 */
	public int getXPosition() {
		return x;
	}
	
	/**
	 * The Y position on the game map.
	 */
	private final int y;
	
	/**
	 * @return {@link #y}
	 */
	public int getYPosition() {
		return y;
	}
	
	/**
	 * The {@link TileType} of this Tile.
	 */
	private TileType tileType;
	
	/**
	 * @return {@link #tileType}.
	 */
	public TileType getTileType() {
		return tileType;
	}
	
	/**
	 * @return The correct Sprite for this Tile's {@link TileType}.
	 */
	public BufferedImage getSprite() {
		return tileType.getTileSprite();
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
     * @return true if this Tile is walkable, otherwise false.
     */
    public boolean isWalkable(boolean considerCharacters) {
	    if (considerCharacters && getCharacter() != null) {
	    	return false;
	    }
    	return tileType.getIsWalkable();
    }
	
	/**
	 * All walkable tiles can be shot through,
	 * most non-walkable Tiles can't be shot through (like Trees, Walls)
	 * but some can (like Water).
	 */
    private boolean canShootThrough = true;
	
	/**
	 * @return {@link #canShootThrough}
	 */
	public boolean getCanShootThrough() {
        return canShootThrough;
    }
	
	/**
	 * The Character currently standing on this Tile. <code>null</code> if there is no Character on this Tile.
	 */
    private Character character;
	
	/**
	 * @return {@link #character}
	 */
	public Character getCharacter() {
		return character;
	}
	
	/**
	 * Sets the character of this Tile.
	 * @param character The new Character or null.
	 */
	public void setCharacter(Character character) {
		this.character = character;
		if (character == null) {
			System.out.println("Tile#setCharacter - Character on Tile " + this + " now: null");
		} else {
			System.out.println("Tile#setCharacter - Character on Tile " + this + " now: " + character);
		}
		this.setNeedsGraphicsUpdate();
	}
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
     * Returns all the Neighbours of this main.game.engine.Tile in an Array.
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
                    if (withWalking && n.isWalkable(true) == false)
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
	
	
}
