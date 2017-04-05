package client.clientgui;

import client.ClientUser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * This Panel displays a Text Area where the Chat is displayed and an input TextField with Button to send messages
 * The ChatListener of the button is the specified ChatListener in the constructor.
 *
 * Created by flavia on 05.04.17.
 */
public class ChatPanel extends JPanel implements ActionListener, KeyListener {
	
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
	
	
	private final ArrayList<ClientUser> chatUsers;
	
	
	JTextField chatInputTextField = new JTextField(15);
	JButton sendChatButton = new JButton("Send");
	JTextArea chatTextArea = new JTextArea(30, 50);
	
	
	/**
	 * Creates a Chat Panel where users can Chat.
	 * @param chatUsers The Users in this Chat.
	 */
	public ChatPanel(ArrayList<ClientUser> chatUsers) {
		this.chatUsers = chatUsers;
		
		// Chat Text
		chatTextArea.setEditable(false);
		chatTextArea.setLineWrap(true);
		JScrollPane chatScroll = new JScrollPane(chatTextArea);
		chatScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(chatScroll);
		
		// Chat Input
		JPanel chatInputPanel = new JPanel(new GridLayout(1, 2));
		chatInputTextField.addKeyListener(this);
		chatInputTextField.setFocusable(true);
		chatInputPanel.add(chatInputTextField);
		sendChatButton.addActionListener(this);
		chatInputPanel.add(sendChatButton);
		this.add(chatInputPanel);
		
		// Format (optionally)
		//chatInputTextField.setFont(new Font("Courier New", Font.ITALIC, 30));
		
		
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
			//System.out.println("ADD GAP");
			textToAppend += "\n";
		}
		textToAppend += chatMessage.getSender().getName() + ": " + chatMessage.getMessage() + "\n";
		chatTextArea.append(textToAppend);
		
		messages.add(chatMessage);
		lastMessageIsInfo = false;
	}
	
	/**
	 * Adds the user to the list of users in this chat so they receive messages.
	 *
	 * @param user the user to add.
	 */
	public void addChatUser(ClientUser user) {
		chatUsers.add(user);
	}
	
	/**
	 * Removes the user from the list of users in this chat so they don't receive messages anymore.
	 *
	 * @param user the user to remove.
	 */
	public void removeChatUser(ClientUser user) {
		chatUsers.remove(user);
	}
	
	/**
	 * Sends the text in the chat text field {@link #chatInputTextField} as a chat message to all the connected clients.
	 */
	private void sendMessage() {
		if (chatInputTextField.getText().isEmpty())
			return;
		
		ArrayList<ClientUser> receivers = new ArrayList<>();
		for (ClientUser user : chatUsers) {
			receivers.add(user);
		}
		new ChatMessage(chatInputTextField.getText(), receivers);
		chatInputTextField.setText("");
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
		chatTextArea.append(infoMessage);
		lastMessageIsInfo = true;
	}
	
	/**
	 * Displays the errorMessage like with {@link #displayInfo(String)} but formatted as an ERROR.
	 *
	 * @param errorMessage The error Message.
	 */
	public void displayError(String errorMessage) {
		displayInfo("ERROR: " + errorMessage);
	}
	
	
	
	/**
	 * Invoked when an action occurs.
	 *
	 * @param e The Action Event
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == sendChatButton) {
			sendMessage();
			
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
				if (chatInputTextField.isFocusOwner()) {
					sendMessage();
					
				}
			
		}
		
	}
	
	
	
	
	//region unused
	
	@Override
	public void keyReleased(KeyEvent e) {
	
	}
	
	
	@Override
	public void keyTyped(KeyEvent e) {
	
	}
	
	//endregion
}
