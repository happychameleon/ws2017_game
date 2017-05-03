package server.parser;

import game.ServerGameController;
import game.engine.Player;
import server.Server;
import server.ServerUser;

/**
 * Created by flavia on 27.04.17.
 */
public class WtchgHandler extends CommandHandler {
	/**
	 * Executes the command from the client.
	 */
	@Override
	public void handleCommand() {
		String gameName = getAndRemoveNextArgumentWord();
		String username = getAndRemoveNextArgumentWord();
		
		ServerGameController game = Server.getGameByName(gameName);
		ServerUser user = Server.getUserByName(username);
		
		assert user != null;
		assert game != null;
		
		game.addWatchingUser(user);
		
		sendAnswer(game);
	}
	
	/**
	 * Sends an answer to the user telling the user about the current gamestate.
	 * @param game The game the user joined.
	 */
	private void sendAnswer(ServerGameController game) {
		String gameName = game.getGameName();
		
		// TODO: send character positions, weapons and wetness.
		String playerStrings = "";
		for (Player player : game.getWorld().getTurnController().getPlayers()) {
			String singlePlayerString = game.getAllChildrenAsString(player);
			playerStrings += singlePlayerString + " ";
		}
		playerStrings = playerStrings.trim();
		
		commandParser.writeBackToClient(String.format("+OK wtchg %s %s", gameName, playerStrings));
		
	}
	
	
}
