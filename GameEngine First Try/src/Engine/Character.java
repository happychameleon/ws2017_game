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
	
	public void setTile(Tile newTile) {
    	if (newTile.getCharacter() != null) {
    		System.out.println("Character::setTile - ERROR: Tile already occupied!");
    		return;
	    }
    	this.tile.setCharacter(null);
		this.tile = newTile;
		newTile.setCharacter(this);
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
	
	private int moveRange = 3;
	
	/**
	 * Get's the maximum {@link #moveRange} of the Character.
	 * @return
	 */
	public int getMoveRange() {
		//TODO: Once the ActionPoints per Round are implemented this should return the maximum Tiles the Character can walk with their remaining ActionPoints.
		return moveRange;
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
	
	/**
	 * Moves the Character by one Tile.
	 * @param direction The direction (N==0; E==1; S==2; W==3) where to move.
	 * @return Whether the move was successful <code>true</code> or not <code>false</code> (e.g. blocked by sth).
	 */
	public boolean moveCharacter (int direction) {
		if (direction > 3 || direction < 0) {
			System.out.println("moveCharacter - ERROR: No valid direction");
			return false;
		}
		switch (direction) {
			case 0:
				if (tile.getNorthTile() != null && tile.getNorthTile().isWalkable(true)) {
					setTile(tile.getNorthTile());
					return true;
				}
				break;
			case 1:
				if (tile.getEastTile() != null && tile.getEastTile().isWalkable(true)) {
					setTile(tile.getEastTile());
					return true;
				}
				break;
			case 2:
				if (tile.getSouthTile() != null && tile.getSouthTile().isWalkable(true)) {
					setTile(tile.getSouthTile());
					return true;
				}
				break;
			case 3:
				if (tile.getWestTile() != null && tile.getWestTile().isWalkable(true)) {
					setTile(tile.getWestTile());
					return true;
				}
				break;
			default:
				break;
		}
		return false;
    }
	
	/**
	 * Moves the Character to the specific Tile if it is walkable.
	 * @param tile The Tile to move to.
	 * @return True if successful, false otherwise.
	 */
	public boolean moveCharacterTo(Tile tile) {
		if (tile == null) {
			return false;
		}
		
		if (tile.isWalkable(true)) {
			setTile(tile);
			return true;
		} else {
			return false;
		}
    }
    
}
