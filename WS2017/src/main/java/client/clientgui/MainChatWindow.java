package client.clientgui;

import client.Client;
import client.ClientUser;
import client.commands.ClientCgethHandler;
import game.ClientGameController;
import serverclient.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * The general Chat window where all users can chat with each other and name changes can be requested.
 * It also holds the List of all games on the client side and displays them for the user to join or watch them.
 */
public class MainChatWindow implements ActionListener, KeyListener, MouseListener {
	
	//region Swing Components
	/**
	 * Contains the different tabs.
	 */
	private JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	
	/**
	 * The Panel where everything is in.
	 */
	JPanel mainPanel;
	
	/**
	 * The main window
	 */
	JFrame mainFrame = new JFrame("Chat");
	
	/**
	 * @return {@link #mainFrame}
	 */
	public JFrame getMainFrame() {
		return mainFrame;
	}
	
	/**
	 * The input to change the username.
	 */
	JTextField usernameChangeInput = new JTextField(15);
	
	/**
	 * The button to send the new username in {@link #usernameChangeInput} to the server.
	 */
	JButton usernameChangeButton = new JButton("Change Username");
	
	/**
	 * The ChatPanel where this Client can chat with every logged in users.
	 */
	private ChatPanel mainChatPanel;
	
	/**
	 * @return {@link #mainChatPanel}.
	 */
	public ChatPanel getMainChatPanel() {
		return mainChatPanel;
	}
	
	/**
	 * The model for the {@link #waitingGameList}.
	 */
	DefaultListModel<ClientGameController> waitingGameListModel = new DefaultListModel<>();
	/**
	 * All the currently open games where this client can join.
	 */
	JList<ClientGameController> waitingGameList = new JList<>(waitingGameListModel);
	
	/**
	 * Join the selected game from the {@link #waitingGameList}.
	 */
	JButton joinGameButton = new JButton("Join Game");
	
	/**
	 * The model for the {@link #runningGameList}.
	 */
	DefaultListModel<ClientGameController> runningGameListModel = new DefaultListModel<>();
	/**
	 * All the currently running games where this client can watch, but not join anymore.
	 */
	JList<ClientGameController> runningGameList = new JList<>(runningGameListModel);
	
	/**
	 * This Button is used to open a window for the selected {@link #runningGameList} (where this client isn't a player) to watch it.
	 */
	JButton watchGameButton = new JButton("Watch Game");
	
	/**
	 * Opens the {@link NewGameDialog} to create a new Game.
	 */
	JButton newGameButton = new JButton("Create Game");
	
	/**
	 * The Dialog Window for creating a new game.
	 */
	NewGameDialog newGameDialog;
	
	/**
	 * Used to display the highscores for all the finished game.
	 */
	JButton viewHighscoreButton = new JButton("Show Highscores");
	
	
	/**
	 * The label for displaying thi users username.
	 */
	JLabel usernameLabel = new JLabel("Your username: " + Client.getThisUser().getName());
	
	DefaultListModel<ClientUser> userListModel = new DefaultListModel<>();
	/**
	 * A list of all the logged in users.
	 */
	JList<ClientUser> userList = new JList<>(userListModel);
	
	/**
	 * Opens a chat with the selected user from {@link #userList}.
	 */
	JButton whisperButton = new JButton("Open Chat");
	//endregion
	
	
	/**
	 * All the games stored which have ended, but there's still a lobby open to chat.
	 */
	private HashSet<ClientGameController> endedGames = new HashSet<>();
	
	
	//region Initializing and GUI-Layout
	/**
	 * Opens the main Chat window.
	 */
	public MainChatWindow() {
		
		mainFrame.setTitle("Wasserschlacht Simulator 2017");
		
		frame();
	}
	
	/**
	 * Opens and displays the MainChatWindow Window.
	 */
	public void frame() {
		// modify JFrame component layout
		mainFrame.setSize(1000, 700);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//usernameChangeInput.setFont(new Font("Courier New", Font.ITALIC, 30));
		//sendChatButton.setPreferredSize(new Dimension(100, 50));
		//usernameChangeButton.setPreferredSize(new Dimension(200,50));
		
		
		// Create main Panel for the chat
		mainPanel = new JPanel(new BorderLayout(20, 10));
		
		// The middle contains the tabbedPane with the Main Chat and future Whisper Chats
		mainChatPanel = new ChatPanel(Client.getAllUsers(), "chatm");
		mainChatPanel.setPreferredSize(new Dimension(500, 700));
		tabbedPane.addTab("Main Chat", mainChatPanel);
		JPanel middlePanel = new JPanel(new BorderLayout(20, 10));
		middlePanel.add(tabbedPane, BorderLayout.CENTER);
		mainPanel.add(middlePanel, BorderLayout.CENTER);
		
		// The left panel with the game selection and new game creation.
		Box gameCreationBox = Box.createVerticalBox();
		gameCreationBox.add(newGameButton);
		gameCreationBox.add(new JLabel("New Games:"));
		JScrollPane openGameListScroller = new JScrollPane(waitingGameList);
		gameCreationBox.add(openGameListScroller);
		gameCreationBox.add(joinGameButton);
		gameCreationBox.add(new JLabel("Games already playing:"));
		JScrollPane runningGameListScroller = new JScrollPane(runningGameList);
		gameCreationBox.add(runningGameListScroller);
		gameCreationBox.add(watchGameButton);
		gameCreationBox.add(viewHighscoreButton);
		mainPanel.add(gameCreationBox, BorderLayout.LINE_START);
		
		// The right panel with the user list and the ability to create whisper chats
		Box userOverviewBox = Box.createVerticalBox();
		userOverviewBox.add(usernameLabel);
		JScrollPane userListScroller = new JScrollPane(userList);
		userOverviewBox.add(userListScroller);
		userOverviewBox.add(whisperButton);
		mainPanel.add(userOverviewBox, BorderLayout.LINE_END);
		
		// Username change at the bottom
		JPanel usernameChangePanel = new JPanel(new GridLayout(1,2));
		usernameChangePanel.setPreferredSize(new Dimension(200, 50));
		usernameChangePanel.add(usernameChangeInput);
		usernameChangePanel.add(usernameChangeButton);
		middlePanel.add(usernameChangePanel, BorderLayout.PAGE_END);
		
		// Add the mainPanel to the main frame
		mainFrame.add(mainPanel);
		
		
		// Non-Layout specific configurations for the ui elements
		
		usernameChangeInput.addKeyListener(this);
		usernameChangeInput.setFocusable(true);
		
		newGameButton.addActionListener(this);
		joinGameButton.addActionListener(this);
		waitingGameList.addMouseListener(this);
		watchGameButton.addActionListener(this);
		runningGameList.addMouseListener(this);
		viewHighscoreButton.addActionListener(this);
		
		waitingGameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		waitingGameList.setLayoutOrientation(JList.VERTICAL);
		runningGameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		runningGameList.setLayoutOrientation(JList.VERTICAL);
		
		userList.addMouseListener(this);
		whisperButton.addActionListener(this);
		
		mainFrame.setVisible(true);
		
		usernameChangeButton.addActionListener(this);
		
	}
	//endregion
	
	
	//region Methods
	//region GameList Methods
	/**
	 * Adds a new game to the list of games at the appropriate place according to tha game's state.
	 * @param game the game to add.
	 */
	public void addGameToList(ClientGameController game) {
		switch (game.getGameState()) {
			case STARTING:
				waitingGameListModel.addElement(game);
				break;
			
			case RUNNING:
				runningGameListModel.addElement(game);
				break;
			
			case FINISHED:
				endedGames.add(game);
				break;
		}
	}
	
	
	/**
	 * Removes a deleted game.
	 * @param game the deleted game.
	 */
	public void removeGameFromList(ClientGameController game) {
		switch (game.getGameState()) {
			case STARTING:
				waitingGameListModel.removeElement(game);
				break;
			
			case RUNNING:
				runningGameListModel.removeElement(game);
				break;
			
			case FINISHED:
				endedGames.remove(game);
				break;
		}
		
		if (game.getAllUsers() == null || game.getAllUsers().size() == 0) {
			mainChatPanel.displayInfo("The game " + game.getGameName() + " has been removed, because there were no players left.");
		}
	}
	
	/**
	 * Gets the Game with the specified name from the List of open games.
	 * @param gameName The name of the game.
	 * @return The ClientGameController or null if the name doesn't exist.
	 */
	public ClientGameController getWaitingGameByName(String gameName) {
		if (waitingGameListModel.isEmpty()) return null;
		for (int i = 0; i < waitingGameListModel.getSize(); i++) {
			if (waitingGameListModel.get(i).getGameName().equals(gameName)) {
				return waitingGameListModel.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Gets the Game with the specified name from the List of running games.
	 * @param gameName The name of the game.
	 * @return The ClientGameController or null if the name doesn't exist.
	 */
	public ClientGameController getRunningGameByName(String gameName) {
		if (runningGameListModel.isEmpty()) return null;
		for (int i = 0; i < runningGameListModel.getSize(); i++) {
			if (runningGameListModel.get(i).getGameName().equals(gameName)) {
				return runningGameListModel.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Gets the Game with the specified name from {@link #endedGames}.
	 * @param gameName The name of the game.
	 * @return The ClientGameController or null if the name doesn't exist.
	 */
	public ClientGameController getEndedGameByName(String gameName) {
		if (endedGames.isEmpty()) return null;
		for (ClientGameController gameController : endedGames) {
			if (gameController.getGameName().equals(gameName)) {
				return gameController;
			}
		}
		return null;
	}
	
	/**
	 * Gets the specified game.
	 * @param gameName the given name.
	 * @return the game with the given name if it exists, otherwise null.
	 */
	public ClientGameController getGameByName(String gameName) {
		if (getRunningGameByName(gameName) != null)
			return getRunningGameByName(gameName);
		if (getWaitingGameByName(gameName) != null)
			return getWaitingGameByName(gameName);
		if (getEndedGameByName(gameName) != null)
			return getEndedGameByName(gameName);
		return null;
	}
	
	/**
	 * Opens the {@link #newGameDialog}.
	 */
	private void openNewGameInputWindow() {
		newGameDialog = new NewGameDialog(mainFrame);
	}
	
	/**
	 * Calls the {@link ClientGameController#askToJoinGame()} ()} method of the game at the given position of the {@link #waitingGameList}.
	 * @param index the given position at the waitingGameList.
	 */
	private void joinGameAtIndex(int index) {
		ClientGameController game = waitingGameListModel.elementAt(index);
		if (game != null)
			game.askToJoinGame();
	}
	
	/**
	 * Moves the game from the {@link #waitingGameList} to the {@link #runningGameList}.
	 * @param gameController The game to move
	 */
	public void moveGameToRunning(ClientGameController gameController) {
		if (waitingGameListModel.removeElement(gameController) == false) {
			if (runningGameListModel.contains(gameController)) {
				return;
			} else {
				System.err.println("MainChatWindow#moveGameToRunning - Why isn't the game " + gameController.getGameName() + " already in running game list?");
			}
		}
		runningGameListModel.addElement(gameController);
	}
	
	/**
	 * Calls the {@link ClientGameController#askToWatchGame()} method of the game at the given position of the {@link #runningGameList}.
	 * @param index the given position at the runningGameList.
	 */
	private void watchGameAtIndex(int index) {
		ClientGameController game = runningGameListModel.elementAt(index);
		if (game != null)
			game.askToWatchGame();
	}
	//endregion
	
	//region Username Change Methods
	/**
	 * Takes the proposed username and puts it in the {@link #usernameChangeInput}. It's called when the chosen username is already taken.
	 *
	 * @param proposedUsername the proposed username (with a number at the end)
	 */
	public void proposeUsername(String proposedUsername) {
		usernameChangeInput.setText(proposedUsername);
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
	//endregion
	
	//region UserList and Chat Methods
	/**
	 * Opens a whisper chat tab with the selected user from {@link #userList}.
	 *
	 * @param user The user to chat with.
	 * @return the ChatPanel which opened.
	 */
	public ChatPanel openWhisperChat(ClientUser user) {
		// Check if there is already a tab open with that user.
		if (getWhisperChatForUser(user) != null) {
			mainChatPanel.displayInfo("You already have a chat with that user");
			return null;
		}
		ArrayList<ClientUser> whisperUsers = new ArrayList<>();
		whisperUsers.add(user);
		ChatPanel whisperTab = new ChatPanel(whisperUsers, "chatw");
		tabbedPane.addTab(user.getName(), whisperTab);
		tabbedPane.setSelectedComponent(whisperTab);
		return whisperTab;
	}
	
	/**
	 * Returns the whisper chat tab of the given user.
	 * @param user the given user
	 * @return the whisper ChatPanel of the user.
	 */
	public ChatPanel getWhisperChatForUser(User user) {
		if (user == Client.getThisUser()) {
			System.err.println("MainChatWindow#getWhisperChatForUser - Should never get it for the own user.");
			return null;
		}
		for (int i = 0; i < tabbedPane.getTabCount(); i++) {
			ArrayList<ClientUser> chatUsers = ((ChatPanel) tabbedPane.getComponentAt(i)).getChatUsers();
			if (chatUsers.size() == 1 && chatUsers.get(0) == user) {
				return (ChatPanel) tabbedPane.getComponentAt(i);
			}
		}
		return null;
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
		mainChatPanel.addChatUser(user);
	}
	
	/**
	 * Removes the user from the list and the main chat, when they logged of.
	 * Also closes the whisper chat with the user if one is open.
	 *
	 * @param user the user to remove.
	 */
	public void removeUserFromUserlist(ClientUser user) {
		userListModel.removeElement(user);
		mainChatPanel.removeChatUser(user);
		tabbedPane.remove(getWhisperChatForUser(user));
	}
	
	/**
	 * Renames the user in the user list and the tab of the user (not this user itself).
	 *
	 * @param oldName The user's old name.
	 * @param newName The user's new name.
	 * @param user The user.
	 */
	public void renamedUser(String oldName, String newName, ClientUser user) {
		// Update userList
		int userListIndex = userListModel.indexOf(user);
		userListModel.set(userListIndex, user);
		
		// Update tabbedPane
		ChatPanel chatPanel = getWhisperChatForUser(user);
		if (chatPanel != null) {
			int i = tabbedPane.indexOfComponent(chatPanel);
			tabbedPane.setTitleAt(i, newName);
		} else {
			System.err.println("MainChatWindow#renamedUser - No user found with oldName: " + oldName);
		}
		
		chatPanel.displayInfo( oldName + " changed their name to " + newName );
	}
	//endregion
	
	//region Misc Methods
	
	/**
	 * Sets the label of the username with the newly chosen username.
	 *
	 * @param username the new username
	 */
	public void setUsername(String username) {
		usernameLabel.setText("Your username: " + username);
	}
	//endregion
	//endregion
	
	
	//region Listeners
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
			startJoiningGame(waitingGameList.getSelectedIndex());
			
		} else if (e.getSource() == watchGameButton) {
			startWatchingGame(runningGameList.getSelectedIndex());
			
		} else if (e.getSource() == whisperButton) {
			openWhisperChat(userListModel.elementAt(userList.getSelectedIndex()));
			
		} else if (e.getSource() == viewHighscoreButton) {
			ClientCgethHandler.sendHighscoreRequest();
			
		}
	}
	
	/**
	 * Joins the game if one is selected or opens the newGameInputWindow.
	 */
	private void startJoiningGame(int index) {
		if (index < 0)
			if (waitingGameListModel.isEmpty())
				openNewGameInputWindow();
			else
				getMainChatPanel().displayError("Please select a game to join!");
		else
			joinGameAtIndex(0);
	}
	
	/**
	 * Start watching game if one is selected.
	 */
	private void startWatchingGame(int index) {
		if (index < 0)
			if (runningGameListModel.isEmpty())
				getMainChatPanel().displayError("There is no game you can watch!");
			else
				getMainChatPanel().displayError("Please select a game to watch!");
		else
			watchGameAtIndex(index);
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
				ClientUser user = userListModel.elementAt(userList.locationToIndex(e.getPoint()));
				openWhisperChat(user);
			}
			
		} else if (e.getSource() == waitingGameList) {
			if (e.getClickCount() == 2) {
				int index = waitingGameList.locationToIndex(e.getPoint());
				startJoiningGame(index);
			}
			
		} else if (e.getSource() == runningGameList) {
			if (e.getClickCount() == 2) {
				int index = runningGameList.locationToIndex(e.getPoint());
				startWatchingGame(index);
			}
			
		}
	}
	//endregion
	
	
	
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