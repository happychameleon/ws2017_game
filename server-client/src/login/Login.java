package login; // TODO: Rename package to gui (and move into client)

import client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class Login implements ActionListener {
	
	
	JFrame userFrame = new JFrame("User Login");
	JLabel userLabel = new JLabel("Username:");
	JTextField userNameText = new JTextField(20);
	JButton loginButton = new JButton("Login");
	
	
	public Login() {
		
		frame();
	}
	
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
		
		userFrame.setVisible(true);
		
		
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
		String username = userNameText.getText();
		
		Client.sendMessageToServer("uname " + username);
	}
	
	
}
