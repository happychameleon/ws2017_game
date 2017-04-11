package game.startscreen;

import client.Client;
import client.ClientUser;
import client.clientgui.LobbyChatPanel;
import game.ClientGameController;
import serverclient.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

/**
 * The overview of the users who joined the game.
 *
 * Created by flavia on 01.04.17.
 */
public class GameLobby extends JPanel implements ActionListener, WindowListener {
	
	private final ClientGameController game;
	
	private JFrame window;
	private JButton leaveGameButton = new JButton("Leave Game");
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
	public GameLobby(ClientGameController game) {
		
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
		
		window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		window.addWindowListener(this);
		
		
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
	
	private void leaveGame() {
		game.askToLeaveGame();
	}
	
	/**
	 * Invoked when an action occurs.
	 *
	 * @param e Action Event
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == leaveGameButton) {
			leaveGame();
		}
	}
	
	
	/**
	 * Invoked the first time a window is made visible.
	 *
	 * @param e
	 */
	@Override
	public void windowOpened(WindowEvent e) {
	
	}
	
	/**
	 * Invoked when the user attempts to close the window
	 * from the window's system menu.
	 *
	 * @param e Window Event
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		leaveGame();
	}
	
	
	
	
	
	/**
	 * Invoked when a window has been closed as the result
	 * of calling dispose on the window.
	 *
	 * @param e The WindowEvent.
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
	 * @param e The WindowEvent.
	 * @see Frame#setIconImage
	 */
	@Override
	public void windowIconified(WindowEvent e) {
	
	}
	
	/**
	 * Invoked when a window is changed from a minimized
	 * to a normal state.
	 *
	 * @param e The WindowEvent.
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
	 * @param e The WindowEvent.
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
	 * @param e The WindowEvent.
	 */
	@Override
	public void windowDeactivated(WindowEvent e) {
	
	}
}
