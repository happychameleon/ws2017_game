package server.parser;

import game.ServerGameController;
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
		
		for (ServerGameController sgc : Server.getStartingGames()) {
			sendWaitingGameAnswer(sgc);
		}
		
		for (ServerGameController sgc : Server.getRunningGames()) {
			sendRunningGameAnswer(sgc);
		}
		
	}
	
	/**
	 * Sends the waiting game info back as
	 * +OK cgetg waiting <gameName> <maxPoints> <username1> (ready [<characterstring>]|choosing) <username2> (ready [<characterstring>]|choosing)
	 */
	private void sendWaitingGameAnswer(ServerGameController sgc) {
		
		String users = "";
		// Add choosing user as <username1> choosing
		for (User user : sgc.getAllChoosingUsers()) {
			users += " ";
			users += user.getName();
			users += " choosing";
		}
		// Add waiting user as <username1> ready [<characterstring>]
		for (User user : sgc.getAllWaitingUsers().keySet()) {
			users += " ";
			users += user.getName();
			users += " ready ";
			users += sgc.getAllWaitingUsers().get(user);
		}
		
		commandParser.writeBackToClient(String.format("+OK cgetg waiting %s %d %s", sgc.getGameName(), sgc.getStartingPoints(), users));
		
	}
	
	/**
	 * Sends the running game info back as
	 * +OK cgetg running <gameName> <maxPoints> <username1> <username2>
	 */
	private void sendRunningGameAnswer(ServerGameController sgc) {
		//TODO send the running game answer.
	}
	
}
