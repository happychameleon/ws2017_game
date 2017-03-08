package Engine;

import java.util.Set;

/**
 * The children which are playing the game.
 *
 * Created by flavia on 02.03.17.
 */
public class Character {

	//region Data
    private Player owner;
    /**
     * The Engine.Player who controls this Engine.Character.
     */
    public Player getOwner() {
        return owner;
    }


    private Tile tile;
    /**
     * The Engine.Tile on which this Engine.Character is.
     */
    public Tile getTile() {
        return tile;
    }

    private Weapon weapon;
    /**
     * The main Engine.Weapon held by this Engine.Character.
     */
    public Weapon getWeapon() { return weapon; }
	
	/**
	 * This is like the health of the Character. If it reaches 100% the Character is "dead". They drop all their
	 * Weapons & other stuff on the ground and leave the game.
	 */
	private int wetness = 0;
	
	public int getWetness() {
		return wetness;
	}
	
	/**
	 * This adds or removes wetness and checks whether the Character is still in the game.
	 * @param wetnessChange By how much the wetness changes. Can be negative to remove wetness.
	 */
	public void changeWetness(int wetnessChange) {
		wetness += wetnessChange;
		if (wetness < 0) {
			wetness = 0;
		}
		if (wetness >= 100) {
			KillCharacter();
		}
	}
	
	/**
	 * The name of this Character.
	 */
	private String name = "Jane";
	
	public String getName() {
		return name;
	}
	//endregion
	
	
	public Character(Player owner, Tile tile, Weapon weapon) {
        this.owner = owner;
        this.tile = tile;
        this.weapon = weapon;
        
        if (tile != null && tile.getCharacter() != null) {
            System.out.println("ERROR: new Character placed on a Tile where there is already one!");
        } else {
            tile.setCharacter(this);
        }
    }
    
    /**
     *
     * @return The Attack range calculated with {@link Tile#getAllTilesInRange(int, boolean)} from this Character's Weapon's range. Can be null!
     */
    public Set<Tile> getAttackRangeInTiles() {
        return this.tile.getAllTilesInRange(weapon.range, false);
    }
    
    @Override
    public String toString() {
        return "Character " + name + ", standing at " + tile.toString() /* TODO: add weapon + ", carrying " + weapon.toString() */ + ".";
    }
	
	/**
	 * UNDONE! This Method removes the Character from play and drops all their stuff on the ground.
	 * It also checks the winning conditions and ends the game if necessary.
	 */
	public void KillCharacter() {
    	// TODO!
    }
}
