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

import java.util.Arrays;

public class GameMapTest {
    private GameMap mapTest1;
    private GameMap mapTest2;
    private String testMapName;
    private char[][] tiles;
    private GameMap testMap;

    @Before
    /**
     * Gets BigLake and SmallLakes for the methods we want to test on our existing Maps.
     * Creates new Map we want to test the methods to in read and add new Maps to the game
     */
    public void loadMapObjects() {
        GameMap.readInAllMaps();
        mapTest1 = GameMap.getMapForName("BigLake", false);
        mapTest2 = GameMap.getMapForName("SmallLakes", false);
        testMapName = "newMap";
        tiles = new char[][]{{'1', '1', 'G', '2'}, {'G', 'w', 'w', '2'}, {'3', 'W', 'W', 'G'}, {'3', 'G', '4', '4'}};
        testMap = new GameMap(testMapName, tiles);
    }

    @Test
    /**
     * Tests if the maps we get are existing.
     */
    public void mapExists() {
        System.out.println(mapTest1.getName());
        Assert.assertTrue(mapTest1.getName() != null);

        System.out.println(mapTest2.getName());
        Assert.assertTrue(mapTest2.getName() != null);
    }

    @Test
    /**
     * Tests if the StartPositions were in read correctly for our existing maps and our new Map.
     */
    public void testGetStartPositionCount() {
        int startpositionsBL = mapTest1.getStartPositionCount();
        int startpositionsSL = mapTest2.getStartPositionCount();
        int startpositionsTM = testMap.getStartPositionCount();

        if (mapTest1.getName().equals("BigLake")) {
            if (startpositionsBL != 9) {
                fail();
            }
        }

        if (mapTest2.getName().equals("SmallLakes")) {
            if (startpositionsSL != 7) {
                fail();
            }
        }

        if (testMap.getName().equals("newMap")) {
            if (startpositionsTM != 2) {
                fail();
            }
        }
    }

    @Test
    public void testGetName() {
        System.out.println(testMap.getName());
        Assert.assertTrue(testMap.getName() == testMapName);
    }

    @Test
    public void testGetTilesAsChars() {
        char[][] mapTest = testMap.getTilesAsChars();

        Assert.assertTrue(Arrays.equals(mapTest, tiles));
    }

    @Test
    public void testGetWidth() {
        int testWidth = 4;
        int width = testMap.getWidth();

        Assert.assertTrue(testWidth == width);
    }

    @Test
    public void getHeight() {
        int testHeight = 4;
        int height = testMap.getHeight();

        Assert.assertTrue(testHeight == height);
    }

    @Test
    public void getAllMaps() {

    }

    @Test
    public void getMapForName() {
    }

    @Test
    public void readInAllMaps() {
    }
}
