package server.parser;

import game.ServerGameController;
import server.Server;
import serverclient.User;

/**
 * The Client has informed the server that they have joined the game and are now choosing the team.
 * The Server now informs all the clients, that the client has joined this game.
 *
 * Created by flavia on 31.03.17.
 */
public class JoingHandler extends CommandHandler {
	
	
	@Override
	public void handleCommand() {
		String gameName = argument.substring(0, argument.indexOf(" "));
		String username = argument.substring(argument.indexOf(" ") + 1);
		User joinedUser = Server.getUserByName(username);
		ServerGameController game = Server.getGameByName(gameName);
		
		if (joinedUser == null || game == null) {
			System.err.println("USER OR GAME DOESN'T EXIST!");
		}
		
		Server.writeToAllClients(String.format("joing %s", argument));
		
		game.addUserToGame(joinedUser);
	}
}
