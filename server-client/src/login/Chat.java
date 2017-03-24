package login;

import client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Chat implements ActionListener {
	
	
	JFrame chatFrame = new JFrame("Chat");
	JTextField chat_in = new JTextField(15);
	JTextField usernameChange_in = new JTextField(15);
	JButton sendChatButton = new JButton("Send");
	JButton usernameChange = new JButton("Change Username");
	JTextArea text_out = new JTextArea(30,50);
	
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
		
		text_out.setEditable(false);
		
		// Create window for the chat
		JPanel p = new JPanel();
		p.add(text_out);
		p.add(chat_in);
		p.add(sendChatButton);
		p.add(usernameChange_in);
		p.add(usernameChange);
		chatFrame.add(p);
		
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
	
	public void proposeUsername(String proposedUsername) {
		usernameChange_in.setText(proposedUsername);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == usernameChange) {
			String username = usernameChange_in.getText();
			Client.sendMessageToServer("uname " + username);
			usernameChange_in.setText("");
			
		} else if (e.getSource() == sendChatButton) {
		
		}
	}
	
}

