package login;

import client.Client;
import client.ClientUser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;


public class Chat implements ActionListener, KeyListener {
	
	/**
	 * All the messages in this chat. A message should ALWAYS be added via {@link #addNewMessage(ChatMessage)}!
	 */
	private ArrayList<ChatMessage> messages = new ArrayList<>();
	
	/**
	 * Adds a {@link ChatMessage} to the chat and displays it properly.
	 * @param chatMessage the message to display
	 */
	private void addNewMessage(ChatMessage chatMessage) {
		messages.add(chatMessage);
		chatText.append(chatMessage.getSender().getName() + ": " + chatMessage.getMessage() + "\n");
	}
	
	
	JFrame chatFrame = new JFrame("Chat");
	JTextField chat_in = new JTextField(15);
	JTextField usernameChange_in = new JTextField(15);
	JButton sendChatButton = new JButton("Send");
	JButton usernameChange = new JButton("Change Username");
	JTextArea chatText = new JTextArea(30,50);
	
	public Chat() {
		
		chatFrame.setTitle("Username: " + Client.getThisUser().getName());
		
		frame();
	}
	
	public void frame() {
		// modify JFrame component layout
		chatFrame.setSize(700, 700);
		chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chat_in.setFont(new Font("Courier New", Font.ITALIC, 50));
		usernameChange_in.setFont(new Font("Courier New", Font.ITALIC, 50));
		sendChatButton.setPreferredSize(new Dimension(100, 50));
		usernameChange.setPreferredSize(new Dimension(200,50));
		
		chatText.setEditable(false);
		
		// Create window for the chat
		JPanel p = new JPanel();
		p.add(chatText);
		p.add(chat_in);
		p.add(sendChatButton);
		p.add(usernameChange_in);
		p.add(usernameChange);
		chatFrame.add(p);
		
		usernameChange_in.addKeyListener(this);
		usernameChange_in.setFocusable(true);
		
		chat_in.addKeyListener(this);
		chat_in.setFocusable(true);
		
		chatFrame.setVisible(true);
		
		sendChatButton.addActionListener(this);
		usernameChange.addActionListener(this);
		
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
		usernameChange_in.setText(proposedUsername);
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
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == usernameChange) {
			sendChangeUsernameRequest();
		} else if (e.getSource() == sendChatButton) {
			// TODO Meilenstein 3: add possibility (maybe different chat windows?) to send to specific user.
			sendMessage();
		}
	}
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("KEY PRESSED!");
		switch (e.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				if (chat_in.isFocusOwner()) {
					sendMessage();
				} else if (usernameChange_in.isFocusOwner()) {
					sendChangeUsernameRequest();
				}
				break;
			default:
				break;
		}
	}
	
	/**
	 * Sends the text in the chat text field {@link #chat_in} as a chat message to all the connected clients.
	 */
	private void sendMessage() {
		if (chat_in.getText().isEmpty())
			return;
		
		ArrayList<ClientUser> receivers = new ArrayList<>();
		for (ClientUser user : Client.getAllUsers()) {
			if (user != Client.getThisUser()) {
				receivers.add(user);
			}
		}
		addNewMessage(new ChatMessage(chat_in.getText(), receivers));
		chat_in.setText("");
	}
	
	/**
	 * Sends a message to the server requesting to change the username.
	 */
	private void sendChangeUsernameRequest() {
		String username = usernameChange_in.getText();
		if (username.isEmpty())
			return;
		Client.sendMessageToServer("uname " + username);
		usernameChange_in.setText("");
	}
	
	
	
	@Override
	public void keyTyped(KeyEvent e) {
	
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
	
	}
}

