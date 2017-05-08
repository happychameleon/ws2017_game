package unitTestGameEngine;

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

import static game.engine.PlayerColor.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

/**
 * Unit test for TurnController
 *
 * Created by m on 26/04/17.
 */
public class TurnControllerTest {

    private World testWorld;
    private Team testTeam0;
    private Team testTeam1;
    private User testUser0;
    private ClientUser testUser1;
    private Player testPlayer0;
    private Player testPlayer1; 

    private GameMap gameMap;
    private ClientGameController clientGameController;
    private ArrayList<String> characters = new ArrayList<>();
    private GameState gameState;
    private int startingPoints;
    private String testGameName;



    private ArrayList<User> testUsers = new ArrayList<>();

    private TurnController turnController;

    @Before
    public void setup(){
        GameMap.readInAllMaps();
        Weapon.createWeaponPrototypes();

        testTeam0 = new Team("adaTeam");
        testUser0 = new User("ada");
        testPlayer0 = new Player(testTeam0, testUser0, RED, testWorld);
        //testCharacter = new Character(testWorld, "scala", testPlayer, testWeapon);
        testTeam0.addPlayerToTeam(testPlayer0);
        characters.add("[nim 'Medium Water Gun' 1,4 go 'Medium Water Gun' 3,4]");

        testTeam1 = new Team("swiftTeam");
        testUser1 = new ClientUser("swift");
        testPlayer1 = new Player(testTeam1, testUser1, RED, testWorld);
        testTeam1.addPlayerToTeam(testPlayer1);
        characters.add("[fortran 'Medium Water Gun' 1,2 rust 'Medium Water Gun' 3,2]");

        testUsers.add(testUser0);
        testUsers.add(testUser1);

        gameState = GameState.STARTING;
        startingPoints = 10;
        testGameName = "TestGame";
        HashMap<User, String> users = new HashMap<>();
        users.put(testUser0,"[nim 'Medium Water Gun' 1,4 go 'Medium Water Gun' 3,4]");
        users.put(testUser1,"[fortran 'Medium Water Gun' 1,2 rust 'Medium Water Gun' 3,2]");
        gameMap = GameMap.getMapForName("SmallTestMap",false);
        clientGameController = new ClientGameController(gameState,startingPoints,testGameName,users, gameMap);

        testWorld = new World(gameMap,clientGameController, characters);

        turnController = new TurnController(testUsers, testWorld);
    }

    @Test
    /**
     * Tests that the method endTurn really ends the players turn.
     */
    public void testAskServerToEndTurn(){
        User testUsers3 = turnController.getCurrentPlayer().getUser();

        turnController.endTurn();

        Assert.assertThat(turnController.getCurrentPlayer().getUser(), not(testUsers3));
    }

    @Test
    /**
     * Test checks that Players are really removed with the RemovePlayer method.
     */
    public void testRemovePlayer(){
        User testUser3 = testPlayer1.getUser();

        turnController.removePlayer(testPlayer0.getUser());

        Assert.assertThat(turnController.getPlayers().size(), is(1));
        Assert.assertThat(turnController.getPlayers().get(0).getUser(),equalTo(testUser3));
    }
}
