package unitTestGameEngine;

import game.engine.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import serverclient.User;

import game.engine.Character;


import static game.engine.PlayerColor.RED;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by m on 26/04/17.
 */
public class PlayerTest {
    private World testWorld;
    private Team testTeam;
    private User testUser;
    private Player testPlayer;
    private Character testCharacter;
    private Weapon testWeapon;

    @Before
    public void setUp() {
        testTeam = new Team("testTeam");
        testUser = new User("userName");
        testPlayer = new Player(testTeam, testUser, RED, testWorld);
        testCharacter = new Character(testWorld, "Bob", testPlayer, testWeapon);
        testTeam.addPlayerToTeam(testPlayer);
        testPlayer.addKilledCharacter("Justus");
        testPlayer.addDeadCharacter("Peter");
    }

    @Test
    public void testGetTeam() {
        Team getTeam = testPlayer.getTeam();

        Assert.assertThat(getTeam, equalTo(testTeam));
    }

    @Test
    public void testGetUser() {
        User getUser = testPlayer.getUser();

        Assert.assertThat(getUser, equalTo(testUser));
    }

    @Test
    /**
     * Tests getName() the more flexible version of player.getUser().getName() by comparing
     * the Strings we get.
     */
    public void testGetName() {
        String getName = testPlayer.getName();

        Assert.assertThat(getName, equalTo(testPlayer.getUser().getName()));
    }

    @Test
    public void testGetColor() {
        PlayerColor getColor = testPlayer.getColor();

        Assert.assertThat(getColor, equalTo(RED));
    }

    @Test
    public void testGetKillCount() {
        int getKill = testPlayer.getKillCount();

        Assert.assertThat(getKill, is(1));

    }

    @Test
    public void testGetDeathCount() {
        int getDeath = testPlayer.getDeathCount();

        Assert.assertThat(getDeath, is(1));
    }

    @Test
    /**
     * Tests addKilledCharacter by checking if getKillCount() gives us now an higher value.
     */
    public void testAddKilledCharacter() {
        int killBefore = testPlayer.getKillCount();
        testPlayer.addKilledCharacter("Peter");
        int addKilled = testPlayer.getKillCount();

        Assert.assertThat(addKilled, is(killBefore + 1));
    }

    @Test
    /**
     * Tests addDeadCharacter by checking if getDeathCount() is now one value greater.
     */
    public void testAddDeadCharacter() {
        int deadBefore = testPlayer.getDeathCount();
        testPlayer.addDeadCharacter("Peter");
        int addDead = testPlayer.getDeathCount();

        Assert.assertThat(addDead, is(deadBefore + 1));
    }
}
