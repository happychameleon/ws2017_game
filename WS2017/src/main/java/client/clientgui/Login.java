package client.clientgui;

import client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * This is the first window when starting the client. Here the user can log in by setting their username.
 */
public class Login implements ActionListener, KeyListener {
	
	
	JFrame userFrame = new JFrame("User Login");
	JLabel userLabel = new JLabel("Username:");
	JTextField userNameText = new JTextField(20);
	JButton loginButton = new JButton("Login");
	
	
	public Login() {
		
		frame();
	}
	
	/**
	 * Opens and displays the Login Window.
	 */
	public void frame(){
		// modify JFrame component layout
		userFrame.setSize(650,300);
		userFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		userLabel.setFont(new Font("Courier New", Font.BOLD, 75));
		loginButton.setPreferredSize(new Dimension(100,50));
		userNameText.setFont(new Font("Courier New", Font.ITALIC , 50));
		// Create the window for the Login
		JPanel userpanel = new JPanel();
		userpanel.setLayout(new BoxLayout(userpanel, BoxLayout.Y_AXIS));
		
		userpanel.add(userLabel);
		userpanel.add(userNameText);
		userpanel.add(loginButton);
		userFrame.add(userpanel);
		
		userNameText.addKeyListener(this);
		userNameText.setFocusable(true);
		loginButton.addActionListener(this);
		
		// Propose a username by presetting the value in the login window text field if possible.
		// First get the systemUsername safely
		String systemUserName;
		try {
			systemUserName = System.getProperty("user.name");
		} catch (SecurityException | NullPointerException | IllegalArgumentException e) {
			systemUserName = "";
		}
		// Then set the text.
		if (Client.getCommandLineUsername().isEmpty() == false) {
			// Set the username from the command line if one is given.
			userNameText.setText(Client.getCommandLineUsername());
			// This immediately tries to log in when a user name is given via command line, as if the user logged in via button.
			// If the username is invalid the login window stays open.
			sendUsernameRequest();
		} else if (systemUserName.isEmpty() == false) {
			// Achievement whoami (The client suggest a nickname based on the system username)
			userNameText.setText(systemUserName);
		}
		
		userFrame.pack();
		userFrame.setVisible(true);
		
	}
	
	public void closeWindow() {
		userFrame.dispose();
	}
	
	public void proposeUsername(String proposedUsername) {
		userNameText.setText(proposedUsername);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		sendUsernameRequest();
	}
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				sendUsernameRequest();
				break;
			default:
				break;
		}
	}
	
	/**
	 * Sends a request to see if the entered username is available.
	 * The answer is handled by the {@link client.ClientCommandParser}.
	 */
	private void sendUsernameRequest() {
		String username = userNameText.getText();
		if (username.isEmpty() || username.contains(" ") || username.contains("'"))
			return;
		Client.sendMessageToServer("uname " + username);
		userNameText.setText("");
	}
	
	
	@Override
	public void keyTyped(KeyEvent e) {
	
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
	
	}
}
