package game.startscreen;

import client.ClientUser;
import client.clientgui.LobbyChatPanel;
import game.GameController;
import serverclient.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * The overview of the users who joined the game.
 *
 * Created by flavia on 01.04.17.
 */
public class GameLobby extends JPanel implements ActionListener {
	
	private final GameController game;
	
	private JFrame window;
	private JButton leaveGameButton = new JButton("Leave Game (TODO)");
	
	
	private DefaultListModel<ClientUser> choosingUsersModel = new DefaultListModel<>();
	private JList<ClientUser> choosingUsersList = new JList<>(choosingUsersModel);
	private DefaultListModel<ClientUser> waitingUsersModel = new DefaultListModel<>();
	private JList<ClientUser> waitingUsersList = new JList<>(waitingUsersModel);
	
	/**
	 * The Chat panel of this lobby
	 */
	private LobbyChatPanel lobbyChat;
	
	/**
	 * @return {@link #lobbyChat}.
	 */
	public LobbyChatPanel getLobbyChat() {
		return lobbyChat;
	}
	
	/**
	 * Creates a new GameLobby of the specified game.
	 */
	public GameLobby(GameController game) {
		
		this.game = game;
		
		window = new JFrame(game.getGameName() + " Lobby");
		
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
		ArrayList<ClientUser> users = new ArrayList<>();
		for (User user : game.getAllUsers()) {
			users.add((ClientUser) user);
		}
		lobbyChat = new LobbyChatPanel(users, "chatl", game);
		chatBox.add(lobbyChat);
		this.add(chatBox);
		
		
		window.add(this);
		
		window.pack();
		window.setVisible(true);
	}
	
	
	/**
	 * Removes the user from all the list of users
	 * @param user the user to remove
	 */
	public void removeUser(ClientUser user) {
		choosingUsersModel.removeElement(user);
		waitingUsersModel.removeElement(user);
	}
	
	/**
	 * Adds the user to the waiting users list.
	 * @param user the user to add.
	 */
	public void addUserToWaiting(ClientUser user) {
		waitingUsersModel.addElement(user);
	}
	
	/**
	 * Adds the user to the choosing users list.
	 * @param user the user to add.
	 */
	public void addUserToChoosing(ClientUser user) {
		choosingUsersModel.addElement(user);
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
