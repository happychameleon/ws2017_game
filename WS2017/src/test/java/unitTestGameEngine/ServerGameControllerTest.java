package unitTestGameEngine;

import game.ClientGameController;
import game.GameMap;
import game.GameState;
import game.ServerGameController;
import org.junit.Before;
import org.junit.Test;
import server.ServerUser;
import serverclient.User;

import java.util.HashMap;

/**
 * Created by m on 26/04/17.
 */
public class ServerGameControllerTest {
    private GameState gameState;
    private int startingPoints;
    private char[][] tiles;
    private String testGameName;
    private GameMap mapTest;
    private ServerGameController serverGameController;

    @Before
    public void setup(){
        GameMap.readInAllMaps();

        gameState = GameState.STARTING;
        startingPoints = 10;
        testGameName = "TestGame";
        HashMap<User, String> users = new HashMap<>();
        mapTest = GameMap.getMapForName("SmallTestMap",false);

        serverGameController = new ServerGameController(gameState,startingPoints,testGameName,users, mapTest);
    }

    @Test
    public void testAddUserToGame(){
        //ServerUser user = new ServerUser("serverusertest0");
        //serverGameController.addUserToGame();
    }
}
