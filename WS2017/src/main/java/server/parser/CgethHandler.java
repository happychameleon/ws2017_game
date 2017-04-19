package server.parser;

import java.io.*;

/**
 * Gets all the highscores from the highscore File and sends them to the requesting Client.
 *
 * Created by flavia on 17.04.17.
 */
public class CgethHandler extends CommandHandler {
	
	/**
	 * Executes the command from the client.
	 */
	@Override
	public void handleCommand() {
		File highscoreFile = new File("./highscore.txt");
		
		try {
			if (highscoreFile.exists() == false) {
				writeNoHighscoresYetMessage();
				return;
			}
			// Stores all lines in the highscore file as one String. Every new Line is separated with ':
			String highscoreLines = "";
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(highscoreFile)));
			// Read in all the lines.
			while (true) {
				String line = bufferedReader.readLine();
				if (line == null || line.isEmpty()) break;
				if (line.endsWith(" ")) line = line.substring(0, line.length() - 1); // To remove the last space.
				String messageLine = "'" + line + "'";
				highscoreLines += messageLine + " ";
			}
			if (highscoreLines.isEmpty()) {
				writeNoHighscoresYetMessage();
				return;
			}
			highscoreLines = highscoreLines.substring(0, highscoreLines.length() - 1); // For the last space.
			
			commandParser.writeBackToClient(String.format("+OK cgeth %s", highscoreLines));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes the message back for when there is not yet any game with a highscore.
	 */
	private void writeNoHighscoresYetMessage() {
		commandParser.writeBackToClient("-ERR cgeth no highscores yet");
	}
	
}
