package unitTestGameEngine;

/**
 * Created by m on 26/04/17.
 */

import game.GameMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;


public class TestGameMap {
    private GameMap gameMap;

    @Before
    public void loadMap() {
        GameMap.readInAllMaps();
        gameMap = GameMap.getMapForName("BigLake", false);
        //gameMap = GameMap.getAllMaps().get(0);// name angeben für bestimme karte getMapforName
    }

    @Test
    public void mapExists() {
        System.out.println(gameMap.getName());
        Assert.assertTrue(gameMap.getName() != null);

    }

    @Test
    public void testGetStartPositionCount() {
        int startpositions = gameMap.getStartPositionCount();
        if (gameMap.getName().equals("BigLake")) {
            if (startpositions != 9) {
                fail();
            }
        }
    }

}
