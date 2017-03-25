package clientgui; // TODO: Rename package to gui (and move into client)

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
		userpanel.add(userLabel);
		userpanel.add(userNameText);
		userpanel.add(loginButton);
		userFrame.add(userpanel);
		
		// Achievement whoami (The client suggest a nickname based on the system username)
		userNameText.setText(System.getProperty("user.name"));
		
		userFrame.setVisible(true);
		
		userNameText.addKeyListener(this);
		userNameText.setFocusable(true);
		loginButton.addActionListener(this);
		
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
		if (username.isEmpty())
			return;
		Client.sendMessageToServer("uname " + username);
	}
	
	
	@Override
	public void keyTyped(KeyEvent e) {
	
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
	
	}
}
