package unitTestGameEngine;

import game.engine.Player;
import game.engine.Team;
import game.engine.World;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import serverclient.User;

import java.util.ArrayList;

import static game.engine.PlayerColor.RED;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by Julischka Saravia on 06.05.2017.
 */
public class TeamTest {
    private Player testPlayer;
    private Player testplayer1;
    private Team testTeam;
    private User testUser;
    private World testWorld;


    @Before
    public void setUp() {
        testTeam = new Team("testTeam");
        testPlayer = new Player(testTeam, testUser, RED, testWorld);
        testTeam.addPlayerToTeam(testPlayer);
    }

    @Test
    public void testGetName() {
        String testName = testTeam.getName();

        Assert.assertThat(testName, equalTo("testTeam"));
    }

    @Test
    /**
     * Tests getMembers by checking the index of the existing Player in the
     * new ArrayList<Player>.
     */
    public void testGetMembers() {
        ArrayList<Player> testMemL = testTeam.getMembers();
        int testIndex = testMemL.indexOf(testPlayer);

        Assert.assertTrue(testIndex >= 0);

    }

    @Test
    /**
     * Tests addPlayerToTeam by testing if the added Player exists in the ArrayList
     * of testTeam.
     */
    public void testAddPlayerToTeam() {
        testTeam.addPlayerToTeam(testplayer1);
        boolean addWorked = testTeam.getMembers().contains(testplayer1);

        Assert.assertTrue(addWorked == true);
    }

    @Test
    public void testRemovePlayerFromTeam() {
        testTeam.removePlayerFromTeam(testPlayer);
        ArrayList<Player> testRemL = testTeam.getMembers();

        Assert.assertTrue(testRemL.isEmpty());
    }
}