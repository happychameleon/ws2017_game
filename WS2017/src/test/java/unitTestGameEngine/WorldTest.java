package unitTestGameEngine;

import client.ClientUser;
import game.ClientGameController;
import game.GameMap;
import game.GameState;
import game.engine.*;
import game.engine.Character;
import org.junit.Before;
import org.junit.Test;
import serverclient.User;

import java.util.ArrayList;
import java.util.HashMap;

import static game.engine.PlayerColor.RED;

/**
 * Unit Test for World
 * Created by m on 26/04/17.
 */
public class WorldTest {

    private World testWorld;
    private Team testTeam0;
    private Team testTeam1;
    private User testUser0;
    private ClientUser testUser1;
    private Player testPlayer0;
    private Player testPlayer1;
    private Character testCharacter;

    private GameMap gameMap;
    private ClientGameController clientGameController;
    private ArrayList<String> characters = new ArrayList<>();
    private GameState gameState;
    private int startingPoints;
    private String testGameName;



    private ArrayList<User> testUsers = new ArrayList<>();
    @Before
    public void setup(){
        GameMap.readInAllMaps();
        Weapon.createWeaponPrototypes();

        testTeam0 = new Team("adaTeam");
        testUser0 = new User("ada");
        testPlayer0 = new Player(testTeam0, testUser0, RED, testWorld);
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
        gameMap = GameMap.getMapForName("SmallTestMap",false);
        clientGameController = new ClientGameController(gameState,startingPoints,testGameName,users, gameMap);

        clientGameController.addUserToGame(testUser0);
        clientGameController.addUserToGame(testUser1);

        testWorld = new World(gameMap,clientGameController, characters);
    }

    @Test
    public void testNoTileSelected(){}

    @Test
    public void testATileSelected(){
        //testWorld.setSelectedTile();
        //testWorld.getSelectionType
        //setSelectionType

    }
    @Test
    public void testSelectionType(){}

}
