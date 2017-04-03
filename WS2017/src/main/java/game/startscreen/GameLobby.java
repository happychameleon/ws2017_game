package game.startscreen;

import serverclient.User;

import javax.swing.*;

/**
 * The overview of the users who joined the game.
 * TODO
 * Created by flavia on 01.04.17.
 */
public class GameLobby extends JPanel {

	private JFrame window;
	private JButton leaveGameButton = new JButton("Leave Game");
	
	private JList<User> choosingUsers;
	private JList<User> waitingUsers;
	
	

}
