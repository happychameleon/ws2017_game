package game.startscreen;

import client.Client;
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
	private JButton startGameButton = new JButton("Start Game (TODO)");
	
	
	
	private DefaultListModel<ClientUser> userListModel = new DefaultListModel<>();
	private JList<ClientUser> userList = new JList<>(userListModel);

	
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
		JScrollPane choosingUsersScroll = new JScrollPane(userList);
		userOverviewBox.add(new JLabel("Players"));
		userOverviewBox.add(choosingUsersScroll);
		JPanel buttons = new JPanel(new GridLayout(1, 2));
		buttons.add(leaveGameButton);
		buttons.add(startGameButton);
		userOverviewBox.add(buttons);
		leaveGameButton.addActionListener(this);
		startGameButton.addActionListener(this);
		this.add(userOverviewBox, BorderLayout.LINE_START);
		
		// The center with the Chat.
		Box chatBox = Box.createHorizontalBox();
		ArrayList<ClientUser> users = new ArrayList<>();
		// Add already existing users to the chat and the user list. This user will be added like a new user therefore is ignored here..
		for (User user : game.getAllUsers()) {
			if (user == Client.getThisUser()) {
				continue;
			}
			users.add((ClientUser) user);
			userListModel.addElement((ClientUser) user);
		}
		lobbyChat = new LobbyChatPanel(users, "chatl", game);
		
		
		chatBox.add(lobbyChat);
		this.add(chatBox);
		
		// Add the Game Lobby to it's window
		window.add(this);
		
		
		
		
		window.pack();
		window.setVisible(true);
	}
	
	
	/**
	 * Removes the user from the list of users
	 * @param user the user to remove
	 */
	public void removeUser(ClientUser user) {
		userListModel.removeElement(user);
		if (user == Client.getThisUser()) {
			window.dispose();
			game.removeGameLobby();
		}
	}
	
	/**
	 * Adds the user to the lobby user list and the chat.
	 * @param user the user to add.
	 */
	public void addUserToLobby(ClientUser user) {
		userListModel.addElement(user);
		lobbyChat.addChatUser(user);
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
