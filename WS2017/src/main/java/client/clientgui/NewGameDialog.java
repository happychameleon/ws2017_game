package client.clientgui;

import client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by flavia on 01.04.17.
 */
public class NewGameDialog extends JDialog implements ActionListener, KeyListener {
	
	private JTextField gameNameTF = new JTextField("Name");
	private JTextField maxPointsTF = new JTextField("MaxPoints");
	private JButton createGameButton = new JButton("Create Game");
	private JButton cancelGameCreationButton = new JButton("Cancel");
	
	private MainChatWindow getChat() {
		return Client.getMainChatWindow();
	}
	
	/**
	 * Opens and displays a Dialog Window where the Client can set the parameters for the new Game.
	 * @param owner The frame this belongs to.
	 */
	public NewGameDialog(Frame owner) {
		super(owner);
		
		JLabel gameNameLabel = new JLabel("gameName");
		gameNameTF = new JTextField("GameName");
		JLabel maxPointsLabel = new JLabel("Max Points:");
		maxPointsTF = new JTextField("100");
		createGameButton = new JButton("Create Game");
		cancelGameCreationButton = new JButton("Cancel");
		
		createGameButton.addActionListener(this);
		cancelGameCreationButton.addActionListener(this);
		
		JPanel panel = new JPanel(new GridLayout(3,2));
		
		panel.add(gameNameLabel);
		panel.add(gameNameTF);
		panel.add(maxPointsLabel);
		panel.add(maxPointsTF);
		panel.add(cancelGameCreationButton);
		panel.add(createGameButton);
		
		this.add(panel);
		
		this.pack();
		this.setVisible(true);
		
		this.addKeyListener(this);
		requestFocus();
		
		gameNameTF.requestFocusInWindow();
		gameNameTF.selectAll();
		gameNameTF.addKeyListener(this);
		maxPointsTF.addKeyListener(this);
		cancelGameCreationButton.addKeyListener(this);
		createGameButton.addKeyListener(this);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cancelGameCreationButton) {
			
			closeWindow();
			
		} else if (e.getSource() == createGameButton) {
			
			tryCreateGame();
			closeWindow();
			
		}
		
	}
	
	/**
	 * Disposes the window when the Client hits enter or cancel.
	 */
	private void closeWindow() {
		this.dispose();
		getChat().newGameDialog = null;
	}
	
	/**
	 * Sends a message to the server with the parameters of the proposed new Game.
	 */
	private void tryCreateGame() {
		
		String gameName = gameNameTF.getText();
		if (gameName.contains(" ") || gameName.contains("'") || gameName.isEmpty()) {
			getChat().getMainChatPanel().displayError("New Game Name Contains Invalid Characters or is empty");
			return;
		}
		int maxPoints;
		try {
			maxPoints = Integer.parseInt(maxPointsTF.getText());
		} catch (NumberFormatException nfe) {
			getChat().getMainChatPanel().displayError("MaxPoints must be entered as a valid number.");
			return;
		}
		
		if (maxPoints <= 0) {
			getChat().getMainChatPanel().displayError("MaxPoints must be positive!");
			return;
		}
		
		// Now we know the input is valid
		Client.sendMessageToServer("newgm " + maxPoints + " " + gameName);
		
	}
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("Key pressed!");
		switch (e.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				if (cancelGameCreationButton.hasFocus() == false)
					tryCreateGame();
				closeWindow();
				break;
			case KeyEvent.VK_ESCAPE:
				closeWindow();
				break;
			default:
				break;
		}
	}
	
	
	
	@Override
	public void keyTyped(KeyEvent e) {
	
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
	
	}
}
