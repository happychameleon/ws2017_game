package unitTestGameEngine;

import client.ClientUser;
import game.ClientGameController;
import game.GameMap;
import game.GameState;
import org.junit.Before;
import org.junit.Test;
import serverclient.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by m on 03/05/17.
 */
public class ClientGameControllerTest {
    private GameState gameState;
    private int startingPoints;
    private char[][] tiles;
    private String testGameName;
    private GameMap mapTest;
    private ClientGameController clientGameController;

    @Before
    public void setup(){
        GameMap.readInAllMaps();

        gameState = GameState.STARTING;
        startingPoints = 10;
        testGameName = "TestGame";
        HashMap<User, String> users = new HashMap<>();
        mapTest = GameMap.getMapForName("SmallTestMap",false);

        clientGameController = new ClientGameController(gameState,startingPoints,testGameName,users, mapTest);
    }

    @Test
    /**
     * Tests whether adding a user to the game works.
     */
    public void testAddUserToGame() {
        ArrayList<User> users = new ArrayList<User>();
        ClientUser user0 = new ClientUser("test0");
        ClientUser user1 = new ClientUser("test1");
        users.add(user0);
        users.add(user1);
        clientGameController.addUserToGame(user0);
        clientGameController.addUserToGame(user1);

        assertThat(clientGameController.getAllUsers(), equalTo(users));
    }

    @Test
    /**
     * Tests whether all clients waiting for the game to start, are returned correctly.
     */
    public void testAllWaitingClients() {
        ArrayList<User> users = new ArrayList<User>();
        ClientUser user0 = new ClientUser("test0");
        ClientUser user1 = new ClientUser("test1");
        users.add(user0);
        users.add(user1);
        clientGameController.addUserToGame(user0);
        clientGameController.addUserToGame(user1);

        assertThat(clientGameController.getAllWaitingUsers(), not(users));
    }
}
