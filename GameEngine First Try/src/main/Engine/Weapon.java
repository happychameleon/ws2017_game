package main.Engine;

import java.util.ArrayList;

/**
 * Created by flavia on 02.03.17.
 */
public class Weapon {
	
	
	//region Data
	private final String name;
	
	public String getName() {
		return name;
	}
	
	private final int range;
    /**
     * The range of the Weapon in Tiles
     */
    public int getRange() {
        return range;
    }
    
    private final int damage;
	
	public int getDamage() {
		return damage;
	}
	
	/**
	 * How many actionPoint it costs a Character to shoot once with this Weapon.
	 */
	private final int actionPointPerShot;
	
	public int getActionPointPerShot() {
		return actionPointPerShot;
	}
	
	/**
	 * Which Character holds this Weapon. Can be null if on the Ground!
	 */
	Character owner;
    
    public Character getOwner() {
        return owner;
    }
    
    public void setOwner(Character owner) {
        this.owner = owner;
    }
	
	/**
	 * The tile this Weapon is on. Unused when hold by a Character.
	 */
	private Tile tile;
	
	/**
	 * @return Returns the owner's tile if this Weapon is hold by someone or the tile on which this Weapon lies.
	 */
	public Tile getTile() {
    	if (owner != null) {
    		return owner.getTile();
	    }
	    return tile;
    }
	
	/**
	 * Sets the Tile this Weapon lies on. Should be changed whenever this Weapon get's dropped.
	 * @param tile
	 */
	public void setTile(Tile tile) {
		this.tile = tile;
	}
	//endregion
	
	
	//region Weapon Prototypes
	/**
	 * These are all the prototypes of the weapons in the game. They aren't used directly in the game,
	 * they are only used to create clones from them via {@link #addWeaponPrototype}.
	 */
	static ArrayList<Weapon> weaponPrototypes = new ArrayList<>();
	
	/**
	 * This adds a new prototype to {@link #weaponPrototypes}.
	 * @param range
	 */
	public static void addWeaponPrototype (int range, String name, int damage, int actionPointPerShot) {
		//TODO: Add sprite to prototype.
		Weapon prototype = new Weapon(range, name, damage, actionPointPerShot);
		weaponPrototypes.add(prototype);
	}
	
	/**
	 * This is only used to create Weapon prototypes via {@link #addWeaponPrototype}.
	 * @param range The range of the Weapon.
	 */
	private Weapon(int range, String name, int damage, int actionPointPerShot) {
		this.range = range;
		this.name = name;
		this.damage = damage;
		this.actionPointPerShot = actionPointPerShot;
	}
	//endregion
	
	/**
	 * This creates a new Instance of a Weapon with the values copied from the given prototype from {@link #weaponPrototypes}.
	 * @param weaponPrototype
	 * @param tile
	 * @param owner
	 */
	public Weapon(Weapon weaponPrototype, Tile tile, Character owner) {
		this.range = weaponPrototype.range;
		this.name = weaponPrototype.name;
		this.damage = weaponPrototype.damage;
		this.actionPointPerShot = weaponPrototype.actionPointPerShot;
		
		this.tile = tile;
		this.owner = owner;
		
		if (owner != null) {
			owner.setWeapon(this);
		}
    }
	
	
	@Override
	public String toString() {
		return name + " with range " + range;
	}
	
	
}
