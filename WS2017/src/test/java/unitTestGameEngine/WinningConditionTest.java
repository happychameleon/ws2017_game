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

import java.util.ArrayList;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.*;

/**
 * Unit Test for WinningCondition
 * Created by m on 26/04/17.
 */
public class WinningConditionTest {
    private static boolean setUpNotDone = true;

    private World testWorld;
    private User testUser;
    private GameMap gameMap;
    private ClientGameController clientGameController;
    private ArrayList<String> characters = new ArrayList<>();
    private GameState gameState;
    private int startingPoints;
    private String testGameName;
    private WinningCondition winningCondition = WinningCondition.LAST_TEAM_STANDING;

    private World test2World;
    private User test2User;
    private User test2User2;
    private ClientGameController clientGameController2;
    private ArrayList<String> test2Characters = new ArrayList<>();
    private String testGameName2;

    @Before
    public void loadTestObjects() {
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

        test2User = new User("test2User");
        test2User2 = new User("test2User2");
        test2Characters.add("[Justus 'Medium Water Gun' 3,4]");
        test2Characters.add("[Peter 'Medium Water Gun' 1,2]");
        HashMap<User, String> users2 = new HashMap<>();
        testGameName2 = "test2Game";
        clientGameController2 = new ClientGameController(gameState, startingPoints, testGameName2, users2, gameMap);
        clientGameController2.addUserToGame(test2User);
        clientGameController2.addUserToGame(test2User2);
        test2World = new World(gameMap, clientGameController2, test2Characters);
    }

    /**
     * Tests winningCondition with only one team in the world, it has to be the winningTeam.
     */
    @Test
    public void testCheckForWinningCondition() {
        Team winningTeam = winningCondition.checkForWinningCondition(testWorld);
        Team onlyTeam = testWorld.getTurnController().getTeams().get(0);

        Assert.assertThat(winningTeam, equalTo(onlyTeam));
    }

    /**
     * Tests condition with two teams. Both teams have characters left. The method has to return null.
     */
    @Test
    public void testCheckForWinningCondition2() {
        Team noWinningTeam = winningCondition.checkForWinningCondition(test2World);
        boolean hasCharacters = test2World.getTurnController().getTeams().get(0).getMembers().get(0).hasCharactersLeft();
        boolean hasCharacters2 = test2World.getTurnController().getTeams().get(1).getMembers().get(0).hasCharactersLeft();

        Assert.assertThat(hasCharacters && hasCharacters2, equalTo(true));
        Assert.assertThat(noWinningTeam, equalTo(null));
    }
}

