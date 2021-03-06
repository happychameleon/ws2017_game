package game.engine;

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
	
	/**
	 * How many points this Weapon costs
	 */
	private int pointCost;
	
	/**
	 * @return {@link #pointCost}.
	 */
	public int getPointCost() {
		return pointCost;
	}
	
	/**
	 * The range of the Weapon in Tiles
	 */
	private final int range;
	
    /**
     * @return {@link #range}
     */
    public int getRange() {
        return range;
    }
	
	/**
	 * The amount of wetness this weapon adds on hit.
	 */
	private final int damage;
	
	/**
	 * @return {@link #damage}.
	 */
	public int getDamage() {
		return damage;
	}
	
	/**
	 * The radius of Tiles effected when the center Tile is shot by this Weapon.
	 * Most Weapons have 0. Special Weapons like e.g. Water Balloons have a aOE > 0.
	 * Currently unused. Every Weapon has aOE == 0.
	 */
	private int areaOfEffect = 0;
	
	/**
	 * @return {@link #areaOfEffect}
	 */
	public int getAreaOfEffect() {
		return areaOfEffect;
	}
	
	/**
	 * How many actionPoint it costs a Character to shoot once with this Weapon.
	 */
	private final int actionPointPerShot;
	
	public int getActionPointPerShot() {
		return actionPointPerShot;
	}
	//endregion
	
	
	//region Weapon Prototypes
	/**
	 * These are all the prototypes of the weapons in the game. They aren't used directly in the game,
	 * they are only used to create clones from them via {@link #addWeaponPrototype}.
	 */
	private static ArrayList<Weapon> weaponPrototypes;
	
	public static Weapon getWeaponForName(String weaponName) {
		for (Weapon weapon : weaponPrototypes) {
			if (weapon.getName().equals(weaponName))
				return weapon;
		}
		return null;
	}
	
	/**
	 * Creates all the prototype weapons.
	 */
	public static void createWeaponPrototypes() {
		if (weaponPrototypes != null) {
			System.err.println("Weapon#createWeaponPrototypes - Already created!");
			return;
		}
		weaponPrototypes = new ArrayList<>();
		
		// TODO(M4)? instead of hardcoding the weapons here we could read them in from a file.
		Weapon.addWeaponPrototype("Medium Water Gun", 5, 4, 3, 25);
		Weapon.addWeaponPrototype("Heavy Water Gun", 4, 2, 4, 60);
		// TODO(M4) add more weapons.
		
		Weapon.addWeaponPrototype("Totaly OP Weapon", 2, 20, 2, 300);
	}
	
	/**
	 * @return A shallow copy of {@link #weaponPrototypes}.
	 */
	public static ArrayList<Weapon> getWeaponPrototypes() {
		return (ArrayList<Weapon>) weaponPrototypes.clone();
	}
	
	/**
	 * @return An Array with all the Weapons from {@link #weaponPrototypes}.
	 */
	public static Weapon[] getWeaponPrototypesArray() {
		return weaponPrototypes.toArray(new Weapon[]{});
	}
	
	/**
	 * This adds a new prototype to {@link #weaponPrototypes} to create Weapon Instances from.
	 *
	 * @param name {@link #name}.
	 * @param pointCost {@link #pointCost}.
	 * @param damage {@link #damage}.
	 * @param actionPointPerShot {@link #actionPointPerShot}.
	 * @param areaOfEffect {@link #areaOfEffect}.
	 * @param range {@link #range}.
	 */
	public static void addWeaponPrototype(String name, int pointCost, int damage, int actionPointPerShot, int areaOfEffect, int range) {
		//TODO(M4) Add sprite to prototype.
		Weapon prototype = new Weapon(name, pointCost, range, damage, actionPointPerShot, areaOfEffect);
		weaponPrototypes.add(prototype);
	}
	
	/**
	 * {@link #addWeaponPrototype(String, int, int, int, int, int)} but with {@link #areaOfEffect} set to 0;
	 *
	 * @param name {@link #name}.
	 * @param pointCost {@link #pointCost}.
	 * @param range {@link #range}.
	 * @param actionPointPerShot {@link #actionPointPerShot}.
	 * @param damage {@link #damage}.
	 */
	public static void addWeaponPrototype(String name, int pointCost, int range, int actionPointPerShot, int damage) {
		
		addWeaponPrototype(name, pointCost , damage, actionPointPerShot, 0, range);
	}
	
	/**
	 * This is only used to create Weapon prototypes via {@link #addWeaponPrototype}.
	 *
	 * @param name {@link #name}.
	 * @param pointCost {@link #pointCost}.
	 * @param range {@link #range}.
	 * @param damage {@link #damage}.
	 * @param actionPointPerShot {@link #actionPointPerShot}.
	 * @param areaOfEffect {@link #areaOfEffect}.
	 */
	private Weapon(String name, int pointCost, int range, int damage, int actionPointPerShot, int areaOfEffect) {
		this.pointCost = pointCost;
		this.range = range;
		this.name = name;
		this.damage = damage;
		this.actionPointPerShot = actionPointPerShot;
		this.areaOfEffect = areaOfEffect;
	}
	//endregion
	
	/**
	 * This creates a new Instance of a Weapon with the values copied from the given prototype from {@link #weaponPrototypes}.
	 * @param weaponPrototype The Weapon Prototype to create this Weapon Instance from.
	 */
	public Weapon(Weapon weaponPrototype) {
		this.range = weaponPrototype.range;
		this.name = weaponPrototype.name;
		this.damage = weaponPrototype.damage;
		this.actionPointPerShot = weaponPrototype.actionPointPerShot;
		this.areaOfEffect = weaponPrototype.areaOfEffect;
		
		
    }
	
	
	@Override
	public String toString() {
		return name + "; range " + range + "; AoE " + areaOfEffect;
	}
	
	/**
	 * @return The String we want to display in the Start Screen's Weapon Selector.
	 */
	public String getStartScreenString() {
		return name + " (" + pointCost + "Pt)";
	}
	
}
