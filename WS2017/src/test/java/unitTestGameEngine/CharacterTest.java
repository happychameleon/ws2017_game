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

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import static game.engine.PlayerColor.RED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

/**
 * Unit Test for Character
 *
 * Created by m on 26/04/17.
 */
public class CharacterTest {
    private World testWorld;
    private Team testTeam0;
    private Team testTeam1;
    private User testUser0;
    private ClientUser testUser1;
    private Player testPlayer0;
    private Player testPlayer1;
    private Weapon weapon;

    private GameMap gameMap;
    private ClientGameController clientGameController;
    private ArrayList<String> characters = new ArrayList<>();
    private GameState gameState;
    private int startingPoints;
    private String testGameName;

    private Character character;
    private String characterName;



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
        characters.add("[rust 'Medium Water Gun' 1,4]");

        testUsers.add(testUser0);

        gameState = GameState.STARTING;
        startingPoints = 10;
        testGameName = "TestGame";
        HashMap<User, String> users = new HashMap<>();
        users.put(testUser0,"[nim 'Medium Water Gun' 1,4 go 'Medium Water Gun' 3,4]");

        gameMap = GameMap.getMapForName("SmallTestMap",false);
        System.out.println(users.get(testUser0));
        clientGameController = new ClientGameController(gameState,startingPoints,testGameName,users, gameMap);

        testWorld = new World(gameMap,clientGameController, characters);

        character = new Character(testWorld, characterName, testPlayer0, weapon);
    }

    @Test
    /**
     * Makes sure that if a character tries to move outside its range, false is returned.
     */
    public void testMoveCharacterToOutOfRange(){
        Tile testTile = new Tile(testWorld,1,1,TileType.GRASS);
        character.setStartingTile(testTile);

        Tile failTile = new Tile(testWorld,5,4,TileType.WATER);

        Assert.assertThat(character.moveCharacterTo(failTile,5),equalTo(false));
    }

    @Test
    /**
     * Tests whether actionPoints are reset properly after each turn.
     */
    public void testRestForNewTurn(){
        character.removeActionPoints(10);

        Assert.assertThat(character.getActionPoints(),not(10));

        character.resetForNewTurn();

        Assert.assertThat(character.getActionPoints(),is(10));
    }
}
