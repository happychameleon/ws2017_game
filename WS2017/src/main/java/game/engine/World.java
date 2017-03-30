package game.engine;

import game.GameController;
import game.gamegui.SelectionType;

import java.util.ArrayList;
import java.util.Random;

/**
 * The World holds all the game data.
 *
 * Created by flavia on 02.03.17.
 */
public class World {
	
	//region Data
	/**
	 * The gameStartController of this World. All the server-client communication goes over this GameStartController.
	 */
	private final GameController gameController;
	
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
	 * This is the World we currently play in. It should be created at the start of the game. Everything else is created
	 * when creating the World. Therefore it should never be null.
	 * There can always only be one World!
	 *
	 * TODO: Give the world to every class in the game, since there can be more worlds.
	 */
	public static World instance;
    
    /**
     * The Tile which was selected last with a left mouse click.
     * When a Character is on the selectedTile, the movement Range is shown.
     *
     * TODO: IMPORTANT Move selection to the gamegui package!
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
	
	public final TurnController turnController;
	
	public Player getCurrentPlayer() {
		return turnController.getCurrentPlayer();
	}
	
	public void endTurn() {
		turnController.endTurn();
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
	public World (int width, int height, GameController gameController, int charactersPerPlayer) {
		this.gameController = gameController;
		if (instance != null) {
			System.out.println("ERROR: There can always only be one World! But there was already one when creating a new World!");
		}
		instance = this;
		
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
        
        turnController = new TurnController(4);
		
		createWeaponPrototypes();
		
		characters = new ArrayList<>();
		for (Player player : turnController.getPlayers()) {
			for (int i = 0; i < charactersPerPlayer; i++) {
			    CreateNewRandomCharacter(player);
		    }
		}
		
	}
	
	private void createWeaponPrototypes() {
		//TODO: (maybe) instead of hardcoding the weapons here we could read them in from a file.
		Weapon.addWeaponPrototype("Medium Water Gun", 5, 4, 3, 25);
		Weapon.addWeaponPrototype("Heavy Water Gun", 4, 2, 4, 60);
		
		//TODO: Add more weapons.
	}
	
	/**
	 * JUST FOR TESTING!
	 */
	private void CreateNewRandomCharacter(Player player) {
    	Tile tile = getRandomTile(true);
    	Weapon weapon = getRandomWeapon();
    	
    	Character character = new Character(this, "Jane", player, weapon);
    	characters.add(character);
	}
	
	/**
	 * JUST FOR TESTING!
	 */
	private Tile getRandomTile(boolean walkable) {
    	// TODO: add randomness (for testing).
		Tile tile = getTileAt(0, 0);
		while(tile.isWalkable(true) == false) {
			tile = getTileAt(tile.getXPosition() + 1, tile.getYPosition());
		}
    	return tile;
	}
	
	/**
	 * JUST FOR TESTING!
	 */
	private Weapon getRandomWeapon() {
		//TODO add randomness (for testing).
		Random r = new Random();
		Weapon prototype = Weapon.getWeaponPrototypes().get(r.nextInt(Weapon.getWeaponPrototypes().size()));
		return new Weapon(prototype);
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
	
	public ArrayList<Character> getAllCharacterOfOwner (Player owner) {
		ArrayList<Character> charactersOfOwner = new ArrayList<>();
	    for (Character character : characters) {
		    if (character.getOwner() == owner)
		    	charactersOfOwner.add(character);
	    }
	    return charactersOfOwner;
    }



}
