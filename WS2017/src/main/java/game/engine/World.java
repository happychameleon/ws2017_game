package game.engine;

import game.ClientGameController;
import game.GameController;
import game.GameMap;
import game.ServerGameController;
import game.gamegui.SelectionType;
import serverclient.User;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The World holds all the game data.
 *
 * Created by flavia on 02.03.17.
 */
public class World {
	
	//region Data
	/**
	 * The gameController of this World. All the server-client communication goes over this GameRunningController.
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
			System.out.println("World#addCharacter - ERROR: Character already in characters!");
		}
		characters.add(character);
	}
	
	/**
	 * All the Tiles where each {@link Player}'s {@link Character}s can start.
	 *
	 */
	HashMap<Player, ArrayList<Tile>> startingTiles;
	//endregion
	
	
	//region World Creation
	/**
	 * The constructor for the Server.
	 * Used to easily build things differently on the Server and the Client in the future.
	 * @param gameMap The map to build the Tiles from.
	 * @param gameController The ServerGameController.
	 */
	public World(GameMap gameMap, ServerGameController gameController) {
		this(gameMap.getHeight(), gameMap.getWidth(), gameMap.getTilesAsChars(), gameController);
		
	}
	
	/**
	 * The constructor for the Client.
	 * Used to easily build things differently on the Server and the Client in the future.
	 * @param gameMap The map to build the Tiles from.
	 * @param gameController The ClientGameController.
	 */
	public World(GameMap gameMap, ClientGameController gameController) {
		this(gameMap.getHeight(), gameMap.getWidth(), gameMap.getTilesAsChars(), gameController);
		
	}
	
	/**
	 * This constructor is used by both the Server and the Client constructor.
	 * @param height {@link #getMapHeight}
	 * @param width {@link #getMapWidth}
	 * @param tileChars the char[][] to create the Tiles from.
	 * @param gameController the {@link GameController} of this game.
	 */
	private World(int height, int width, char[][] tileChars, GameController gameController) {
		this.gameController = gameController;
		
        tiles = new Tile[width][height];
        // Generate all the Tiles from the given map's tileChars.
        for (int x = 0; x < width; x++) {
	        for (int y = 0; y < height; y++) {
	        	
		        TileType tileType = TileType.getTypeForChar(tileChars[y][x]);
		        tiles[x][y] = new Tile(this, x, y, tileType);
		        
	        }
        }
		
        turnController = new TurnController(gameController.getAllUsers(), this);
		
		System.out.println("World#World - getting all starting tiles");
		getAllStartingTiles();
		
		System.out.println("World#World - reading in characters");
		characters = new ArrayList<>();
		for (Player player : turnController.getPlayers()) {
			User user = player.getUser();
			String characterString = gameController.getCharacterStringForUser(user);
			characters = parseCharacterString(characterString, player);
		}
		// TODO: Parse the Character Array and create all the characters for the correct player.
		
	}
	
	/**
	 * Takes the characterString and parses it into the characters.
	 * @param characterString The characterString to parse.
	 * @param player The player this characterString belongs to.
	 * @return the Characters from the string.
	 */
	private ArrayList<Character> parseCharacterString(String characterString, Player player) {
		ArrayList<Character> characters = new ArrayList<>();
		if (characterString.charAt(0) == '[')
			characterString = characterString.substring(1);
		
		String characterName;
		String weaponName;
		while (characterString.isEmpty() == false) {
			int spaceIndex = characterString.indexOf(" ");
			characterName = characterString.substring(0, spaceIndex);
			characterString = characterString.substring(spaceIndex + 2);
			int apostropheIndex = characterString.indexOf("'");
			weaponName = characterString.substring(0, apostropheIndex);
			characterString = characterString.substring(apostropheIndex + 2); // It should be empty when we read in the last character.
			if (Weapon.getWeaponForName(weaponName) == null) {
				System.err.println("World#parseCharacterString - weaponname wrong");
				return null;
			}
			Character character = new Character(this, characterName, player, Weapon.getWeaponForName(weaponName));
			characters.add(character);
			character.setStartingTile(getCorrectStartingTileForCharacter(character));
		}
		return characters;
	}
	
	/**
	 * This returns the correct Tile for a character
	 * @param character The Character.
	 * @return The Tile for this Character.
	 */
	private Tile getCorrectStartingTileForCharacter(Character character) {
		ArrayList<Tile> playerStartingTiles = startingTiles.get(character.getOwner());
		int i = 0;
		while (playerStartingTiles.get(i).isWalkable(true) == false) {
			i++;
		}
		return playerStartingTiles.get(i);
	}
	
	/**
	 * @return A HashMap linking all Players to an ArrayList of their starting Tiles.
	 */
	private void getAllStartingTiles() {
		startingTiles = new HashMap<>();
		for (Player player : turnController.getPlayers()) {
			startingTiles.put(player, new ArrayList<>());
		}
		// Foreach Tile add it to the correct player's list in startingTiles.
		for (int x = 0; x < getMapWidth(); x++) {
			for (int y = 0; y < getMapHeight(); y++) {
				int playerNumber = java.lang.Character.digit(gameController.getGameMap().getTilesAsChars()[y][x], 10);
				if (playerNumber < 1 || playerNumber > startingTiles.size())
					continue;
				System.out.println("World#getAllStartingTiles - playerNumber: " + playerNumber);
				Player player = turnController.getPlayers().get(playerNumber - 1);
				startingTiles.get(player).add(getTileAt(x, y));
			}
		}
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
            //System.out.println("ERROR: Requested Tile out of GameMap range!");
            return null;
        }
        if (y < 0 || y >= getMapHeight()) {
            //System.out.println("ERROR: Requested Tile out of GameMap range!");
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
