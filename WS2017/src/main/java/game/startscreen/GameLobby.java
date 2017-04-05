package game.startscreen;

import client.ClientUser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The overview of the users who joined the game.
 *
 * Created by flavia on 01.04.17.
 */
public class GameLobby extends JPanel implements ActionListener {
	
	private final ClientGameStartController cgsc;
	
	private JFrame window;
	private JButton leaveGameButton = new JButton("Leave Game");
	
	
	private DefaultListModel<ClientUser> choosingUsersModel;
	private JList<ClientUser> choosingUsersList = new JList<>(choosingUsersModel);
	private DefaultListModel<ClientUser> waitingUsersModel;
	private JList<ClientUser> waitingUsersList = new JList<>(waitingUsersModel);
	
	/**
	 * Creates a new GameLobby of the specified game.
	 */
	public GameLobby(ClientGameStartController cgsc) {
		
		this.cgsc = cgsc;
		
		window = new JFrame(cgsc.getGameName() + " Lobby");
		
		this.setLayout(new BorderLayout(10, 10));
		
		// The left side with the overview of all the users
		Box userOverviewBox = Box.createVerticalBox();
		JScrollPane choosingUsersScroll = new JScrollPane(choosingUsersList);
		JScrollPane waitingUsersScroll = new JScrollPane(waitingUsersList);
		userOverviewBox.add(new JLabel("Users choosing"));
		userOverviewBox.add(choosingUsersScroll);
		userOverviewBox.add(new JLabel("Users ready"));
		userOverviewBox.add(waitingUsersScroll);
		userOverviewBox.add(leaveGameButton);
		leaveGameButton.addActionListener(this);
		this.add(userOverviewBox, BorderLayout.LINE_START);
		
		// The center with the Chat.
		Box chatBox = Box.createHorizontalBox();
		
		
		window.add(this);
		
		window.pack();
		window.setVisible(true);
	}
	
	/**
	 * Invoked when an action occurs.
	 *
	 * @param e Action Event
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
	
	}
}
