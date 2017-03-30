package game.startscreen;

import game.GameController;
import game.engine.Character;
import game.engine.*;
import game.gamegui.Window;

import java.util.ArrayList;

/**
 *
 *
 * Created by flavia on 27.03.17.
 */
public class GameStartController {
	
	/**
	 * The world the game is playing in.
	 */
	World world;
	
	/**
	 * @return {@link #world}.
	 */
	public World getWorld() {
		return world;
	}
	
	/**
	 * The window where the game is played in.
	 */
	Window gameWindow;
	
	/**
	 * @return {@link #gameWindow}
	 */
	public Window getGameWindow() {
		return gameWindow;
	}
	
	/**
	 * The Player this Client represents.
	 */
	Player thisClientPlayer;
	
	/**
	 * @return {@link #thisClientPlayer}.
	 */
	public Player getThisClientPlayer() {
		return thisClientPlayer;
	}
	
	
	//region game start
	/**
	 * This is called when a new Game is opened and this client clicked join.
	 */
	public void beginGameWithStartScreen() {
	
	}
	
	/**
	 * This is called by the StartScreen window and tells the server that this client is ready to play.
	 */
	public void clientIsReady (ArrayList<Character> characters) {
		for (Character character : characters) {
			world.addCharacter(character);
		}
		// TODO: Inform the server with the selected characters.
	}
	
	/**
	 * When all clients are ready the server sends the start game command and sends all the necessary info to this client.
	 *
	 */
	public void startGame() {
		gameWindow = new Window(world, "WasserschlachtSimulator 2017 Prototype");
		
	}
	//endregion
	
	
	
	/**
	 * Currently: this just returns the next free Tile on the Top row from the left. Not usable for multiplayer.
	 * TODO: This returns an empty Tile for a Character in the Map Corner of this CLient.
	 * @return
	 */
	public Tile getStartingTileForCharacter() {
		Tile tile = world.getTileAt(0, 0);
		while(tile.isWalkable(true) == false) {
			tile = world.getTileAt(tile.getXPosition() + 1, tile.getYPosition());
		}
		return tile;
	}
	
	
	/**
	 * Only for testing to see if the game still works.
	 * TODO Meilenstein 3: remove this test.
	 */
	public static void main (String[] args) {
		createWeaponPrototypes();
		new StartScreen(new GameStartController(), 30, PlayerColor.YELLOW);
		//startNewGameForTesting();
	}
	
	/**
	 * JUST FOR TESTING
	 * Starts a new Game with default values to test some mechanics. Not intended for actual use.
	 */
	public static void startNewGameForTesting() {
		World world = new World(30, 25, new GameController(), 2);
		Window window = new Window(world, "WasserschlachtSimulator 2017 Prototype");
	}
	
	/**
	 * JUST FOR TESTING
	 */
	private static void createWeaponPrototypes() {
		Weapon.addWeaponPrototype("Medium Water Gun", 5, 4, 3, 25);
		Weapon.addWeaponPrototype("Heavy Water Gun", 4, 2, 4, 60);
	}
	
	
	
}
