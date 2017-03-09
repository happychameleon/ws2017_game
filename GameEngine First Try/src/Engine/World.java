package Engine;

import GraphicAndInput.SelectionType;

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
     * When a Character is on the selectedTile, the movement Range is shown.
     */
    private Tile selectedTile;
	
	/**
	 * @return The currently {@link #selectedTile}.
	 */
	public Tile getSelectedTile() {
        return selectedTile;
    }
	
	/**
	 * @param selectedTile The newly {@link #selectedTile}.
	 */
	public void setSelectedTile (Tile selectedTile) {
        this.selectedTile = selectedTile;
    }
	
	/**
	 * This defines what exactly on the Tile is selected. (e.g. the Tile itself, the Character on it,
	 * the Weapon hold by the Character etc.)
	 */
	private SelectionType selectionType = SelectionType.NOTHING;
	
	/**
	 * @return {@link #selectionType}
	 */
	public SelectionType getSelectionType() { return selectionType; }
	
	/**
	 * @param selectionType {@link #selectionType}
	 */
	public void setSelectionType(SelectionType selectionType) { this.selectionType = selectionType; }
	
	/**
	 * All the players Playing the game. The order of the Players in here represents the turn order
	 * (players[0] is first, then players[1] etc.).
	 * @see #getCurrentPlayer()
	 */
	private final ArrayList<Player> players;
	
	/**
	 * The index of the Player from {@link #players} which's turn it is.
	 */
	private int currentPlayerIndex = 0;
	
	/**
	 * This returns the Player from {@link #players} at index {@link #currentPlayerIndex}.
	 * @return The player which's turn it is.
	 */
	public Player getCurrentPlayer() {
		return players.get(currentPlayerIndex);
	}
	
	public void endTurn () {
		currentPlayerIndex++;
		if (players.size() <= currentPlayerIndex) { // == should also work, but just to be sure.
			currentPlayerIndex = 0;
		}
	}
    
    private ArrayList<Character> characters;
	
	public void removeCharacter(Character character) {
		if (characters.contains(character) == false) {
			System.out.println("World::removeCharacter - ERROR: Character not in characters!");
		}
		characters.remove(character);
		if (selectedTile == character.getTile()) {
			selectedTile = null;
			setSelectionType(SelectionType.NOTHING);
			//TODO: remove highlighted tiles.
		}
	}
	
	public void addCharacter(Character character) {
		if (characters.contains(character)) {
			System.out.println("World::addCharacter - ERROR: Character already in characters!");
		}
		characters.add(character);
	}
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
		
		createWeaponPrototypes();
		
		// FIXME: Add real players.
		players = new ArrayList<>();
		players.add(new Player());
		
		characters = new ArrayList<>();
		for (Player player : players) {
			for (int i = 0; i < charactersPerPlayer; i++) {
			    CreateNewRandomCharacter(player);
		    }
		}
		
	}
	
	private void createWeaponPrototypes() {
		//TODO: (maybe) instead of hardcoding the weapons here we could read them in from a file.
		Weapon.addWeaponPrototype(4, "Medium Water Gun", 30);
		//TODO: Add more weapons.
	}
	
	private void CreateNewRandomCharacter(Player player) {
    	Tile tile = getRandomTile(true);
    	Weapon weapon = getRandomWeapon();
    	
    	Character character = new Character(this, player, tile, weapon);
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
		//TODO add randomness (for testing).
		Weapon prototype = Weapon.weaponPrototypes.get(0);
		return new Weapon(prototype, null, null);
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
