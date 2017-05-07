package unitTestGameEngine;

import client.ClientUser;
import game.ClientGameController;
import game.GameMap;
import game.GameState;
import game.engine.*;
import game.engine.Character;
import game.gamegui.SelectionType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import serverclient.User;

import java.util.ArrayList;
import java.util.HashMap;

import static game.engine.PlayerColor.RED;
import static game.gamegui.SelectionType.NOTHING;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;

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
    private Character testCharacter0;
    private Character testCharacter1;
    private Character testCharacter2;
    private Character testCharacter3;
    private Weapon weapon;

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

        weapon = Weapon.getWeaponForName("Medium Water Gun");


        testTeam0 = new Team("adaTeam");
        testUser0 = new User("ada");
        testPlayer0 = new Player(testTeam0, testUser0, RED, testWorld);
        testTeam0.addPlayerToTeam(testPlayer0);
        characters.add("[nim 'Medium Water Gun' 1,4 go 'Medium Water Gun' 3,4]");
        //testCharacter0 = new Character(testWorld,"nim",testPlayer0,weapon);
        //testCharacter1 = new Character(testWorld,"go",testPlayer0,weapon);

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
        System.out.println(users.get(testUser0));
        clientGameController = new ClientGameController(gameState,startingPoints,testGameName,users, gameMap);

        testWorld = new World(gameMap,clientGameController, characters);
    }

    @Test
    /**
     * Tests that no tiles selected is correctly represented.
     */
    public void testNoTileSelected(){
        Assert.assertThat(testWorld.getSelectedTile(), equalTo(null));
        Assert.assertThat(testWorld.getSelectionType(), equalTo(NOTHING));
    }

    @Test
    /**
     * Tests that a tile can be selected properly
     */
    public void testATileSelected(){
        Tile testTile = new Tile(testWorld, 2,2,TileType.GRASS);
        testWorld.setSelectedTile(testTile);

        Assert.assertThat(testWorld.getSelectedTile(), equalTo(testTile));
    }
    @Test
    /**
     * Tests that setting selectionType is done properly.
     */
    public void testSelectionType(){
        SelectionType selectionType = SelectionType.TILE;
        testWorld.setSelectionType(selectionType);

        Assert.assertThat(testWorld.getSelectionType(), equalTo(selectionType));
    }
}
