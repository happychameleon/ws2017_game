package unitTestGameEngine;

import client.ClientUser;
import game.ClientGameController;
import game.GameMap;
import game.GameState;
import game.ServerGameController;
import game.engine.*;
import game.engine.Character;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.runners.statements.ExpectException;
import org.junit.rules.ExpectedException;
import server.ServerUser;
import serverclient.User;

import java.io.IOException;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import static game.engine.PlayerColor.RED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;

/**
 * Created by m on 26/04/17.
 */
public class ServerGameControllerTest {

    private World testWorld;
    private Team testTeam;
    private User testUser;
    private Player testPlayer;
    private Character testCharacter;
    private Weapon testWeapon;

    private GameState gameState;
    private int startingPoints;
    private char[][] tiles;
    private String testGameName;
    private GameMap mapTest;
    private ServerGameController serverGameController;

    @Before
    public void setup(){
        GameMap.readInAllMaps();


        testTeam = new Team("testTeam");
        testUser = new User("userName");
        testPlayer = new Player(testTeam, testUser, RED, testWorld);
        testCharacter = new Character(testWorld, "Bob", testPlayer, testWeapon);
        testTeam.addPlayerToTeam(testPlayer);

        gameState = GameState.STARTING;
        startingPoints = 10;
        testGameName = "TestGame";
        HashMap<User, String> users = new HashMap<>();
        mapTest = GameMap.getMapForName("SmallTestMap",false);

        serverGameController = new ServerGameController(gameState,startingPoints,testGameName,users, mapTest);
    }

    @Test
    /**
     * Tests whether method addUserToGame really adds a user to the game.
     */
    public void testAddUserToGame() {
        ArrayList<User> users = new ArrayList<>();
        ClientUser testUser0 = new ClientUser("java");
        ClientUser testUser1 = new ClientUser("ruby");
        users.add(testUser0);
        users.add(testUser1);
        serverGameController.addUserToGame(testUser0);
        serverGameController.addUserToGame(testUser1);

        Assert.assertThat(serverGameController.getAllUsers(), equalTo(users));
    }

    @Test
    /**
     * Test weather users are really removed from the game with the removeUser method.
     */
    public void testRemoveUserFromGame(){
        ArrayList<User> users = new ArrayList<>();
        ClientUser testUser0 = new ClientUser("java");
        ClientUser testUser1 = new ClientUser("ruby");
        users.add(testUser0);
        users.add(testUser1);
        serverGameController.addUserToGame(testUser0);
        serverGameController.addUserToGame(testUser1);

        serverGameController.removeUser(testUser0);

        Assert.assertThat(serverGameController.getAllUsers(), not(users));
    }
}
