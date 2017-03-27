package game.gamegui;

/**
 * What exactly on the selected Tile is actually selected?
 *
 * Created by flavia on 09.03.17.
 */
public enum SelectionType {
	
	/**
	 * Nothing is selected and the selected Tile should be null.
	 */
	NOTHING,
	/**
	 * The Tile itself is selected (most likely because nothing special is on it).
	 */
	TILE,
	/**
	 * The Character on the Tile is selected.
	 */
	CHARACTER,
	/**
	 * The Weapon hold by an owned Character is selected.
	 */
	OWNED_WEAPON,
	/**
	 * The Weapon lying on the floor is selected.
	 * TODO: weapon on floor not yet implemented.
	 */
	FLOOR_WEAPON
	
}
