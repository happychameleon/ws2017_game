package game.startscreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Handles the selection of the team at the start of the game.
 *
 * FIXME: When closing the window or selecting cancel the leavg command should tell the server that this user left the game.
 *
 * Created by flavia on 28.03.17.
 */
public class StartScreen extends JPanel implements ActionListener {
	
	/**
	 * The ClientGameStartController for this game.
	 */
	private final ClientGameStartController clientGameStartController;
	
	/**
	 * The amount of points to spend for Children and Equipment.
	 */
	private final int startingPoints;
	
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
	 */
	public StartScreen(ClientGameStartController gameStartController, int startingPoints) {
		this.clientGameStartController = gameStartController;
		this.startingPoints = startingPoints;
		
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
				clientGameStartController.clientIsReady(getAllChildrenAsString());
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
	
	/**
	 * Prevents the user from selecting too many points or no child at all.
	 */
	private void updateButtonEnabling() {
		if (currentPoints > startingPoints || currentPoints == 0) {
			startGameButton.setEnabled(false);
		} else
			startGameButton.setEnabled(true);
	}
	
	private void updateCurrentPointsLabel() {
		currentPointCost.setText("Current Point Cost: " + currentPoints + " out of " + startingPoints);
	}
	
	/**
	 * Creates the String representing all the Children
	 * [<child1name> <child1weaponname> <child2name> <child2weaponname>]
	 */
	private String getAllChildrenAsString() {
		String characters = "[";
		for (Component component : childrenPanels.getComponents()) {
			if (component instanceof ChildPanel) {
				ChildPanel childPanel = ((ChildPanel) component);
				String name = childPanel.getCharacterName();
				String weaponName = childPanel.getCharacterWeapon().getName();
				characters += name + " " + weaponName + " ";
			}
		}
		characters.substring(0, characters.length() - 1); // To remove last space
		characters += "]";
		return characters;
	}
	
}
