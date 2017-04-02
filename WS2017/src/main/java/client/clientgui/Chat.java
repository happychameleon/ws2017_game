package client.clientgui;

import client.Client;
import client.ClientUser;
import game.startscreen.ClientGameStartController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The general Chat window where all users can chat with each other and name changes can be requested.
 * TODO Meilenstein 3: In future milestones there will also be the possibility to start a game with selected people and to privately chat
 * TODO: Separate this Chat Class into two classes, one for only the main Chat stuff and one for the whole window and keeping track of the game.
 */
public class Chat implements ActionListener, KeyListener {
	
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
	
	/**
	 * The Panel where the main Chat is in.
	 */
	JPanel mainChatPanel;
	
	JFrame chatFrame = new JFrame("Chat");
	JTextField chatInput = new JTextField(15);
	JTextField usernameChangeInput = new JTextField(15);
	JButton sendChatButton = new JButton("Send");
	JButton usernameChangeButton = new JButton("Change Username");
	JTextArea chatText = new JTextArea(30,50);
	JScrollPane scroll;
	
	
	DefaultListModel<ClientGameStartController> openGameListModel = new DefaultListModel<>();
	/**
	 * All the currently open games where this client can join.
	 */
	JList<ClientGameStartController> openGameList = new JList<>(openGameListModel);
	
	JButton newGameButton = new JButton("Create Game");
	JButton joinGameButton = new JButton("Join Game");
	
	// The Dialog Window for creating a new game.
	NewGameDialog newGameDialog;
	
	
	
	
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
		chatFrame.setSize(700, 700);
		chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatInput.setFont(new Font("Courier New", Font.ITALIC, 50));
		usernameChangeInput.setFont(new Font("Courier New", Font.ITALIC, 50));
		sendChatButton.setPreferredSize(new Dimension(100, 50));
		usernameChangeButton.setPreferredSize(new Dimension(200,50));
		
		// Make scrolling possible:
		chatText.setEditable(false);
		scroll = new JScrollPane (chatText);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		// Either set lineWrap to true or make horizontal scrolling available. Just change the commented line to get the other option.
		// One of them has to be active or long text will be cut.
		chatText.setLineWrap(true);
		//scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		// Create main Panel for the chat
		mainChatPanel = new JPanel(new BorderLayout());
		mainChatPanel.add(scroll, BorderLayout.CENTER);
		
		// The Panel with the text input for the chat and username change
		JPanel textInputPanel = new JPanel(new GridLayout(2,2));
		textInputPanel.add(chatInput);
		textInputPanel.add(sendChatButton);
		textInputPanel.add(usernameChangeInput);
		textInputPanel.add(usernameChangeButton);
		mainChatPanel.add(textInputPanel, BorderLayout.PAGE_END);
		
		// The left panel with the game selection and new game creation.
		JPanel gameCreationPanel = new JPanel();
		gameCreationPanel.setLayout(new BoxLayout(gameCreationPanel, BoxLayout.Y_AXIS));
		JScrollPane openGameListScroller = new JScrollPane(openGameList);
		gameCreationPanel.add(openGameListScroller);
		gameCreationPanel.add(joinGameButton);
		gameCreationPanel.add(newGameButton);
		mainChatPanel.add(gameCreationPanel, BorderLayout.LINE_START);
		
		chatFrame.add(mainChatPanel);
		
		
		// Non-Layout specific configurations for the ui elements
		
		usernameChangeInput.addKeyListener(this);
		usernameChangeInput.setFocusable(true);
		
		chatInput.addKeyListener(this);
		chatInput.setFocusable(true);
		
		newGameButton.addActionListener(this);
		joinGameButton.addActionListener(this);
		
		openGameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		openGameList.setLayoutOrientation(JList.VERTICAL);
		
		
		chatFrame.setVisible(true);
		
		sendChatButton.addActionListener(this);
		usernameChangeButton.addActionListener(this);
		
	}
	
	
	/**
	 * Sets the Title of the Chat Window.
	 * @param newTitle
	 */
	public void setTitle(String newTitle) {
		chatFrame.setTitle(newTitle);
	}
	
	/**
	 * Takes the proposed username and puts it in the textfield. It's called when the chosen username is already taken.
	 * @param proposedUsername the proposed username (with a number at the end)
	 */
	public void proposeUsername(String proposedUsername) {
		usernameChangeInput.setText(proposedUsername);
	}
	
	/**
	 * This is to display info like when a new user joins or when a user changes their name.
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
	 * @param chatMessage the message to display
	 */
	private void addNewMessage(ChatMessage chatMessage) {
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
	
	/**
	 * This is called when the server sends a new message with the chatm command.
	 * @param argument the argument from the server.
	 */
	public void receiveMessage(String argument) {
		char[] argumentChars = argument.toCharArray();
		String senderName = "";
		String receiverName = "";
		String message = "";
		int argumentNr = 1;
		// split the whole argument into the three argument parts
		for (char c : argumentChars) {
			if (argumentNr < 3 && c == ' ') {
				argumentNr++;
				continue;
			}
			if (argumentNr == 1)
				senderName += c;
			else if (argumentNr == 2)
				receiverName +=c;
			else
				message += c;
		}
		ClientUser sender = Client.getUserByName(senderName);
		ClientUser receiver = Client.getUserByName(receiverName);
		if (receiver != Client.getThisUser()) {
			System.err.println("WARNIGN: This message isn't intended for this client! Or did someone miss a namechange?");
		}
		// To get rid of the '
		message = message.substring(1, message.length() - 1);
		
		addNewMessage(new ChatMessage(message, sender, new ArrayList<>(Arrays.asList(receiver))));
	}
	
	
	public void addNewGameToList(ClientGameStartController cgsc) {
		openGameListModel.addElement(cgsc);
		displayInfo("A new game called " + cgsc.getGameName() + " has been created.");
	}
	
	public void removeGameFromList(ClientGameStartController cgsc) {
		openGameListModel.removeElement(cgsc);
		displayInfo("The game " + cgsc.getGameName() + " has been removed, because there were no players left.");
	}
	
	/**
	 * Gets the Game with the specified name from the List of games.
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
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == usernameChangeButton) {
			sendChangeUsernameRequest();
			
		} else if (e.getSource() == sendChatButton) {
			// TODO Meilenstein 3: add possibility (maybe different chat windows?) to send to specific user.
			sendMessage();
			
		} else if (e.getSource() == newGameButton) {
			openNewGameInputWindow();
			
		} else if (e.getSource() == joinGameButton) {
			ClientGameStartController cgsc = openGameList.getSelectedValue();
			if (cgsc != null)
				cgsc.joinGame();
			
		}
	}
	
	/**
	 * Tries to create a new game from the input entered into the {@link #newGameDialog}.
	 */
	protected void tryCreateGame(String gameName, int maxPoints) {
		
		if (maxPoints <= 0) {
			displayError("MaxPoints must be positive!");
			return;
		}
		// Now we know the input is valid
		Client.sendMessageToServer("newgm " + maxPoints + " " + gameName);

	}
	
	private void openNewGameInputWindow() {
		newGameDialog = new NewGameDialog(chatFrame, this);
	}
	
	
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
	
	
	
	@Override
	public void keyTyped(KeyEvent e) {
	
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
	
	}
	
	
}

