package Engine;

/**
 * Created by flavia on 02.03.17.
 */
public class Weapon {
	
	
	//region Data
	final int range;
    /**
     * The range of the Weapon in Tiles
     */
    public int getRange() {
        return range;
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
    
    
    public Weapon(int range) {
		this.range = range;
    }


}
