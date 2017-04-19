package client.clientgui;

import client.Client;
import client.commands.ClientNewgmHandler;
import game.GameMap;
import game.engine.WinningCondition;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by flavia on 01.04.17.
 */
public class NewGameDialog extends JDialog implements ActionListener, KeyListener, ListSelectionListener {
	
	private JTextField gameNameTF = new JTextField("Name");
	private JTextField maxPointsTF = new JTextField("MaxPoints");
	
	private DefaultListModel<GameMap> mapListModel = new DefaultListModel();
	/**
	 * A List with all the Maps to choose from.
	 */
	private JList<GameMap> mapList = new JList<>(mapListModel);
	
	private DefaultListModel<WinningCondition> winningConditionListModel = new DefaultListModel();
	/**
	 * A List with all the Winning Conditions to choose from.
	 */
	private JList<WinningCondition> winningConditionList = new JList<>(winningConditionListModel);
	
	/**
	 * Displays the description of the selected Winning Condition from {@link #winningConditionList}.
	 */
	private JTextArea winningConditionDescriptionLabel = new JTextArea();
	
	private JButton createGameButton = new JButton("Create Game");
	private JButton cancelGameCreationButton = new JButton("Cancel");
	
	private MainChatWindow getChat() {
		return Client.getMainWindow();
	}
	
	/**
	 * Opens and displays a Dialog Window where the Client can set the parameters for the new Game.
	 * @param owner The frame this belongs to.
	 */
	public NewGameDialog(Frame owner) {
		super(owner);
		
		JLabel gameNameLabel = new JLabel("Game Name:");
		gameNameTF = new JTextField("GameName");
		JLabel maxPointsLabel = new JLabel("Max Points:");
		maxPointsTF = new JTextField("100");
		createGameButton = new JButton("Create Game");
		cancelGameCreationButton = new JButton("Cancel");
		
		createGameButton.addActionListener(this);
		cancelGameCreationButton.addActionListener(this);
		
		Box mainBox = Box.createVerticalBox();
		
		JPanel gameInputPanel = new JPanel(new GridLayout(2,2));
		gameInputPanel.add(gameNameLabel);
		gameInputPanel.add(gameNameTF);
		gameInputPanel.add(maxPointsLabel);
		gameInputPanel.add(maxPointsTF);
		
		
		JPanel gameButtonsPanel = new JPanel(new GridLayout(1, 2));
		gameButtonsPanel.add(cancelGameCreationButton);
		gameButtonsPanel.add(createGameButton);
		
		mainBox.add(gameInputPanel);
		
		mainBox.add(new JLabel("Map:"));
		mainBox.add(new JScrollPane(mapList));
		mainBox.add(new JLabel("Winning Condition:"));
		mainBox.add(new JScrollPane(winningConditionList));
		mainBox.add(winningConditionDescriptionLabel);
		
		mainBox.add(gameButtonsPanel);
		
		this.add(mainBox);
		
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
		
		for (GameMap map : GameMap.getAllMaps())
			mapListModel.addElement(map);
		mapList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mapList.setSelectedIndex(0);
		
		for (WinningCondition winningCondition : WinningCondition.values())
			winningConditionListModel.addElement(winningCondition);
		winningConditionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		winningConditionList.setSelectedIndex(0);
		winningConditionList.addListSelectionListener(this);
		updateWinningDescription();
		
		winningConditionDescriptionLabel.setEditable(false);
		winningConditionDescriptionLabel.setLineWrap(true);
		
	}
	
	/**
	 * @param e The ActionEvent
	 */
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
		
		if (gameName.contains("'")) gameName = gameName.replaceAll("'", "");
		if (gameName.contains(" ")) gameName = gameName.replaceAll(" ", "");
		
		if (gameName.isEmpty()) {
			getChat().getMainChatPanel().displayError("New Game Name is empty");
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
		if (mapList.isSelectionEmpty()) {
			getChat().getMainChatPanel().displayError("Please select a Map!");
			return;
		}
		
		ClientNewgmHandler.sendGameCreationMessage(maxPoints, gameName, mapListModel.getElementAt(mapList.getSelectedIndex()));
	}
	
	private void updateWinningDescription() {
		winningConditionDescriptionLabel.setText(winningConditionListModel.getElementAt(winningConditionList.getSelectedIndex()).getDescription());
	}
	
	
	@Override
	public void keyPressed(KeyEvent e) {
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
	
	/**
	 * Called whenever the value of the selection changes.
	 *
	 * @param e the event that characterizes the change.
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
	
	}
	
	
	@Override
	public void keyTyped(KeyEvent e) {
	
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
	
	}
}
