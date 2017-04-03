package server.parser;

import game.startscreen.ServerGameStartController;
import server.Server;
import serverclient.User;

/**
 * Informs the requesting client about all the currently existing games.
 *
 * Created by flavia on 02.04.17.
 */
public class CgetgHandler extends CommandHandler {
	
	@Override
	public void handleCommand() {
		if (argument.isEmpty() == false) {
			System.err.println("cgetg should not have an argument!");
		}
		
		for (ServerGameStartController sgsc : Server.getAllWaitingGames()) {
			sendWaitingGameAnswer(sgsc);
		}
		
	}
	
	/**
	 * Sends the waiting game info back as
	 * +OK cgetg waiting <gameName> <maxPoints> <username1> (ready [<characterstring>]|choosing) <username2> (ready [<characterstring>]|choosing)
	 */
	private void sendWaitingGameAnswer(ServerGameStartController sgsc) {
		
		String answer = "+OK cgetg ";
		answer += "waiting ";
		answer += sgsc.getGameName() + " " + sgsc.getStartingPoints();
		// Add choosing user as <username1> choosing
		for (User user : sgsc.getAllChoosingUsers()) {
			answer += " ";
			answer += user.getName();
			answer += " choosing";
		}
		// Add waiting user as <username1> ready [<characterstring>]
		for (User user : sgsc.getAllWaitingUsers().keySet()) {
			answer += " ";
			answer += user.getName();
			answer += " ready ";
			answer += sgsc.getAllWaitingUsers().get(user);
		}
		
		commandParser.writeBackToClient(answer);
		
	}
	
}
