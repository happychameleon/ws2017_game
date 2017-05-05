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
    public void testRemoveGameLobby() throws Exception {
    }

    @Test
    public void testGetStartScreen() throws Exception {
    }

    @Test
    public void testStartGameForWatching() throws Exception {
    }

    @Test
    public void testAskToJoinGame() throws Exception {
    }

    @Test
    public void testAddUserToGame() throws Exception {
        ArrayList<User> users = new ArrayList<User>();
        ClientUser user0 = new ClientUser("test0");
        ClientUser user1 = new ClientUser("test1");
        users.add(user0);
        users.add(user1);
        clientGameController.addUserToGame(user0);
        clientGameController.addUserToGame(user1);

        System.out.println(clientGameController.getStartingCharacterStringForUser(user0));
        //clientGameController.thisClientIsReady(clientGameController.getStartingCharacterStringForUser(user0));

        assertThat(clientGameController.getAllUsers(), equalTo(users));
    }

    @Test
    public void testAllWaitingClients() throws Exception {
        ArrayList<User> users = new ArrayList<User>();
        ClientUser user0 = new ClientUser("test0");
        ClientUser user1 = new ClientUser("test1");
        users.add(user0);
        users.add(user1);
        clientGameController.addUserToGame(user0);
        clientGameController.addUserToGame(user1);

        assertThat(clientGameController.getAllWaitingUsers(), not(users));
    }

    @Test
    public void thisClientIsReady() throws Exception {
    }

    @Test
    public void setUserAsWaiting() throws Exception {
    }

    @Test
    public void sendStartGame() throws Exception {
    }

    @Test
    public void startGame() throws Exception {
    }

    @Test
    public void removeUser() throws Exception {
    }

    @Test
    public void askToLeaveGame() throws Exception {
    }

    @Test
    public void askToWatchGame() throws Exception {
    }

    @Test
    public void endGame() throws Exception {
    }

}