package Engine;

import java.util.HashSet;
import java.util.Set;

/**
 * The children which are playing the game.
 *
 * Created by flavia on 02.03.17.
 */
public class Character {

	//region Data
	private World world;
	
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
	
	/**
	 * The main Weapon held by this Character.
	 */
	private Weapon weapon;
    
    public Weapon getWeapon() { return weapon; }
	
	public void setWeapon(Weapon weapon) {
    	if (this.weapon != null) {
    		System.out.println("Character::setWeapon - ERROR: Character already has a weapon!");
	    }
		this.weapon = weapon;
	}
	
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
	
	
	public Character(World world, Player owner, Tile tile, Weapon weapon) {
		this.world = world;
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
        return this.tile.getAllTilesInRange(weapon.getRange(), false);
    }
    
    @Override
    public String toString() {
    	String s = "Character " + name;
    	if (tile != null)
    	    s += ", standing at " + tile.toString();
    	if (weapon != null)
		    s += ", carrying " + weapon.toString();
    	s += ", Wetness: " + wetness + "%";
    	return s;
    }
	
	/**
	 * UNDONE! This Method removes the Character from play and drops all their stuff on the ground.
	 * It also checks the winning conditions and ends the game if necessary.
	 */
	public void KillCharacter() {
		if (tile != null) {
			tile.setCharacter(null);
		}
		world.removeCharacter(this);
    	// If more Lists with characters are implemented, remove them from there to! (E.g. Every Player will have a Character List.)
		System.out.println(this.toString() + " has been \"killed\"!");
		//TODO? Add to list of killed Characters for statistic or "kill" count or other info.
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
			System.out.println(this.toString() + " moved to " + tile);
			return true;
		} else {
			return false;
		}
    }
	
	
	public HashSet<Character> getAllCharactersInRange () {
		if (weapon == null) {
			System.out.println("Character::getAllCharactersInRange - ERROR: Character has no Weapon!");
			return null;
		}
		HashSet<Character> charactersInRange = new HashSet<>();
		for (Tile tile : tile.getAllTilesInRange(weapon.getRange(), false)) {
			if (tile.getCharacter() != null
					/*TODO: add check for same Team! && tile.getCharacter().getOwner().getTeam() != this.getOwner().getTeam()*/) {
				charactersInRange.add(tile.getCharacter());
			}
		}
		return charactersInRange;
	}
    
}
