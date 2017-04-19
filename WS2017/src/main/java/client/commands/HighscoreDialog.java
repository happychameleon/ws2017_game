package client.commands;


import client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by flavia on 17.04.17.
 */
public class HighscoreDialog extends Dialog implements WindowListener {
	
	/**
	 * Displays a Dialog window informing the user about the highscores.
	 * @param higschoreTeams List of HashMaps mapping each playername with their highscore. The Teamname is stored with a negative score.
	 */
	public HighscoreDialog(ArrayList<HashMap<String, Integer>> higschoreTeams) {
		super(Client.getMainWindow().getMainFrame());
		
		String highscoreInfoString = "";
		
		for (HashMap<String, Integer> playerScoreMap : higschoreTeams) {
			
			String winningTeamName = "";
			String playerScoreString = "";
			
			for (String playerName : playerScoreMap.keySet()) {
				int score = playerScoreMap.get(playerName);
				if (score == -1) { // This is the teamname!
					winningTeamName = playerName;
					continue;
				}
				playerScoreString += playerName + ": ";
				playerScoreString += playerScoreMap.get(playerName) + "\n";
			}
			
			highscoreInfoString += "Winning Team: " + winningTeamName + "\n";
			highscoreInfoString += "Winning Players:\n" + playerScoreString;
			highscoreInfoString += "\n\n";
		}
		
		JTextArea textArea = new JTextArea();
		textArea.setText(highscoreInfoString);
		textArea.setEditable(false);
		this.add(textArea);
		
		setTitle("Highscores");
		setMinimumSize(new Dimension(200, 0));
		
		pack();
		
		setVisible(true);
		
		addWindowListener(this);
		
	}
	
	/**
	 * Invoked the first time a window is made visible.
	 *
	 * @param e The Window Event
	 */
	@Override
	public void windowOpened(WindowEvent e) {
	
	}
	
	/**
	 * Invoked when the user attempts to close the window
	 * from the window's system menu.
	 *
	 * @param e The Window Event
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		dispose();
	}
	
	/**
	 * Invoked when a window has been closed as the result
	 * of calling dispose on the window.
	 *
	 * @param e The Window Event
	 */
	@Override
	public void windowClosed(WindowEvent e) {
	
	}
	
	/**
	 * Invoked when a window is changed from a normal to a
	 * minimized state. For many platforms, a minimized window
	 * is displayed as the icon specified in the window's
	 * iconImage property.
	 *
	 * @param e The Window Event
	 * @see Frame#setIconImage
	 */
	@Override
	public void windowIconified(WindowEvent e) {
	
	}
	
	/**
	 * Invoked when a window is changed from a minimized
	 * to a normal state.
	 *
	 * @param e The Window Event
	 */
	@Override
	public void windowDeiconified(WindowEvent e) {
	
	}
	
	/**
	 * Invoked when the Window is set to be the active Window. Only a Frame or
	 * a Dialog can be the active Window. The native windowing system may
	 * denote the active Window or its children with special decorations, such
	 * as a highlighted title bar. The active Window is always either the
	 * focused Window, or the first Frame or Dialog that is an owner of the
	 * focused Window.
	 *
	 * @param e The Window Event
	 */
	@Override
	public void windowActivated(WindowEvent e) {
	
	}
	
	/**
	 * Invoked when a Window is no longer the active Window. Only a Frame or a
	 * Dialog can be the active Window. The native windowing system may denote
	 * the active Window or its children with special decorations, such as a
	 * highlighted title bar. The active Window is always either the focused
	 * Window, or the first Frame or Dialog that is an owner of the focused
	 * Window.
	 *
	 * @param e The Window Event
	 */
	@Override
	public void windowDeactivated(WindowEvent e) {
	
	}
}
