package game.startscreen;

import game.ClientGameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Handles the selection of the team at the start of the game.
 *
 * Created by flavia on 28.03.17.
 */
public class StartScreen extends JPanel implements ActionListener, WindowListener {
	
	/**
	 * The ClientGameStartController for this game.
	 */
	private final ClientGameController clientGameStartController;
	
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
	private JButton acceptSelectionButton = new JButton("Accept Selection");
	
	private JButton cancelButton = new JButton("Cancel");
	
	/**
	 * Holds the Components at the Bottom of this window, namely {@link #currentPointCost} and {@link #acceptSelectionButton}.
	 */
	private Box pageEndBox;
	
	/**
	 * Opens the Start Screen to choose the team.
	 * @param gameStartController
	 * @param startingPoints The max amount of points to choose the team from.
	 */
	public StartScreen(ClientGameController gameStartController, int startingPoints) {
		this.clientGameStartController = gameStartController;
		this.startingPoints = startingPoints;
		
		window = new JFrame();
		window.add(this);
		window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		window.addWindowListener(this);
		
		this.setLayout(new BorderLayout());
		
		this.add(newChildButton, BorderLayout.PAGE_START);
		newChildButton.addActionListener(this);
		
		this.add(childrenPanels, BorderLayout.CENTER);
		childrenPanels.setLayout(new BoxLayout(childrenPanels, BoxLayout.Y_AXIS));
		childrenPanels.add(new JSeparator(SwingConstants.HORIZONTAL));
		
		pageEndBox = Box.createVerticalBox();
		pageEndBox.add(currentPointCost);
		currentPointCost.setAlignmentX(CENTER_ALIGNMENT);
		Box buttonsBox = Box.createHorizontalBox();
		buttonsBox.add(cancelButton);
		cancelButton.addActionListener(this);
		buttonsBox.add(acceptSelectionButton);
		acceptSelectionButton.addActionListener(this);
		pageEndBox.add(buttonsBox);
		this.add(pageEndBox, BorderLayout.PAGE_END);
		
		recalculatePoints(); // To set the currentPointCost-Label Text and just to be sure (in case we add default stuff or change something else)
		
		window.pack();
		window.setVisible(true);
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object i = e.getSource();
		if (i.equals(newChildButton)) {
			addNewChild();
		}
		else if (i.equals(acceptSelectionButton)) {
			recalculatePoints(); // Just to be sure an update to the points went missing.
			if (currentPoints <= startingPoints) {
				clientGameStartController.thisClientIsReady(getAllChildrenAsString());
			} else {
				System.err.println("The acceptSelectionButton wasn't disabled but the currentPoints > startingPoints!");
			}
		} else if (i.equals(cancelButton)) {
			leaveGame();
		} else {
			System.out.println("StartScreen#actionPerformed - different button clicked");
		}
	}
	
	/**
	 * This needs to be called whenever a user has canceled joining the game.
	 * (when a user quits it is registered by the server automatically)
	 * TODO: When the window is closed, this also has to be called!
	 */
	protected void leaveGame() {
		clientGameStartController.askToLeaveGame();
		
		//window.dispose(); Not needed because the answer to the leavg command should close it.
	}
	
	/**
	 * Closes the window without leaving the game.
	 */
	public void dispose() {
		window.dispose();
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
			acceptSelectionButton.setEnabled(false);
		} else
			acceptSelectionButton.setEnabled(true);
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
				characters += name + " '" + weaponName + "' ";
			}
		}
		characters = characters.trim(); // To remove last space
		characters += "]";
		return characters;
	}
	
	
	//region windowEvent
	@Override
	public void windowOpened(WindowEvent e) {
	
	}
	
	/**
	 * Called when the user tries to close the window
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		leaveGame();
	}
	
	/**
	 * Called when the window actually closed (e.g. via dispose()).
	 */
	@Override
	public void windowClosed(WindowEvent e) {
	
	}
	
	@Override
	public void windowIconified(WindowEvent e) {
	
	}
	
	@Override
	public void windowDeiconified(WindowEvent e) {
	
	}
	
	@Override
	public void windowActivated(WindowEvent e) {
	
	}
	
	@Override
	public void windowDeactivated(WindowEvent e) {
	
	}
	//endregion
}
