package Engine;

/**
 * Created by flavia on 02.03.17.
 */
public class Weapon {


    final int range;
    /**
     * The range of the Engine.Weapon in Tiles
     */
    public int getRange() {
        return range;
    }

    Character owner;





    public Weapon(int range) {
        this.range = range;
    }


}
