package Engine;

import java.util.Set;

/**
 * The children which are playing the game.
 *
 * Created by flavia on 02.03.17.
 */
public class Character {


    Player owner;
    /**
     * The Engine.Player who controls this Engine.Character.
     */
    public Player getOwner() {
        return owner;
    }


    Tile tile;
    /**
     * The Engine.Tile on which this Engine.Character is.
     */
    public Tile getTile() {
        return tile;
    }

    Weapon weapon;
    /**
     * The main Engine.Weapon held by this Engine.Character.
     */
    public Weapon getWeapon() { return weapon; }



    public Character(Player owner, Tile tile, Weapon weapon) {
        this.owner = owner;
        this.tile = tile;
        this.weapon = weapon;
    }
    
    /**
     *
     * @return The Attack range calculated with {@link Tile#getAllTilesInRange(int, boolean)} from this Character's Weapon's range. Can be null!
     */
    public Set<Tile> getAttackRangeInTiles() {
        return this.tile.getAllTilesInRange(weapon.range, false);
    }






}
