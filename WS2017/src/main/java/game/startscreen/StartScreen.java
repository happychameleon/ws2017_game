package game.startscreen;

import game.engine.PlayerColor;
import game.engine.Character;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Handles the selection of the team at the start of the game.
 *
 * Created by flavia on 28.03.17.
 */
public class StartScreen extends JPanel implements ActionListener {
	
	/**
	 * The GameStartController for this game.
	 */
	private final GameStartController gameStartController;
	
	/**
	 * The amount of points to spend for Children and Equipment.
	 */
	private final int startingPoints;
	
	/**
	 * The Color this player was assigned to from the server. Should be displayed somewhere here.
	 */
	private final PlayerColor playerColor;
	
	/**
	 * The window this is displayed in.
	 */
	private JFrame window;
	
	/**
	 * This button adds a new child to the Team.
	 */
	private JButton newChildButton = new JButton("Add Child");
	
	/**
	 * This panel holds all the {@link ChildPanel}s.
	 */
	private JPanel childrenPanels = new JPanel();
	
	/**
	 * The Points of all the Characters and their gear combined.
	 */
	private int currentPoints = 0;
	
	/**
	 * Displays the total of points for the selected team.
	 */
	private JLabel currentPointCost = new JLabel();
	
	/**
	 * The Button to accept the current selection and start the Game.
	 * It is disabled if too many points were spent.
	 */
	private JButton startGameButton = new JButton("Start Game");
	
	/**
	 * Holds the Components at the Bottom of this window, namely {@link #currentPointCost} and {@link #startGameButton}.
	 */
	private JPanel pageEndPanel = new JPanel();
	
	// TODO: Add cancel button to leave this game.
	
	/**
	 * Opens the Start Screen to choose the team.
	 * @param gameStartController
	 * @param startingPoints The max amount of points to choose the team from.
	 * @param playerColor The Color of this user.
	 */
	public StartScreen(GameStartController gameStartController, int startingPoints, PlayerColor playerColor) {
		this.gameStartController = gameStartController;
		this.startingPoints = startingPoints;
		this.playerColor = playerColor;
		
		window = new JFrame();
		window.add(this);
		
		this.setLayout(new BorderLayout());
		
		this.add(newChildButton, BorderLayout.PAGE_START);
		newChildButton.addActionListener(this);
		
		this.add(childrenPanels, BorderLayout.CENTER);
		childrenPanels.setLayout(new BoxLayout(childrenPanels, BoxLayout.Y_AXIS));
		childrenPanels.add(new JSeparator(SwingConstants.HORIZONTAL));
		
		pageEndPanel.setLayout(new BoxLayout(pageEndPanel, BoxLayout.X_AXIS));
		pageEndPanel.add(currentPointCost);
		pageEndPanel.add(startGameButton);
		recalculatePoints(); // To set the currentPointCost-Label Text and to be sure (in case we add default stuff or change something else)
		this.add(pageEndPanel, BorderLayout.PAGE_END);
		
		window.pack();
		window.setVisible(true);
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object i = e.getSource();
		if (i.equals(newChildButton)) {
			addNewChild();
		}
		else if (i.equals(startGameButton)) {
			recalculatePoints(); // Just to be sure an update to the points went missing.
			if (currentPoints <= startingPoints) {
				gameStartController.clientIsReady(getAllChildrenAsCharacterArrayList());
			} else {
				System.err.println("The startGameButton wasn't disabled but the currentPoints > startingPoints!");
			}
		}
	}
	
	/**
	 * Adds a new Child to the {@link #childrenPanels} and recalculates the points.
	 */
	private void addNewChild() {
		JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
		childrenPanels.add(new ChildPanel(this, separator));
		childrenPanels.add(separator);
		recalculatePoints();
		window.pack();
	}
	
	/**
	 * Removes the child from the {@link #childrenPanels} and recalculates the points.
	 * @param childPanel the JPanel representing the Child to remove.
	 */
	protected void removeChild (ChildPanel childPanel, JSeparator separator) {
		childrenPanels.remove(childPanel);
		childrenPanels.remove(separator);
		recalculatePoints();
		window.pack();
	}
	
	/**
	 * This calculates the currently used points from all the Children Panels via {@link ChildPanel#getPointCost()}
	 */
	protected void recalculatePoints() {
		int currentPoints = 0;
		for (Component childPanel : childrenPanels.getComponents()) {
			if (childPanel instanceof ChildPanel) {
				currentPoints += ((ChildPanel) childPanel).getPointCost();
			}
		}
		this.currentPoints = currentPoints;
		updateButtonEnabling();
		updateCurrentPointsLabel();
	}
	
	private void updateButtonEnabling() {
		if (currentPoints > startingPoints)
			startGameButton.setEnabled(false);
		else
			startGameButton.setEnabled(true);
	}
	
	private void updateCurrentPointsLabel() {
		currentPointCost.setText("Current Point Cost: " + currentPoints + " out of " + startingPoints);
	}
	
	/**
	 * Creates all the Children as specified in the  and
	 * @return
	 */
	private ArrayList<Character> getAllChildrenAsCharacterArrayList() {
		ArrayList<Character> characters = new ArrayList<Character>();
		for (Component component : childrenPanels.getComponents()) {
			if (component instanceof ChildPanel) {
				ChildPanel childPanel = ((ChildPanel) component);
				characters.add(new Character(
						gameStartController.getWorld(),
						childPanel.getCharacterName(),
						gameStartController.getThisClientPlayer(),
						childPanel.getCharacterWeapon()
				));
			}
		}
		return characters;
	}
	
}
