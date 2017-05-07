package unitTestGameEngine;

import game.engine.TileType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;

import static org.hamcrest.CoreMatchers.*;

/**
 * Unit test for TileType
 * <p>
 * Created by m on 26/04/17.
 */
public class TileTypeTest {
    private TileType testGRASS;
    private TileType testWATER;

    @Before
    public void setUp() {
        testGRASS = TileType.GRASS;
        testWATER = TileType.WATER;
    }

    @Test
    /**
     * Tests if the BufferedImage we get with getTileSprite() aren't null and
     * not the same.
     */
    public void getTileSprite() {
        BufferedImage testGRASSSprite = testGRASS.getTileSprite();
        BufferedImage testWATERSprite = testWATER.getTileSprite();

        Assert.assertThat(testGRASSSprite, not(equalTo(null)));
        Assert.assertThat(testWATERSprite, not(equalTo(null)));
        Assert.assertThat(testGRASSSprite, not(equalTo(testWATERSprite)));
    }

    @Test
    public void getIsWalkable() {
        boolean testGRASSWable = testGRASS.getIsWalkable();
        boolean testWATERWable = testWATER.getIsWalkable();

        Assert.assertThat(testGRASSWable, equalTo(true));
        Assert.assertThat(testWATERWable, equalTo(false));
    }

    @Test
    /**
     * Tests if we get the same TileType enum with getTypeForChar() like
     * through direct call of the enum.
     */
    public void getTypeForChar() {
        TileType testGRASSChar = TileType.getTypeForChar('G');
        TileType testWATERChar = TileType.getTypeForChar('W');

        Assert.assertThat(testGRASSChar, equalTo(testGRASS));
        Assert.assertThat(testWATERChar, equalTo(testWATER));
    }
}
