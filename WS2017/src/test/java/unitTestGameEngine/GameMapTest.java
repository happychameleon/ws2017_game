package unitTestGameEngine;

/**
 * Created by m on 26/04/17.
 */

import game.GameMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;


public class GameMapTest {
    private GameMap mapTest1;
    private GameMap mapTest2;

    @Before
    public void loadMap() {
        GameMap.readInAllMaps();
        mapTest1 = GameMap.getMapForName("BigLake", false);
        mapTest2 = GameMap.getMapForName("SmallLakes", false);
    }

    @Test
    public void mapExists() {
        System.out.println(mapTest1.getName());
        Assert.assertTrue(mapTest1.getName() != null);

    }

    @Test
    public void testGetStartPositionCount() {
        int startpositions = mapTest1.getStartPositionCount();
        if (mapTest1.getName().equals("BigLake")) {
            if (startpositions != 9) {
                fail();
            }
        }
    }

}
