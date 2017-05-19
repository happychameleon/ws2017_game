package unitTestGameEngine;

import game.ClientGameController;
import game.GameMap;
import game.GameState;
import game.engine.*;
import game.engine.Character;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import serverclient.User;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.*;

/**
 * Unit Test for Tile
 * Created by m on 26/04/17.
 */
public class TileTest {
    private boolean setUpNotDone = true;

    private Tile testTile;
    private Tile testTile2;

    private World testWorld;
    private User testUser;
    private GameMap gameMap;
    private ClientGameController clientGameController;
    private ArrayList<String> characters = new ArrayList<>();
    private GameState gameState;
    private int startingPoints;
    private String testGameName;
    private Character charFromTile;

    @Before
    public void setUp() {
        if (setUpNotDone) {
            GameMap.readInAllMaps();
            Weapon.createWeaponPrototypes();
            setUpNotDone = false;
        }

        gameState = GameState.STARTING;
        startingPoints = 10;
        testUser = new User("testUser");
        characters.add("[Bob 'Medium Water Gun' 1,4]");
        testGameName = "testGame";
        HashMap<User, String> users = new HashMap<>();
        gameMap = GameMap.getMapForName("SmallLakes", false);
        clientGameController = new ClientGameController(gameState, startingPoints, testGameName, users, gameMap);
        clientGameController.addUserToGame(testUser);
        testWorld = new World(gameMap, clientGameController, characters);

        testTile = testWorld.getTileAt(0, 0);
        testTile2 = testWorld.getTileAt(1, 4);
        charFromTile = testTile2.getCharacter();
    }

    @Test
    public void testGetNeedsGraphicsUpdate() {
        boolean testN = testTile.getNeedsGraphicsUpdate();

        Assert.assertThat(testN, anyOf(equalTo(true), equalTo(false)));
    }

    @Test
    /**
     * Tests setNeedsGraphicUpdate() without parameter, has to return true.
     */
    public void testSetNeedsGraphicsUpdate() {
        testTile.setNeedsGraphicsUpdate();
        boolean testNeedsUpdate = testTile.getNeedsGraphicsUpdate();

        Assert.assertThat(testNeedsUpdate, equalTo(true));
    }

    @Test
    /**
     * Tests setNeedsGraphicUpdate with boolean parameter, has to return the parameter.
     */
    public void testSetNeedsGraphicsUpdate1() {
        testTile.setNeedsGraphicsUpdate(false);
        boolean testNeedsUpdate1 = testTile.getNeedsGraphicsUpdate();

        Assert.assertThat(testNeedsUpdate1, equalTo(false));

    }

    @Test
    public void testGetXPosition() {
        int testGetX = testTile.getXPosition();

        Assert.assertThat(testGetX, is(0));
    }

    @Test
    public void testGetYPosition() {
        int testGetY = testTile.getYPosition();

        Assert.assertThat(testGetY, is(0));
    }

    @Test
    public void testGetTileType() {
        TileType testGetType = testTile.getTileType();

        Assert.assertThat(testGetType, equalTo(TileType.GRASS));
    }

    @Test
    public void testGetSprite() {
        BufferedImage testGetSprite = testTile.getSprite();

        Assert.assertThat(testGetSprite, not(equalTo(null)));
        Assert.assertThat(testGetSprite, not(equalTo(TileType.WATER.getTileSprite())));
    }

    @Test
    /**
     * Tests isWalkable() for a tile with character. Without consider the character as blocking it should be walkable.
     * With consideration it should be false. They can't be the same value.
     */
    public void testIsWalkable() {
        boolean testWable = testTile2.isWalkable(true);
        boolean testWable1 = testTile2.isWalkable(false);

        Assert.assertThat(testWable, equalTo(false));
        Assert.assertThat(testWable1, equalTo(true));
        Assert.assertThat(testWable, not(equalTo(testWable1)));
    }

    @Test
    public void testGetCanShootThrough() {
        boolean testShootThr = testTile.getCanShootThrough();

        Assert.assertThat(testShootThr, equalTo(true));
    }

    @Test
    /**
     *Tests getCharacter for two tiles with and without character. Should return null for the tile without character
     * and the character for the other tile.
     */
    public void testGetCharacter() {
        Character testGetChar = testTile.getCharacter();
        Character test2GetChar = testTile2.getCharacter();

        Assert.assertThat(testGetChar, equalTo(null));
        Assert.assertThat(test2GetChar, allOf(equalTo(charFromTile), is(notNullValue())));

    }

    @Test
    public void testHasCharacter() {
        Boolean testHasChar = testTile.hasCharacter();
        Boolean test2HasChar = testTile2.hasCharacter();

        Assert.assertThat(testHasChar, equalTo(false));
        Assert.assertThat(test2HasChar, equalTo(true));
    }

    /**
     * Tests setCharacter with the method getNeedsGraphicsUpdate. If the method setCharacter worked the tile should
     * need a graphicsUpdate therefore the value has to be true.
     */
    @Test
    public void testSetCharacter() {
        testTile.setCharacter(charFromTile);
        boolean worked = testTile.getNeedsGraphicsUpdate();

        Assert.assertThat(worked, equalTo(true));
    }

    /**
     * Tests the cardinal direction method at the (0/0) Tile. north/west/north east/ south west/ north west are
     * outside the map they have to be null. The remaining directions are east:(0 + 1, 0)...
     */
    @Test
    public void testCompassTile() {
        Tile north = testTile.getNorthTile();
        Tile east = testTile.getEastTile();
        Tile south = testTile.getSouthTile();
        Tile west = testTile.getWestTile();
        Tile noea = testTile.getNETile();
        Tile soea = testTile.getSETile();
        Tile sowe = testTile.getSWTile();
        Tile nowe = testTile.getNWTile();

        boolean gotNull = north == null && west == null && noea == null && sowe == null && nowe == null;

        Assert.assertThat(gotNull, equalTo(true));
        Assert.assertThat(east.toString(), equalTo("(1/0)"));
        Assert.assertThat(south.toString(), equalTo("(0/1)"));
        Assert.assertThat(soea.toString(), equalTo("(1/1)"));
    }

    /**
     * Tests getNeighbours with a compare array which gets the tiles through the specific direction methods. The
     * arrays should have the same entries to [3] without the diagonals and to [8] with them.
     */
    @Test
    public void testGetNeighbours() {
        Tile[] neighDiag = testTile.getNeighbours(true);
        Tile[] neighNoDiag = testTile.getNeighbours(false);
        Tile[] testNeigh = new Tile[8];
        testNeigh[0] = testTile.getNorthTile();
        testNeigh[1] = testTile.getEastTile();
        testNeigh[2] = testTile.getSouthTile();
        testNeigh[3] = testTile.getWestTile();
        testNeigh[4] = testTile.getNETile();
        testNeigh[5] = testTile.getSETile();
        testNeigh[6] = testTile.getSWTile();
        testNeigh[7] = testTile.getNWTile();

        Assert.assertThat(neighDiag.length, is(8));
        Assert.assertThat(neighNoDiag.length, is(4));

        for (int toFour = 0; toFour > 4; toFour++) {
            boolean DiagTest = testNeigh[toFour] == neighDiag[toFour];
            boolean noDiagTest = testNeigh[toFour] == neighNoDiag[toFour];
            Assert.assertThat(DiagTest, equalTo(true));
            Assert.assertThat(noDiagTest, equalTo(true));
            for (int toEnd = 4; toEnd > 8; toEnd++) {
                DiagTest = testNeigh[toEnd] == neighDiag[toEnd];
                Assert.assertThat(DiagTest, equalTo(true));
            }
        }
    }

    /**
     * Testing getAllTilesInRange with a range of 1. The HashMap should have the tiles of the four cardinal direction
     * and the tested tile.
     */
    @Test
    public void testGetAllTilesInRange() {
        HashMap<Tile, Integer> inRange = testTile2.getAllTilesInRange(1, true);
        Tile north = testTile2.getNorthTile();
        Tile east = testTile2.getEastTile();
        Tile west = testTile2.getWestTile();
        Tile south = testTile2.getSouthTile();

        Assert.assertThat(inRange.size(), is(5));
        Assert.assertThat(inRange.containsKey(testTile2), equalTo(true));
        Assert.assertThat(inRange.containsKey(north), equalTo(true));
        Assert.assertThat(inRange.containsKey(east), equalTo(true));
        Assert.assertThat(inRange.containsKey(west), equalTo(true));
        Assert.assertThat(inRange.containsKey(south), equalTo(true));
    }
}