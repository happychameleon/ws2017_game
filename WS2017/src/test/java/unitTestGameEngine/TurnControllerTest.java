package unitTestGameEngine;

import game.ClientGameController;
import game.GameMap;
import game.GameState;
import game.engine.World;
import org.junit.Test;
import serverclient.User;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by m on 02/05/17.
 */
public class TurnControllerTest {
    @Test
    public void getCurrentPlayer() throws Exception {
        char[][] tiles = {{'1','g','g','2'},{'g','w','w','g'},{'g','w','w','g'},{'3','g','g','4'}};
        int startingPoints = 5;
        String gameName = "game1";
        GameState gameState = GameState.STARTING;
        User aUser = new User("max");
        HashMap<User, String> users = new HashMap<>();
        users.put(new User("blab"), "sadf");
        users.put(new User("ab"), "sdf");
        users.put(aUser, "asdf");
        GameMap map = new GameMap(gameName, tiles);
        ArrayList<User> user = new ArrayList<>();
        ArrayList<String> characterString = new ArrayList<>();
        ClientGameController gameController = new ClientGameController(gameState, startingPoints, gameName, users, map);
        World gameWorld = new World(map, gameController, characterString);
        //TurnController(user, world);
    }

    @Test
    public void endTurn() throws Exception {
    }

    @Test
    public void askServerToEndTurn() throws Exception {
    }

    @Test
    public void removePlayer() throws Exception {
    }

}