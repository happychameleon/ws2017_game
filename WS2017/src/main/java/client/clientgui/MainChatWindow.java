package client.clientgui;

import client.Client;
import client.ClientUser;
import game.ClientGameController;
import game.startscreen.ClientGameStartController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The general Chat window where all users can chat with each other and name changes can be requested.
 * TODO Meilenstein 3: In future milestones there will also be the possibility to start a game with selected people and to privately chat
 * TODO: Separate this Chat Class into two classes, one for only the main MainChatWindow stuff and one for the whole window and keeping track of the game.
 */
public class MainChatWindow implements ActionListener, KeyListener, MouseListener {
	
	
	/**
	 * Contains the different tabs.
	 */
	private JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	
	/**
	 * The Panel where the everything is in.
	 */
	JPanel mainPanel;
	
	/**
	 * The main window
	 */
	JFrame mainFrame = new JFrame("Chat");
	
	/**
	 * The input to change the username.
	 */
	JTextField usernameChangeInput = new JTextField(15);
	
	/**
	 * The button to send the new username in {@link #usernameChangeInput} to the server.
	 */
	JButton usernameChangeButton = new JButton("Change Username");
	
	
	private ChatPanel chatPanel;
	
	public ChatPanel getChatPanel() {
		return chatPanel;
	}
	
	DefaultListModel<ClientGameStartController> openGameListModel = new DefaultListModel<>();
	/**
	 * All the currently open games where this client can join.
	 */
	JList<ClientGameStartController> openGameList = new JList<>(openGameListModel);
	
	/**
	 * Join the selected game from the {@link #openGameList}.
	 */
	JButton joinGameButton = new JButton("Join Game");
	
	
	DefaultListModel<ClientGameController> runningGameListModel = new DefaultListModel<>();
	/**
	 * All the currently running games where this client can watch, but not join anymore.
	 */
	JList<ClientGameController> runningGameList = new JList<>(runningGameListModel);
	
	/**
	 * This Button is used to open a window for the selected {@link #runningGameList} (where this client isn't a player) to watch it.
	 */
	JButton watchGameButton = new JButton("Watch Game (TODO)");
	
	/**
	 * Opens the {@link NewGameDialog} to create a new Game.
	 */
	JButton newGameButton = new JButton("Create Game");
	
	/**
	 * The Dialog Window for creating a new game.
	 */
	NewGameDialog newGameDialog;
	
	
	DefaultListModel<ClientUser> userListModel = new DefaultListModel<>();
	/**
	 * A list of all the logged in users.
	 */
	JList<ClientUser> userList = new JList<>(userListModel);
	
	/**
	 * Opens a chat with the selected user from {@link #userList}.
	 */
	JButton whisperButton = new JButton("Open Chat");
	
	
	/**
	 * Opens the main Chat window.
	 */
	public MainChatWindow() {
		
		mainFrame.setTitle("Username: " + Client.getThisUser().getName());
		
		frame();
	}
	
	/**
	 * Opens and displays the MainChatWindow Window.
	 */
	public void frame() {
		// modify JFrame component layout
		mainFrame.setSize(1000, 700);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		usernameChangeInput.setFont(new Font("Courier New", Font.ITALIC, 30));
		//sendChatButton.setPreferredSize(new Dimension(100, 50));
		//usernameChangeButton.setPreferredSize(new Dimension(200,50));
		
		
		// Create main Panel for the chat
		mainPanel = new JPanel(new BorderLayout(20, 10));
		
		// The middle contains the MainChatWindow Panel
		chatPanel = new ChatPanel(Client.getAllUsers());
		chatPanel.setPreferredSize(new Dimension(500, 700));
		mainPanel.add(chatPanel, BorderLayout.CENTER);
		
		// The left panel with the game selection and new game creation.
		Box gameCreationBox = Box.createVerticalBox();
		gameCreationBox.add(newGameButton);
		gameCreationBox.add(new JLabel("Open Games:"));
		JScrollPane openGameListScroller = new JScrollPane(openGameList);
		gameCreationBox.add(openGameListScroller);
		gameCreationBox.add(joinGameButton);
		gameCreationBox.add(new JLabel("Running Games:"));
		JScrollPane runningGameListScroller = new JScrollPane(runningGameList);
		gameCreationBox.add(runningGameListScroller);
		gameCreationBox.add(watchGameButton);
		mainPanel.add(gameCreationBox, BorderLayout.LINE_START);
		
		// The right panel with the user list and the ability to create whisper chats
		Box userOverviewBox = Box.createVerticalBox();
		JScrollPane userListScroller = new JScrollPane(userList);
		userOverviewBox.add(userListScroller);
		userOverviewBox.add(whisperButton);
		mainPanel.add(userOverviewBox, BorderLayout.LINE_END);
		
		// Username change at the bottom (TODO Better place for this)
		JPanel usernameChangePanel = new JPanel(new GridLayout(1,2));
		usernameChangePanel.add(usernameChangeInput);
		usernameChangePanel.add(usernameChangeButton);
		mainPanel.add(usernameChangePanel, BorderLayout.PAGE_END);
		
		
		// Add the main Panel as a Tab to the tabbedPanel.
		tabbedPane.addTab("Main Chat", mainPanel);
		// Add the tabbedPanel to the main frame
		mainFrame.add(tabbedPane);
		
		
		
		// Non-Layout specific configurations for the ui elements
		
		usernameChangeInput.addKeyListener(this);
		usernameChangeInput.setFocusable(true);
		
		newGameButton.addActionListener(this);
		joinGameButton.addActionListener(this);
		openGameList.addMouseListener(this);
		watchGameButton.addActionListener(this);
		runningGameList.addMouseListener(this);
		
		openGameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		openGameList.setLayoutOrientation(JList.VERTICAL);
		runningGameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		runningGameList.setLayoutOrientation(JList.VERTICAL);
		
		userList.addMouseListener(this);
		whisperButton.addActionListener(this);
		
		mainFrame.setVisible(true);
		
		usernameChangeButton.addActionListener(this);
		
	}
	
	
	/**
	 * Sets the Title of the Main Window.
	 *
	 * @param newTitle the new Title for the Main Window.
	 */
	public void setTitle(String newTitle) {
		mainFrame.setTitle(newTitle);
	}
	
	/**
	 * Takes the proposed username and puts it in the {@link #usernameChangeInput}. It's called when the chosen username is already taken.
	 *
	 * @param proposedUsername the proposed username (with a number at the end)
	 */
	public void proposeUsername(String proposedUsername) {
		usernameChangeInput.setText(proposedUsername);
	}
	
	
	
	public void addNewGameToList(ClientGameStartController cgsc) {
		openGameListModel.addElement(cgsc);
	}
	
	public void addRunningGameToList(ClientGameController cgc) {
		runningGameListModel.addElement(cgc);
	}
	
	public void removeGameFromList(ClientGameStartController cgsc) {
		openGameListModel.removeElement(cgsc);
		chatPanel.displayInfo("The game " + cgsc.getGameName() + " has been removed, because there were no players left.");
	}
	
	/**
	 * Gets the Game with the specified name from the List of games.
	 *
	 * @return The ClientGameStartController or null if the name doesn't exist.
	 */
	public ClientGameStartController getWaitingGameByName(String gameName) {
		for (int i = 0; i < openGameListModel.getSize(); i++) {
			if (openGameListModel.get(i).getGameName().equals(gameName)) {
				return openGameListModel.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Invoked when an action occurs.
	 *
	 * @param e The ActionEvent
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == usernameChangeButton) {
			sendChangeUsernameRequest();
			
		} else if (e.getSource() == newGameButton) {
			openNewGameInputWindow();
			
		} else if (e.getSource() == joinGameButton) {
			joinGameAtIndex(openGameList.getSelectedIndex());
			
		} else if (e.getSource() == runningGameList) {
			watchGameAtIndex(runningGameList.getSelectedIndex());
			
		} else if (e.getSource() == whisperButton) {
			openWhisperChat(userList.getSelectedIndex());
		}
	}
	
	/**
	 * Invoked when a key has been pressed.
	 * See the class description for {@link KeyEvent} for a definition of
	 * a key pressed event.
	 *
	 * @param e The KeyEvent.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				if (usernameChangeInput.isFocusOwner()) {
					sendChangeUsernameRequest();
				}
				break;
			default:
				break;
		}
	}
	
	/**
	 * Invoked when the mouse button has been clicked (pressed
	 * and released) on a component.
	 *
	 * @param e The MouseEvent
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == userList) {
			if (e.getClickCount() == 2) {
				int index = userList.locationToIndex(e.getPoint());
				openWhisperChat(index);
			}
			
		} else if (e.getSource() == openGameList) {
			if (e.getClickCount() == 2) {
				int index = openGameList.locationToIndex(e.getPoint());
				joinGameAtIndex(index);
			}
			
		} else if (e.getSource() == runningGameList) {
			if (e.getClickCount() == 2) {
				int index = runningGameList.locationToIndex(e.getPoint());
				watchGameAtIndex(index);
			}
			
		}
	}
	
	
	/**
	 * Opens the {@link #newGameDialog}.
	 */
	private void openNewGameInputWindow() {
		newGameDialog = new NewGameDialog(mainFrame);
	}
	
	/**
	 * Calls the {@link ClientGameStartController#joinGame()} method of the game at the given position of the {@link #openGameList}.
	 * @param index the given position at the openGameList.
	 */
	private void joinGameAtIndex(int index) {
		ClientGameStartController cgsc = openGameListModel.elementAt(index);
		if (cgsc != null)
			cgsc.joinGame();
	}
	
	
	/**
	 * Calls the {@link ClientGameController#watchGame()} method of the game at the given position of the {@link #runningGameList}.
	 * @param index the given position at the runningGameList.
	 */
	private void watchGameAtIndex(int index) {
		ClientGameController cgc = runningGameListModel.elementAt(index);
		if (cgc != null)
			cgc.watchGame();
	}
	
	
	
	/**
	 * Sends a message to the server requesting to change the username.
	 */
	private void sendChangeUsernameRequest() {
		String username = usernameChangeInput.getText();
		if (username.isEmpty() || username.contains(" ") || username.contains("'"))
			return;
		Client.sendMessageToServer("uname " + username);
		usernameChangeInput.setText("");
	}
	
	/**
	 * Opens a whisper chat tab with the selected user from {@link #userList}.
	 *
	 * @param index The index from the userList where the desired user is at.
	 */
	private void openWhisperChat(int index) {
		ClientUser user = userListModel.elementAt(index);
		// CHeck if there is already a tab open with that user.
		for (int i = 0; i < tabbedPane.getTabCount(); i++) {
			if (tabbedPane.getTitleAt(i).equals(user.getName())) {
				chatPanel.displayInfo("You already have a chat with that user");
				return;
			}
		}
		WhisperTab whisperTab = new WhisperTab(user);
		tabbedPane.addTab(user.getName(), whisperTab);
	}
	
	/**
	 * Adds the user to the list when they logged in and to the main chat.
	 *
	 * @param user the new user.
	 */
	public void addUserToUserlist(ClientUser user) {
		if (user == Client.getThisUser())
			System.err.println("MainChatWindow#addUserToUserlist - trying to add this user to the list!");
		userListModel.addElement(user);
		chatPanel.addChatUser(user);
	}
	
	/**
	 * Removes the user from the list and the chat, when they logged of.
	 *
	 * @param user the user to remove.
	 */
	public void removeUserFromUserlist(ClientUser user) {
		userListModel.removeElement(user);
		chatPanel.removeChatUser(user);
	}
	
	/**
	 * Renames the user in the user list and the tab of the user.
	 *
	 * @param oldName The users old name.
	 * @param newName The users new name.
	 * @param user The user.
	 */
	public void renamedUser(String oldName, String newName, ClientUser user) {
		
		// Update userList
		int userListIndex = userListModel.indexOf(user);
		userListModel.set(userListIndex, user);
		
		// Update tabbedPane
		boolean userFound = false;
		for (int i = 0; i < tabbedPane.getTabCount(); i++) {
			if (tabbedPane.getTitleAt(i).equals(oldName)) {
				tabbedPane.setTitleAt(i, newName);
				userFound = true;
			}
		}
		if (userFound == false) {
			System.err.println("MainChatWindow#renamedUser - No user found with oldName: " + oldName);
		}
		
		chatPanel.displayInfo( oldName + " changed their name to " + newName );
	}
	
	//region unused interface methods
	@Override
	public void keyTyped(KeyEvent e) {
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
	}
	//endregion
}

