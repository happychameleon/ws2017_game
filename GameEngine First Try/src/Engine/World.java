package Engine;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by flavia on 02.03.17.
 */
public class World {

    
    //region Data
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
    
    /**
     * The Tile which was selected last with a left mouse click.
     */
    private Tile selectedTile;
    
    public Tile getSelectedTile() {
        return selectedTile;
    }
    
    public void setSelectedTile(Tile selectedTile) {
        this.selectedTile = selectedTile;
    }
    
    ArrayList<Player> players;
    
    ArrayList<Character> characters;
    //endregion
	
	
	//region World Creation
	public World (int width, int height, int charactersPerPlayer) {
        tiles = new Tile[width][height];
        Random random = new Random();
        // Generate all the Tiles and randomly set the tileType.
        for (int x = 0; x < width; x++) {
	        for (int y = 0; y < height; y++) {
		        int type = random.nextInt(4);
		        TileType tileType;
		        switch (type) {
			        case 0:
			        case 1:
			        case 2:
				        tileType = TileType.GRASS;
				        break;
			        case 3:
				        tileType = TileType.WATER;
				        break;
			        default:
				        tileType = TileType.GRASS;
		        }
		        tiles[x][y] = new Tile(this, x, y, tileType);
	        }
        }
        
        // FIXME: Add real players.
		players = new ArrayList<>();
		players.add(new Player());
		
		characters = new ArrayList<>();
		for (Player player : players) {
			for (int i = 0; i < charactersPerPlayer; i++) {
			    CreateNewRandomCharacter(player);
		    }
		}
		
		for (Character character :
				characters) {
			System.out.println(character.toString());
		}
	}
	
	private void CreateNewRandomCharacter(Player player) {
    	Tile tile = getRandomTile(true);
    	Weapon weapon = getRandomWeapon();
    	
    	Character character = new Character(player, tile, weapon);
    	characters.add(character);
	}
	
	private Tile getRandomTile(boolean walkable) {
    	// TODO: add randomness (for testing).
		Tile tile = getTileAt(0, 0);
		while(tile.isWalkable(true) == false) {
			tile = getTileAt(tile.getXPosition() + 1, tile.getYPosition());
		}
    	return tile;
	}
	
	private Weapon getRandomWeapon() {
		//TODO (for testing).
		return null;
	}
	//endregion
	
	/**
	 * Get's the Tile at the specified coordinates.
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @return The Tile or <code>null</code> if the coordinates are out of range.
	 */
	public Tile getTileAt(int x, int y) {
        if (x < 0 || x >= getMapWidth()) {
            //System.out.println("ERROR: Requested Tile out of Map range!");
            return null;
        }
        if (y < 0 || y >= getMapHeight()) {
            //System.out.println("ERROR: Requested Tile out of Map range!");
            return null;
        }
        return tiles[x][y];
    }



}
