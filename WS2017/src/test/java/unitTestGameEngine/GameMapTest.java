package unitTestGameEngine;

import game.GameMap;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by m on 02/05/17.
 */
public class GameMapTest {
    private String mapName;
    private char[][] tiles;
    private GameMap map;

    @Before
    public void create(){
        mapName = "amap";
        tiles = new char[][]{{'1', '1', 'G', '2'}, {'G', 'w', 'w', '2'}, {'3', 'W', 'W', 'G'}, {'3', 'G', '4', '4'}};
        map = new GameMap(mapName, tiles);
    }

    @Test
    public void getName() throws Exception {
        String mapNameTest = map.getName();

        boolean matches = mapNameTest.matches(mapName);
        assertTrue(matches);
    }

    @Test
    public void getTilesAsChars() throws Exception {
        char[][] mapTest = map.getTilesAsChars();

        boolean match = Arrays.equals(tiles, mapTest);
        assertTrue(match);
    }

    @Test
    public void getWidth() throws Exception {
        int testWidth = 4;
        int width = map.getWidth();

        assertTrue(testWidth == width);

    }

    @Test
    public void getHeight() throws Exception {
        int testHeight = 4;
        int height = map.getHeight();

        assertTrue(testHeight == height);
    }

    @Test
    public void getStartPositionCount() throws Exception {
        int testPostionCount = 2;
        int positionCount = map.getStartPositionCount();

        assertTrue(testPostionCount == positionCount);
    }

    @Test
    public void getAllMaps() throws Exception {
    }

    @Test
    public void getMapForName() throws Exception {
    }

    @Test
    public void readInAllMaps() throws Exception {
    }

}