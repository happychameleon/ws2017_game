package unitTestGameEngine;

import game.ClientGameController;
import game.GameMap;
import game.GameState;
import game.engine.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import serverclient.User;

import java.util.ArrayList;
import java.util.HashMap;

import static game.engine.PlayerColor.RED;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by m on 26/04/17.
 */
public class PlayerTest {
    private static boolean setUpNotDone = true;

    private GameMap gameMap;
    private ClientGameController clientGameController;
    private ArrayList<String> characters = new ArrayList<>();
    private GameState gameState;
    private int startingPoints;
    private String testGameName;

    private World testWorld;
    private Team testTeam;
    private User testUser;
    private Player testPlayer;

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
        testTeam = testWorld.getTurnController().getTeams().get(0);
        testPlayer = new Player(testTeam, testUser, RED, testWorld);

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

    /**
     * Tests if the CurrentPlayer has the turn and is our only Player in the world.
     */
    @Test
    public void testHasTurn() {
        Player thisHasTurn = testWorld.getCurrentPlayer();
        boolean hasTurn = testWorld.getCurrentPlayer().hasTurn();
        boolean samePlayer = thisHasTurn.getName() == testPlayer.getName();

        Assert.assertThat(samePlayer, equalTo(hasTurn));
    }

    @Test
    public void testHasCharactersLeft() {
        boolean hasCharacters = testWorld.getTurnController().getTeams().get(0).getMembers().get(0).hasCharactersLeft();
        boolean hasCharactersTest = testPlayer.getTeam().getMembers().get(0).hasCharactersLeft();
        boolean testHasChar = hasCharacters && hasCharactersTest;

        Assert.assertThat(testHasChar, equalTo(true));
    }
}
