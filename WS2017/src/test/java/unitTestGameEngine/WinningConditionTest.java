package unitTestGameEngine;
//package game.engine;

import client.ClientUser;
import game.ClientGameController;
import game.GameMap;
import game.GameState;
import game.engine.*;
import game.engine.Character;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import serverclient.User;

import java.util.ArrayList;
import java.util.HashMap;

import static game.engine.PlayerColor.RED;
import static org.hamcrest.CoreMatchers.*;


/**
 * Unit Test for WinningCondition
 * Created by m on 26/04/17.
 */
public class WinningConditionTest {
    private World testWorld;
    private Team testTeam;
    private User testUser;
    private ClientUser testUser1;
    private Player testPlayer;
    private Character testCharacter;
    private GameMap gameMap;
    private ClientGameController clientGameController;
    private ArrayList<String> characters = new ArrayList<>();
    private GameState gameState;
    private int startingPoints;
    private String testGameName;
    private WinningCondition winningCondition = WinningCondition.LAST_TEAM_STANDING;

    private ArrayList<User> testUsers = new ArrayList<>();

    @Before
    public void loadTestObjects() {
        GameMap.readInAllMaps();
        Weapon.createWeaponPrototypes();

        testTeam = new Team("testTeam");
        testUser = new User("testUser");
        testPlayer = new Player(testTeam, testUser, RED, testWorld);
        testTeam.addPlayerToTeam(testPlayer);
        characters.add("[Bob 'Medium Water Gun' 1,4]");

        testUsers.add(testUser);

        gameState = GameState.STARTING;
        startingPoints = 10;
        testGameName = "testGame";
        HashMap<User, String> users = new HashMap<>();
        gameMap = GameMap.getMapForName("SmallLakes",false);
        clientGameController = new ClientGameController(gameState,startingPoints,testGameName,users, gameMap);

        clientGameController.addUserToGame(testUser);

        testWorld = new World(gameMap,clientGameController, characters);
    }

    @Test
    public void testCheckForWinningCondition() {
        Team winningTeam = winningCondition.checkForWinningCondition(testWorld);

        //Assert.assertThat(winningTeam, equalTo(testTeam));
    }
}
