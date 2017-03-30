package game.engine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
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
	
	/**
	 * The Player who controls this Character.
	 */
    private Player owner;
	
	public Player getOwner() {
        return owner;
    }
	
    private BufferedImage sprite;
    
	public BufferedImage getSprite() {
		return sprite;
	}
	
	/**
	 * The movement Cost in {@link #actionPoints} per Tile moved.
	 */
	public static final int movementCostPerTile = 3;
	
	private Tile tile;
    /**
     * The Tile on which this Character is.
     */
    public Tile getTile() {
        return tile;
    }
	
	/**
	 * Sets the Character on the specified Tile, only if the Character hadn't had a Tile before.
	 * @return true if it was successful, false if the character already had a Tile.
	 */
	public boolean setStartingTile(Tile startingTile) {
    	if (this.tile == null) {
    		setTile(startingTile);
    		return true;
	    }
		System.err.println("Character#setStartingTile - Character already had a Tile!");
		return false;
    }
	
	/**
	 * This is only used by the Character themselves to move.
	 */
	private void setTile(Tile newTile) {
    	if (newTile.isWalkable(true)) {
    		System.err.println("Character#setTile - Tile already occupied!");
    		return;
	    }
	    if (this.tile != null) {
		    this.tile.setCharacter(null);
	    }
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
	
	
	/**
	 * @return The maximum move Range of the Character in Tiles.
	 */
	public int getMoveRange () {
		return actionPoints / movementCostPerTile;
	}
	
	/**
	 * The actionPoints this Character has left until it's their player's turn again.
	 */
	private int actionPoints;
	
	/**
	 * @return {@link #actionPoints}
	 */
	public int getActionPoints () {
		return actionPoints;
	}
	
	/**
	 * The actionPoints a Character has at the beginning of their player's turn.
	 * This value could change (e.g. could be raised with power ups or could be less when this Character is 50% wet).
	 */
	private int maximumActionPoints = 10;
	
	/**
	 * @return {@link #maximumActionPoints}
	 */
	public int getMaximumActionPoints() {
		return maximumActionPoints;
	}
	
	public void changeMaximumActionPoints(int maximumActionPointsChange) {
		this.maximumActionPoints += maximumActionPointsChange;
	}
	
	/**
	 * Removes the given value from {@link #actionPoints} ONLY IF there are enough actionPoints left.
	 * Called in an if statement before carrying out the action inside the if statement. In the else part optionally a
	 * message can be displayed to the player (e.g. "not enough action points!").
	 * @param actionPointsToRemove How much actionPoints the action cost.
	 * @return <code>true</code> if the action could be carried out and the actionPoints were removed,
	 * <code>false</code> if there weren't enough action points and the action could not be carried out.
	 */
	public boolean removeActionPoints (int actionPointsToRemove) {
		if (actionPoints < actionPointsToRemove) {
			System.out.println("Character.removeActionPoints - FAIL current actionPoints: " + actionPoints + "; actionPointsToRemove: " + actionPointsToRemove);
			return false;
		}
		this.actionPoints -= actionPointsToRemove;
		System.out.println("Character.removeActionPoints - SUCCESS current actionPoints: " + actionPoints + "; actionPointsToRemove: " + actionPointsToRemove);
		return true;
	}
	
	/**
	 * Sets the current {@link #actionPoints} to the value of {@link #maximumActionPoints}.
	 */
	private void resetActionPoints() {
		this.actionPoints = maximumActionPoints;
	}
	
	/**
	 * The name of this Character.
	 */
	private String name;
	
	public String getName() {
		return name;
	}
	//endregion
	
	
	public Character(World world, String name, Player owner, Weapon weapon) {
		this.world = world;
		this.name = name;
		this.owner = owner;
        this.weapon = weapon;
        
        this.actionPoints = maximumActionPoints;
        
        if (tile != null && tile.getCharacter() != null) {
            System.out.println("ERROR: new Character placed on a Tile where there is already one!");
        } else {
            tile.setCharacter(this);
        }
		
        String imageString = "/images/characters/character__topDown_" + owner.getColor() + ".png";
		
		try {
			sprite = ImageIO.read(getClass().getResource(imageString));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    /**
     *
     * @return The Attack range calculated with {@link Tile#getAllTilesInRange(int, boolean)} from this Character's Weapon's range. Can be null!
     */
    public Set<Tile> getAttackRangeInTiles() {
        return this.tile.getAllTilesInRange(weapon.getRange(), false).keySet();
    }
    
    @Override
    public String toString() {
    	String s = "Character " + name;
    	if (tile != null)
    	    s += ", Position: " + tile.toString();
    	if (weapon != null)
		    s += ", Weapon: " + weapon.toString();
    	if (owner != null)
    		s+= ", Owner: " + owner.name;
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
	 * @param destinationTile The Tile to move to.
	 * @param distance The distance between the Characters current Tile and the destinationTile.
	 * @return <code>true</code> if successful, <code>false</code> otherwise.
	 */
	public boolean moveCharacterTo(Tile destinationTile, int distance) {
		if (destinationTile == null) {
			return false;
		}
		
		if (destinationTile.isWalkable(true)) {
			if (this.removeActionPoints(distance * movementCostPerTile)) { // TODO: Check for action points!
				setTile(destinationTile);
				System.out.println(this.toString() + " moved to " + destinationTile);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
    }
	
	public HashSet<Character> getAllEnemyCharactersInRange () {
		if (weapon == null) {
			System.err.println("Character#getAllCharactersInRange - Character has no Weapon!");
			return null;
		}
		HashSet<Character> charactersInRange = new HashSet<>();
		for (Tile tile : tile.getAllTilesInRange(weapon.getRange(), false).keySet()) {
			if (tile.getCharacter() != null
					&& this.isOnSameTeamAs(tile.getCharacter()) == false) {
				charactersInRange.add(tile.getCharacter());
			}
		}
		return charactersInRange;
	}
	
	/**
	 * @param otherCharacter another Character. (Could also be the same Character)
	 * @return <code>true</code> if they're both on the same Team, <code>false</code> otherwise.
	 */
	public boolean isOnSameTeamAs(Character otherCharacter) {
		return this.owner.getTeam() == otherCharacter.owner.getTeam();
	}
	
	/**
	 * This method should be called by the owner for every Character, when the owner begins their turn.
	 */
	public void resetForNewTurn () {
		resetActionPoints();
	}
}
