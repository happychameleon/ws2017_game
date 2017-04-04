package client.clientgui;

import client.Client;
import client.ClientUser;
import game.ClientGameController;
import game.startscreen.ClientGameStartController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * The general Chat window where all users can chat with each other and name changes can be requested.
 * TODO Meilenstein 3: In future milestones there will also be the possibility to start a game with selected people and to privately chat
 * TODO: Separate this Chat Class into two classes, one for only the main Chat stuff and one for the whole window and keeping track of the game.
 */
public class Chat implements ActionListener, KeyListener, MouseListener {
	
	/**
	 * All the messages in this chat. A message should ALWAYS be added via {@link #addNewMessage(ChatMessage)}!
	 */
	private ArrayList<ChatMessage> messages = new ArrayList<>();
	
	private boolean lastMessageIsInfo = false;
	
	/**
	 * @return The last message that was displayed in this chat. Null if there was no message yet or the last entry was an Info.
	 */
	private ChatMessage getLastMessage() {
		if (lastMessageIsInfo || messages.isEmpty()) {
			return null;
		}
		return messages.get(messages.size() - 1);
	}
	
	
	JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	
	
	/**
	 * The Panel where the main Chat is in.
	 */
	JPanel mainPanel;
	
	JFrame chatFrame = new JFrame("Chat");
	JTextField chatInput = new JTextField(15);
	JTextField usernameChangeInput = new JTextField(15);
	JButton sendChatButton = new JButton("Send");
	JButton usernameChangeButton = new JButton("Change Username");
	JTextArea chatText = new JTextArea(30, 50);
	JScrollPane scroll;
	
	
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
	public Chat() {
		
		chatFrame.setTitle("Username: " + Client.getThisUser().getName());
		
		frame();
	}
	
	/**
	 * Opens and displays the Chat Window.
	 */
	public void frame() {
		// modify JFrame component layout
		chatFrame.setSize(1000, 700);
		chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatInput.setFont(new Font("Courier New", Font.ITALIC, 30));
		usernameChangeInput.setFont(new Font("Courier New", Font.ITALIC, 30));
		//sendChatButton.setPreferredSize(new Dimension(100, 50));
		//usernameChangeButton.setPreferredSize(new Dimension(200,50));
		
		// Make scrolling possible:
		chatText.setEditable(false);
		scroll = new JScrollPane(chatText);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		// Either set lineWrap to true or make horizontal scrolling available. Just change the commented line to get the other option.
		// One of them has to be active or long text will be cut.
		chatText.setLineWrap(true);
		//scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		// Create main Panel for the chat
		mainPanel = new JPanel(new BorderLayout(20, 10));
		
		// The middle contains the Chat Panel
		JPanel chatPanel = new JPanel();
		chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
		//chatPanel.add(new JLabel("MAIN CHAT")); Already existing in Tab
		chatPanel.add(scroll);
		// The Panel with the text input for the chat and username change
		JPanel textInputPanel = new JPanel(new GridLayout(2, 2));
		textInputPanel.add(chatInput);
		textInputPanel.add(sendChatButton);
		textInputPanel.add(usernameChangeInput);
		textInputPanel.add(usernameChangeButton);
		chatPanel.add(textInputPanel);
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
		
		// Add the main Panel as a Tab to the tabbedPanel.
		tabbedPane.addTab("Main Chat", mainPanel);
		// Add the tabbedPanel to the main frame
		chatFrame.add(tabbedPane);
		
		
		// Non-Layout specific configurations for the ui elements
		
		usernameChangeInput.addKeyListener(this);
		usernameChangeInput.setFocusable(true);
		
		chatInput.addKeyListener(this);
		chatInput.setFocusable(true);
		
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
		
		chatFrame.setVisible(true);
		
		sendChatButton.addActionListener(this);
		usernameChangeButton.addActionListener(this);
		
	}
	
	
	/**
	 * Sets the Title of the Chat Window.
	 *
	 * @param newTitle
	 */
	public void setTitle(String newTitle) {
		chatFrame.setTitle(newTitle);
	}
	
	/**
	 * Takes the proposed username and puts it in the textfield. It's called when the chosen username is already taken.
	 *
	 * @param proposedUsername the proposed username (with a number at the end)
	 */
	public void proposeUsername(String proposedUsername) {
		usernameChangeInput.setText(proposedUsername);
	}
	
	/**
	 * This is to display info like when a new user joins or when a user changes their name.
	 *
	 * @param info the info text to display.
	 */
	public void displayInfo(String info) {
		String infoMessage = "";
		if (lastMessageIsInfo == false)
			infoMessage += "\n";
		infoMessage += ">>>" + info + "\n\n";
		chatText.append(infoMessage);
		lastMessageIsInfo = true;
	}
	
	public void displayError(String errorMessage) {
		// TODO Meilenstein 3: separate Error message format than Info.
		displayInfo("ERROR: " + errorMessage);
	}
	
	/**
	 * Adds a {@link ChatMessage} to the chat and displays it properly.
	 *
	 * @param chatMessage the message to display
	 */
	public void addNewMessage(ChatMessage chatMessage) {
		String textToAppend = "";
		if (getLastMessage() != null && chatMessage.getSender() != getLastMessage().getSender()) {
			// If the last message was from a different person add a bigger gap between.
			System.out.println("ADD GAP");
			textToAppend += "\n";
		}
		textToAppend += chatMessage.getSender().getName() + ": " + chatMessage.getMessage() + "\n";
		chatText.append(textToAppend);
		
		messages.add(chatMessage);
		lastMessageIsInfo = false;
	}
	
	
	public void addNewGameToList(ClientGameStartController cgsc) {
		openGameListModel.addElement(cgsc);
	}
	
	public void addRunningGameToList(ClientGameController cgc) {
		runningGameListModel.addElement(cgc);
	}
	
	public void removeGameFromList(ClientGameStartController cgsc) {
		openGameListModel.removeElement(cgsc);
		displayInfo("The game " + cgsc.getGameName() + " has been removed, because there were no players left.");
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
			
		} else if (e.getSource() == sendChatButton) {
			sendMessage();
			
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
				if (chatInput.isFocusOwner()) {
					sendMessage();
				} else if (usernameChangeInput.isFocusOwner()) {
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
		newGameDialog = new NewGameDialog(chatFrame);
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
	 * Sends the text in the chat text field {@link #chatInput} as a chat message to all the connected clients.
	 */
	private void sendMessage() {
		if (chatInput.getText().isEmpty())
			return;
		
		ArrayList<ClientUser> receivers = new ArrayList<>();
		for (ClientUser user : Client.getAllUsers()) {
			if (user != Client.getThisUser()) {
				receivers.add(user);
			}
		}
		addNewMessage(new ChatMessage(chatInput.getText(), receivers));
		chatInput.setText("");
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
				displayInfo("You already have a chat with that user");
				return;
			}
		}
		WhisperTab whisperTab = new WhisperTab(user);
		tabbedPane.addTab(user.getName(), whisperTab);
	}
	
	/**
	 * Adds the user to the list when they logged in.
	 * @param user the new user.
	 */
	public void addUserToUserlist(ClientUser user) {
		if (user == Client.getThisUser())
			System.err.println("Chat#addUserToUserlist - trying to add this user to the list!");
		userListModel.addElement(user);
	}
	
	/**
	 * Removes the user from the list, when they logged of.
	 * @param user the user to remove.
	 */
	public void removeUserFromUserlist(ClientUser user) {
		userListModel.removeElement(user);
	}
	
	/**
	 * Renames the user at the user list and the tab of the user.
	 * TODO Needed?
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
			System.err.println("Chat#renamedUser - No user found with oldName: " + oldName);
		}
	}
	
	
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
}

