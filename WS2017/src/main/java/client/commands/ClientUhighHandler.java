package client.commands;


import client.Client;
import game.ClientGameController;

import java.util.HashMap;

/**
 * This class handles the
 * Created by m on 10/04/17.
 */
public class ClientUhighHandler extends ClientCommandHandler {
	
    @Override
    public void handleCommand() {
    	if (Client.isLoggedIn() == false)
    		return;
     
    	String gameName = getAndRemoveNextArgumentWord();
	    ClientGameController endedGame = Client.getGameByName(gameName);
	    
	    String winningTeamName = getAndRemoveNextArgumentWord();
	
	    HashMap<String, Integer> playerScore = new HashMap<>();
	    while (argument.isEmpty() == false) {
	    	String name = getAndRemoveNextArgumentWord();
	    	int score = Integer.parseInt(getAndRemoveNextArgumentWord());
	    	playerScore.put(name, score);
	    }
	    
		endedGame.endGame(playerScore, winningTeamName);
    }
	
	
    
    
	@Override
	public void handleAnswer(boolean isOK) {
	
	}
	
}
