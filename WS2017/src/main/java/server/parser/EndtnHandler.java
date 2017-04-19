package server.parser;

import game.ServerGameController;
import game.engine.TurnController;
import server.Server;
import server.ServerUser;

/**
 * Created by flavia on 15.04.17.
 */
public class EndtnHandler extends CommandHandler {
	/**
	 * Executes the command from the client.
	 */
	@Override
	public void handleCommand() {
		String wholeArgument = argument;
		
		String gameName = getAndRemoveNextArgumentWord();
		ServerGameController gameController = Server.getGameByName(gameName);
		if (gameController == null) {
			System.err.println("EndtnHandler#handleCommand - gameName doesn't exist!");
			return;
		}
		TurnController turnController = gameController.getWorld().getTurnController();
		
		String username = getAndRemoveNextArgumentWord();
		ServerUser user = Server.getUserByName(username);
		if (user == null) {
			System.err.println("EndtnHandler#handleCommand - username doesn't exist!");
			return;
		}
		
		if (turnController.getCurrentPlayer().getUser() != user) {
			Server.writeToAllClients(String.format("-ERR endtn %s its not your turn", gameName));
			return;
		}
		
		turnController.endTurn();
		
		Server.writeToAllClients(String.format("+OK endtn %s", wholeArgument));
	}
}
