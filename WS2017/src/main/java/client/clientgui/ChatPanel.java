package client.clientgui;

import client.Client;
import client.ClientUser;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.URL;
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
	
	/**
	 * All the users of this chat.
	 */
	protected final ArrayList<ClientUser> chatUsers;
	
	/**
	 * @return A shallow copy of {@link #chatUsers}.
	 */
	public ArrayList<ClientUser> getChatUsers() {
		return (ArrayList<ClientUser>) chatUsers.clone();
	}
	
	/**
	 * The command to send the chat message with (eg chatm or chatw).
	 */
	protected final String chatCommand;
	
	/**
	 * Where the user can write the text messages.
	 */
	protected JTextField chatInputTextField = new JTextField(15);
	/**
	 * Where the user can send the message
	 */
	private JButton sendChatButton = new JButton("Send");
	/**
	 * Where the Chat messages are displayed.
	 */
	private JTextArea chatTextArea = new JTextArea(30, 50);
	
	
	/**
	 * The sound to play when a message arrives.
	 */
	protected Clip chatMessageSoundClip;
	
	
	/**
	 * Creates a Chat Panel where users can Chat.
	 * @param chatUsers The Users in this Chat.
	 * @param chatCommand The command to send the chat message with (eg chatm or chatw).
	 * @param messageSoundName The name of the .wav file in the sound folder to play when a message arrives. {@link #chatMessageSoundClip}.
	 */
	public ChatPanel(ArrayList<ClientUser> chatUsers, String chatCommand, String messageSoundName) {
		this.chatUsers = chatUsers;
		this.chatCommand = chatCommand;
		
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
		
		// Load message Sound
		try {
			URL url = this.getClass().getClassLoader().getResource("sound/" + messageSoundName + ".wav");
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
			chatMessageSoundClip = AudioSystem.getClip();
			chatMessageSoundClip.open(audioInputStream);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		
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
		
		//if (chatMessage.getSender() != Client.getThisUser())
		playSound(chatMessageSoundClip);
	}
	
	/**
	 * Adds the user to the list of users in this chat so they receive messages.
	 *
	 * @param user the user to add.
	 */
	public void addChatUser(ClientUser user) {
		chatUsers.add(user);
		// TODO(M5): Play a sound.
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
	protected void sendMessage() {
		String message = chatInputTextField.getText();
		if (message.isEmpty())
			return;
		
		ArrayList<ClientUser> receivers = new ArrayList<>();
		for (ClientUser user : chatUsers) {
			receivers.add(user);
		}
		ClientUser sender = Client.getThisUser();
		for (ClientUser receiver : receivers) {
			Client.sendMessageToServer(chatCommand + " " + sender.getName() + " " + receiver.getName() + " '" + message + "'");
		}
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
		
		//playSound(chatMessageSoundClip); // TODO different info sound.
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
	 * Plays the given sound.
	 * @param sound The Clip to play.
	 */
	private void playSound(Clip sound) {
		System.out.println("ChatPanel#playSound");
		if (sound.isRunning())
			sound.stop();
		sound.setFramePosition(0);
		sound.start();
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
