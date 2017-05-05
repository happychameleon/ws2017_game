package unitTestGameEngine;

import game.engine.Weapon;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit Test for Weapon
 * <p>
 * Created by m on 26/04/17.
 */
public class WeaponTest {
    private Weapon newWeapon;
    private Weapon testWeapon1;
    private Weapon testWeapon2;
    private static boolean setUpIsDone = false;

    @Before
    public void setUp() {
        if (setUpIsDone) {
            return;
        }
        Weapon.createWeaponPrototypes();
        newWeapon = new Weapon("testgun", 1, 2, 3, 4, 0);
        testWeapon1 = Weapon.getWeaponPrototypes().get(0);
        testWeapon2 = Weapon.getWeaponPrototypes().get(1);
        setUpIsDone = true;
    }

    @Test
    public void testGetName() {

    }

    @Test
    public void testGetPointCost() {
    }

    @Test
    public void testGetRange() {
    }

    @Test
    public void testGetDamage() {
    }

    @Test
    public void testGetAreaOfEffect() {
    }

    @Test
    public void testGetActionPointPerShot() {
    }

    @Test
    public void testGetWeaponForName() {
    }

    @Test
    public void testCreateWeaponPrototypes() {
    }

    @Test
    public void testGetWeaponPrototypes() {
    }

    @Test
    public void testGetWeaponPrototypesArray() {
    }

    @Test
    public void testAddWeaponPrototype() {
    }

    @Test
    public void testAddWeaponPrototype1() {
    }

    @Test
    public void testGetStartScreenString() {
    }
}
