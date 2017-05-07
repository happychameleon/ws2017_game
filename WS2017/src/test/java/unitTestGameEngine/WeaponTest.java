package unitTestGameEngine;

import game.engine.Weapon;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.fail;

/**
 * Unit Test for Weapon
 *
 * Created by m on 26/04/17.
 */
public class WeaponTest {
    private static boolean setUpIsDone = false;
    private Weapon newWeapon;
    private Weapon testWeapon1;
    private Weapon testWeapon2;
    private ArrayList<Weapon> compareArrayL;
    private Weapon[] compareArray;

    @Before
    public void setUp() {
        if (setUpIsDone) {
            compareArray = Weapon.getWeaponPrototypesArray();
            compareArrayL = Weapon.getWeaponPrototypes();
            return;
        }

        Weapon.createWeaponPrototypes();
        Weapon.addWeaponPrototype("testgun", 2, 3, 4, 5);
        compareArray = Weapon.getWeaponPrototypesArray();
        compareArrayL = Weapon.getWeaponPrototypes();
        System.out.println(compareArrayL);
        setUpIsDone = true;
    }

    @Test
    /**
     * Compares the String we get with getName() with the expected String.
     */
    public void testGetName() {
        testWeapon1 = Weapon.getWeaponForName("Medium Water Gun");
        testWeapon2 = Weapon.getWeaponForName("Heavy Water Gun");
        newWeapon = Weapon.getWeaponForName("testgun");
        String nameWeap1 = testWeapon1.getName();
        String nameWeap2 = testWeapon2.getName();
        String nameNewWeap = newWeapon.getName();

        Assert.assertThat(nameWeap1, equalTo("Medium Water Gun"));
        Assert.assertThat(nameWeap2, equalTo("Heavy Water Gun"));
        Assert.assertThat(nameNewWeap, equalTo("testgun"));
    }


    @Test
    public void testGetPointCost() {
        testWeapon1 = Weapon.getWeaponForName("Medium Water Gun");
        testWeapon2 = Weapon.getWeaponForName("Heavy Water Gun");
        newWeapon = Weapon.getWeaponForName("testgun");
        int pointWeap1 = testWeapon1.getPointCost();
        int pointWeap2 = testWeapon2.getPointCost();
        int pointNewWeap = newWeapon.getPointCost();

        Assert.assertThat(pointWeap1, is(5));
        Assert.assertThat(pointWeap2, is(4));
        Assert.assertThat(pointNewWeap, is(2));
    }

    @Test
    public void testGetRange() {
        testWeapon1 = Weapon.getWeaponForName("Medium Water Gun");
        testWeapon2 = Weapon.getWeaponForName("Heavy Water Gun");
        newWeapon = Weapon.getWeaponForName("testgun");
        int rangeWeap1 = testWeapon1.getRange();
        int rangeWeap2 = testWeapon2.getRange();
        int rangeNewWeap = newWeapon.getRange();

        Assert.assertThat(rangeWeap1, is(4));
        Assert.assertThat(rangeWeap2, is(2));
        Assert.assertThat(rangeNewWeap, is(3));
    }

    @Test
    public void testGetDamage() {
        testWeapon1 = Weapon.getWeaponForName("Medium Water Gun");
        testWeapon2 = Weapon.getWeaponForName("Heavy Water Gun");
        newWeapon = Weapon.getWeaponForName("testgun");
        int damWeap1 = testWeapon1.getDamage();
        int damWeap2 = testWeapon2.getDamage();
        int damNewWeap = newWeapon.getDamage();

        Assert.assertThat(damWeap1, is(25));
        Assert.assertThat(damWeap2, is(60));
        Assert.assertThat(damNewWeap, is(5));
    }

    @Test
    public void testGetAreaOfEffect() {
        testWeapon1 = Weapon.getWeaponForName("Medium Water Gun");
        testWeapon2 = Weapon.getWeaponForName("Heavy Water Gun");
        newWeapon = Weapon.getWeaponForName("testgun");
        int aOEWeap1 = testWeapon1.getAreaOfEffect();
        int aOEWeap2 = testWeapon2.getAreaOfEffect();
        int aOENewWeap = newWeapon.getAreaOfEffect();

        Assert.assertThat(aOEWeap1, is(0));
        Assert.assertThat(aOEWeap2, is(0));
        Assert.assertThat(aOENewWeap, is(0));
    }

    @Test
    public void testGetActionPointPerShot() {
        testWeapon1 = Weapon.getWeaponForName("Medium Water Gun");
        testWeapon2 = Weapon.getWeaponForName("Heavy Water Gun");
        newWeapon = Weapon.getWeaponForName("testgun");
        int aPPSWeap1 = testWeapon1.getActionPointPerShot();
        int aPPSWeap2 = testWeapon2.getActionPointPerShot();
        int aPPSNewWeap = newWeapon.getActionPointPerShot();

        Assert.assertThat(aPPSWeap1, is(3));
        Assert.assertThat(aPPSWeap2, is(4));
        Assert.assertThat(aPPSNewWeap, is(4));
    }

    @Test
    /**
     * Tests with getName if the weapon we get with getWeaponForName has the
     * expected Name.
     */
    public void testGetWeaponForName() {
        Weapon testWeap1 = Weapon.getWeaponForName("Medium Water Gun");
        Weapon testWeap2 = Weapon.getWeaponForName("Heavy Water Gun");
        Weapon testNewWeap = Weapon.getWeaponForName("testgun");

        Assert.assertThat(testWeap1.getName(), equalTo("Medium Water Gun"));
        Assert.assertThat(testWeap2.getName(), equalTo("Heavy Water Gun"));
        Assert.assertThat(testNewWeap.getName(), equalTo("testgun"));
    }

    @Test
    public void testCreateWeaponPrototypes() {
        if (Weapon.getWeaponPrototypes() != null) {
            Weapon.createWeaponPrototypes();
        } else {
            fail();
        }
    }

    @Test
    /**
     * Tests if the method gives us the same ArrayList again.
     */
    public void testGetWeaponPrototypes() {
        testWeapon1 = Weapon.getWeaponForName("Medium Water Gun");
        testWeapon2 = Weapon.getWeaponForName("Heavy Water Gun");
        newWeapon = Weapon.getWeaponForName("testgun");
        ArrayList<Weapon> testArrayL = Weapon.getWeaponPrototypes();

        Assert.assertThat(testArrayL, equalTo(compareArrayL));

    }

    @Test
    public void testGetWeaponPrototypesArray() {
        testWeapon1 = Weapon.getWeaponForName("Medium Water Gun");
        testWeapon2 = Weapon.getWeaponForName("Heavy Water Gun");
        newWeapon = Weapon.getWeaponForName("testgun");
        Weapon[] testArray = Weapon.getWeaponPrototypesArray();

        Assert.assertThat(testArray, equalTo(compareArray));
    }

    @Test
    /**
     * Tests if the Prototype was added by comparing the old ArrayList<Weapon> with
     * the new ArrayList after addWeaponPrototype().
     */
    public void testAddWeaponPrototype() {
        Weapon.addWeaponPrototype("testAdd", 2, 3, 4, 3, 4);
        ArrayList<Weapon> testAddArrayL = Weapon.getWeaponPrototypes();
        int aOEtestWeap = Weapon.getWeaponForName("testAdd").getAreaOfEffect();

        Assert.assertTrue(aOEtestWeap >= 0);
        Assert.assertThat(testAddArrayL, not(equalTo(compareArrayL)));
        System.out.println(testAddArrayL);
    }

    @Test
    /**
     * Tests if Prototype is added and if the constructor set areaOfEllect the value 0.
     */
    public void testAddWeaponPrototype1() {
        Weapon.addWeaponPrototype("testAdd1", 3, 5, 2, 4);
        ArrayList<Weapon> testAdd1ArrayL = Weapon.getWeaponPrototypes();
        int aOEtest1Weap = Weapon.getWeaponForName("testAdd1").getAreaOfEffect();

        Assert.assertThat(aOEtest1Weap, is(0));
        Assert.assertThat(testAdd1ArrayL, not(equalTo(compareArrayL)));
        System.out.println(testAdd1ArrayL);
    }

    @Test
    public void testGetStartScreenString() {
        testWeapon1 = Weapon.getWeaponForName("Medium Water Gun");
        testWeapon2 = Weapon.getWeaponForName("Heavy Water Gun");
        newWeapon = Weapon.getWeaponForName("testgun");
        String stringWeap1 = testWeapon1.getStartScreenString();
        String stringWeap2 = testWeapon2.getStartScreenString();
        String stringnewWeap = newWeapon.getStartScreenString();

        Assert.assertThat(stringWeap1, equalTo("Medium Water Gun (5Pt)"));
        Assert.assertThat(stringWeap2, equalTo("Heavy Water Gun (4Pt)"));
        Assert.assertThat(stringnewWeap, equalTo("testgun (2Pt)"));
    }
}
