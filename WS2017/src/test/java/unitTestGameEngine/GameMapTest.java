package unitTestGameEngine;

/**
 * Unit test for the GameMap
 * <p>
 * Created by m on 26/04/17.
 */

import game.GameMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;

public class GameMapTest {
    private GameMap testMap1;
    private GameMap testMap2;
    private String newMapName;
    private char[][] tiles;
    private GameMap newMap;
    private ArrayList<GameMap> compareList;

    @Before
    /**
     * Gets BigLake and SmallLakes for the methods we want to test on our existing Maps.
     * Creates new Map we want to test the methods to in read and add new Maps to the game
     */
    public void loadMapObjects() {
        GameMap.readInAllMaps();
        testMap1 = GameMap.getMapForName("BigLake", false);
        testMap2 = GameMap.getMapForName("SmallLakes", false);
        newMapName = "newMap";
        tiles = new char[][]{{'1', '1', 'G', '2'}, {'G', 'w', 'w', '2'}, {'3', 'W', 'W', 'G'}, {'3', 'G', '4', '4'}};
        newMap = new GameMap(newMapName, tiles);
        compareList = GameMap.getAllMaps();
    }

    @Test
    /**
     * Tests if the maps we get in loadMapObjects() are existing.
     */
    public void mapExists() {
        System.out.println(testMap1.getName());
        Assert.assertTrue(testMap1.getName() != null);

        System.out.println(testMap2.getName());
        Assert.assertTrue(testMap2.getName() != null);
    }

    @Test
    /**
     * Tests if the StartPositions were read correctly for existing maps and new map.
     */
    public void testGetStartPositionCount() {
        int startpositionsBL = testMap1.getStartPositionCount();
        int startpositionsSL = testMap2.getStartPositionCount();
        int startpositionsTM = newMap.getStartPositionCount();

        if (testMap1.getName().equals("BigLake")) {
            if (startpositionsBL != 9) {
                fail();
            }
        }

        if (testMap2.getName().equals("SmallLakes")) {
            if (startpositionsSL != 7) {
                fail();
            }
        }

        if (newMap.getName().equals("newMap")) {
            if (startpositionsTM != 2) {
                fail();
            }
        }
    }

    @Test
    public void testGetName() {
        System.out.println(newMap.getName());
        Assert.assertTrue(newMap.getName() == newMapName);
    }

    @Test
    public void testGetTilesAsChars() {
        char[][] mapTest = newMap.getTilesAsChars();

        Assert.assertTrue(Arrays.equals(mapTest, tiles));
    }

    @Test
    public void testGetWidth() {
        int testWidth = 4;
        int width = newMap.getWidth();

        Assert.assertTrue(testWidth == width);
    }

    @Test
    public void getHeight() {
        int testHeight = 4;
        int height = newMap.getHeight();

        Assert.assertTrue(testHeight == height);
    }

    @Test
    /**
     * Test if the methods creates the same ArrayList again when called.
     */
    public void getAllMaps() {
        ArrayList<GameMap> testList = GameMap.getAllMaps();

        if (testList.size() != compareList.size()) {
            fail();
        } else {
            for (int start = 1; start < testList.size(); start++) {
                if (testList.get(start) != compareList.get(start)) {
                    fail();
                }
            }
        }
    }

    @Test
    public void getMapForName() {
        GameMap testMap = GameMap.getMapForName("BigLake", false);

        Assert.assertTrue(testMap.getName().equals("BigLake"));
    }
}
