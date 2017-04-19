package client.commands;

import client.ClientCommandParser;
import game.GameController;
import game.engine.Tile;

/**
 * This is the base class for classes which handle the command and the answer for a specific command
 *
 * Created by flavia on 26.03.17.
 */
public abstract class ClientCommandHandler {
	
	/**
	 * The argument of this Command/Answer
	 */
	protected String argument;
	
	public void setArgument(String argument) {
		this.argument = argument;
	}
	
	/**
	 * The commandParser from this command.
	 */
	protected ClientCommandParser commandParser;
	
	/**
	 * @param commandParser {@link #commandParser}
	 */
	public void setCommandParser(ClientCommandParser commandParser) {
		this.commandParser = commandParser;
	}
	
	
	/**
	 * Executes the command from the server.
	 */
	public abstract void handleCommand();
	
	/**
	 * Executes the answer received from the server to the specific command this class is for.
	 * @param isOK <code>true</code> if the answer started with "+OK", <code>false</code> otherwise (answer started with "-ERR").
	 */
	public abstract void handleAnswer(boolean isOK);
	
	/**
	 * Takes the next Word (substring from 0 to indexOf(" ")), removes it from the string and returns it.
	 * @return the next Word from the argument.
	 */
	protected String getAndRemoveNextArgumentWord() {
		if (argument.contains(" ")) {
			String firstWord = argument.substring(0, argument.indexOf(" "));
			argument = argument.substring(argument.indexOf(" ") + 1);
			return firstWord;
		} else if (argument.isEmpty()) {
			return "";
		} else {
			String lastWord = argument;
			argument = "";
			return lastWord;
		}
	}
	
	/**
	 * Reads the position from the positionString as 'x,y'
	 * @param positionString the positionString formatted as x,y
	 * @param gameController the gameController in which the Tile is in.
	 * @return The correct Tile.
	 */
	protected Tile parsePosition(String positionString, GameController gameController) {
		
		String xString = "";
		String yString = "";
		int i = 0;
		for (char c : positionString.toCharArray()) {
			if (i == 0)
				if (c == ',') i++;
				else xString += c;
			else
				yString += c;
		}
		int x = Integer.parseInt(xString);
		int y = Integer.parseInt(yString);
		System.out.println("ClientCommandHandler#parsePosition - x: " + x + " y: " + y);
		return gameController.getWorld().getTileAt(x, y);
	}
	
}
