package unitTestGameEngine;

import game.engine.*;
import game.engine.Character;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;

import static org.hamcrest.CoreMatchers.*;

/**
 * Unit Test for Tile
 * Created by m on 26/04/17.
 */
public class TileTest {
    private Tile testTile;
    private World testWorld;

    @Before
    public void setUp() {
        testTile = new Tile(testWorld, 0, 0, TileType.GRASS);
    }

    @Test
    public void testGetNeedsGraphicsUpdate() {
        boolean testN = testTile.getNeedsGraphicsUpdate();

        Assert.assertThat(testN, not(equalTo(null)));
    }

    @Test
    public void testSetNeedsGraphicsUpdate() {
        testTile.setNeedsGraphicsUpdate();
        boolean testNeedsUpdate = testTile.getNeedsGraphicsUpdate();

        Assert.assertThat(testNeedsUpdate, equalTo(true));
    }

    @Test
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
    public void testIsWalkable() {
        boolean testWable = testTile.isWalkable(true);
        boolean testWable1 = testTile.isWalkable(false);

        Assert.assertThat(testWable, equalTo(true));
        Assert.assertThat(testWable1, equalTo(true));
        Assert.assertThat(testWable, equalTo(testWable1));
    }

    @Test
    public void testGetCanShootThrough() {
        boolean testShootThr = testTile.getCanShootThrough();

        Assert.assertThat(testShootThr, equalTo(true));
    }

    @Test
    public void testGetCharacter() {
        Character testGetChar = testTile.getCharacter();

        Assert.assertThat(testGetChar, equalTo(null));

    }

    @Test
    public void testHasCharacter() {
        Boolean testHasChar = testTile.hasCharacter();

        Assert.assertThat(testHasChar, equalTo(false));

    }
}