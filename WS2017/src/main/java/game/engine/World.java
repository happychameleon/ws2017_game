package game.engine;

import game.ClientGameController;
import game.GameController;
import game.GameMap;
import game.ServerGameController;
import game.gamegui.SelectionType;
import game.gamegui.Window;

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
	 * @return {@link #gameController}.
	 */
	public GameController getGameController() {
		return gameController;
	}
	
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
	 * TODO(M4) Move selection to the gamegui package!
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
	 * @return {@link #turnController}.
	 */
	public TurnController getTurnController() {
		return turnController;
	}
	
	/**
	 * The {@link TurnController} of this world.
	 */
	private final TurnController turnController;
	
	/**
	 * @return the Player who's turn it is (via {@link TurnController#getCurrentPlayer()}).
	 */
	public Player getCurrentPlayer() {
		return turnController.getCurrentPlayer();
	}
	
	/**
	 * Calls the {@link TurnController#askServerToEndTurn} method.
	 * Is only processed when this Client actually has the turn.
	 */
	public void endTurn() {
		turnController.askServerToEndTurn();
	}
	
	/**
	 * All the {@link Character}s in the game.
	 */
	private final ArrayList<Character> characters;
	
	/**
	 * All the Tiles where each {@link Player}'s {@link Character}s can start.
	 */
	HashMap<Player, ArrayList<Tile>> startingTiles;
	
	/**
	 * The conditions to win for this game.
	 */
	WinningCondition winningCondition;
	//endregion
	
	
	//region World Creation
	/**
	 * The constructor for the Server.
	 * Used to easily build things differently on the Server and the Client in the future.
	 * @param gameMap The map to build the Tiles from.
	 * @param gameController The ServerGameController.
	 */
	public World(GameMap gameMap, ServerGameController gameController) {
		this(gameMap.getHeight(), gameMap.getWidth(), gameMap.getTilesAsChars(), gameController, null);
		
	}
	
	/**
	 * The constructor for the Client.
	 * Used to easily build things differently on the Server and the Client in the future.
	 * @param gameMap The map to build the Tiles from.
	 * @param gameController The ClientGameController.
	 * @param characterStrings see characterString description at {@link #World(int, int, char[][], GameController, ArrayList)}
	 */
	public World(GameMap gameMap, ClientGameController gameController, ArrayList<String> characterStrings) {
		this(gameMap.getHeight(), gameMap.getWidth(), gameMap.getTilesAsChars(), gameController, characterStrings);
		
	}
	
	/**
	 * This constructor is used by both the Server and the Client constructor.
	 * @param height {@link #getMapHeight}
	 * @param width {@link #getMapWidth}
	 * @param tileChars the char[][] to create the Tiles from.
	 * @param gameController the {@link GameController} of this game.
	 * @param characterStrings If the world is only created for a watching user and is already running on the server, the characterStrings holds the info for all the already existing characters. Otherwise null.
	 */
	private World(int height, int width, char[][] tileChars, GameController gameController, ArrayList<String> characterStrings) {
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
		
		getAllStartingTiles();
		
		characters = new ArrayList<>();
		for (int i = 0; i < turnController.getPlayers().size(); i++) {
			Player player = turnController.getPlayers().get(i);
			String characterString = "";
			if (characterStrings == null) {
				characterString = gameController.getStartingCharacterStringForUser(player.getUser());
			} else {
				characterString = characterStrings.get(i);
			}
			ArrayList<Character> newCharacters = parseCharacterString(characterString, player);
			for (Character character : newCharacters) {
				characters.add(character);
			}
		}
		
		winningCondition = WinningCondition.LAST_TEAM_STANDING;
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
		String startPosition;
		while (true) {
			System.out.println();
			System.out.println("World#parseCharacterString - characterString: " + characterString);
			int spaceIndex = characterString.indexOf(" ");
			characterName = characterString.substring(0, spaceIndex);
			characterString = characterString.substring(spaceIndex + 2);
			int apostropheIndex = characterString.indexOf("'");
			weaponName = characterString.substring(0, apostropheIndex);
			characterString = characterString.substring(apostropheIndex + 2);
			if (Weapon.getWeaponForName(weaponName) == null) {
				System.err.println("World#parseCharacterString - weaponname wrong");
				return null;
			}
			if (characterString.contains(" ")) {
				spaceIndex = characterString.indexOf(" ");
			} else {
				if (characterString.contains("]")) {
					spaceIndex = characterString.indexOf("]");
				} else {
					spaceIndex = characterString.length();
				}
			}
			startPosition = characterString.substring(0, spaceIndex);
			characterString = characterString.substring(spaceIndex);
			
			Character character = new Character(this, characterName, player, Weapon.getWeaponForName(weaponName));
			characters.add(character);
			character.setStartingTile(getCorrectStartingTileForCharacter(character, startPosition));
			
			if (characterString.isEmpty() || characterString.equals("]")) {
				break;
			} else {
				characterString = characterString.substring(1); // For the space to the next Name.
			}
		}
		return characters;
	}
	
	/**
	 * This returns the correct starting Tile for a character on either the server or client side.
	 * @param character The Character.
	 * @param startPosition The startPosition for the Character as a string.
	 *                         If it is X,Y instead of actual numbers, the startposition has to be calculated on the server.
	 * @return The Tile for this Character.
	 */
	private Tile getCorrectStartingTileForCharacter(Character character, String startPosition) {
		System.out.println("World#getCorrectStartingTileForCharacter - startPosition: " + startPosition);
		if (startPosition.equals("X,Y")) {
			ArrayList<Tile> playerStartingTiles = startingTiles.get(character.getOwner());
			int i = 0;
			while (playerStartingTiles.get(i).isWalkable(true) == false) {
				i++;
			}
			return playerStartingTiles.get(i);
		} else {
			int x = Integer.parseInt(startPosition.substring(0, startPosition.indexOf(",")));
			int y = Integer.parseInt(startPosition.substring(startPosition.indexOf(",") + 1));
			return getTileAt(x, y);
		}
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
				Player player = turnController.getPlayers().get(playerNumber - 1);
				startingTiles.get(player).add(getTileAt(x, y));
			}
		}
	}
	//endregion
	
	//region Methods
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
	
	/**
	 * Returns an ArrayList of all the Characters of the given Player.
	 * @param owner The given Player
	 * @return An ArrayList of all the Characters of the given Player.
	 */
	public ArrayList<Character> getAllCharacterOfOwner (Player owner) {
		ArrayList<Character> charactersOfOwner = new ArrayList<>();
		for (Character character : characters) {
			if (character.getOwner() == owner)
				charactersOfOwner.add(character);
		}
		return charactersOfOwner;
	}
	
	/**
	 * Removes the given character for either being killed or when a user leaves the game.
	 * Doesn't check winning conditions and other stuff.
	 * @param character The Character to remove.
	 */
	public void removeCharacter(Character character) {
		if (characters.contains(character) == false) {
			System.out.println("World::removeCharacter - ERROR: Character not in characters!");
		}
		characters.remove(character);
		character.getTile().setCharacter(null);
		
		if (gameController instanceof ClientGameController) {
			Window window = ((ClientGameController) gameController).getWindow();
			if (selectedTile == character.getTile()) {
				selectedTile = null;
				setSelectionType(SelectionType.NOTHING);
			}
		}
		
		checkWinningCondition();
	}
	
	/**
	 * Checks whether the WinningConditions are met and if so carries them out.
	 * Only done on the Server. The Clients then get informed by the Server.
	 */
	public void checkWinningCondition() {
		if (gameController instanceof ServerGameController) {
			System.out.println("World#checkWinningCondition - On Server");
			
			Team winningTeam = winningCondition.checkForWinningCondition(this);
			
			if (winningTeam != null) {
				((ServerGameController) gameController).teamHasWon(winningTeam);
			}
		}
	}
	
	/**
	 * Prints a infoMessage to the gameLobby only IF this happens on the client side (so there is a gameLobby).
	 * @param infoMessage The info to print
	 */
	public void printToGameLobby(String infoMessage) {
		if (getGameController() instanceof ClientGameController) {
			if (((ClientGameController) getGameController()).getGameLobby() != null) {
				((ClientGameController) getGameController()).getGameLobby().getLobbyChat().displayInfo(infoMessage);
			}
		}
	}
	
	/**
	 * This Method carries out an Attack by the selected Character against the Character standing on the given Tile
	 * @param attackingTile The Tile where the attacking Character stands on.
	 * @param targetedTile The Tile where the targeted Character stands on.
	 * @param attackIntensity The 'damage' this attack causes.
	 */
	public void attackCharacter(Tile attackingTile, Tile targetedTile, int attackIntensity) {
		System.out.println("World#attackCharacter");
		if (targetedTile.hasCharacter() == false || attackingTile.hasCharacter() == false) {
			System.err.println("World#attackCharacter - character check went wrong.");
		}
		
		Character targetedCharacter = targetedTile.getCharacter();
		Character attackingCharacter = attackingTile.getCharacter();
		
		if (attackingCharacter.removeActionPoints(attackingCharacter.getWeapon().getActionPointPerShot())) {
			targetedCharacter.changeWetness(attackingCharacter, attackIntensity);
		} else {
			System.err.println("World#attackCharacter - actionpoints check went wrong.");
		}
		
		if (gameController instanceof ClientGameController)
			((ClientGameController) gameController).getWindow().getMainGamePanel().repaintImage();
	}
	//endregion
}
