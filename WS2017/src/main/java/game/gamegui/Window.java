package game.gamegui;

import client.Client;
import game.ClientGameController;
import game.engine.Player;
import game.engine.Tile;
import game.engine.Weapon;
import game.engine.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * The Window containing the general info about how to display the Game.
 *
 * Created by flavia on 04.03.17.
 */
public class Window extends JFrame implements WindowListener {
	
	//region Data
	/**
	 * The ClientGameController of this game.
	 */
	private final ClientGameController clientGameController;
	
	/**
	 * @return {@link #clientGameController}.
	 */
	public ClientGameController getClientGameController() {
		return clientGameController;
	}
	
	/**
	 * The World this Window displays.
	 */
	private World world;
	
	/**
	 * @return {@link #world}.
	 */
	public World getWorld() {
		return world;
	}
	
	/**
	 * These Tiles represent the movement Range if a Character is selected.
	 * The keys are the Tiles, the values are the distance from the starting Tile.
	 */
	private HashMap<Tile, Integer> walkRangeTiles;
	
	/**
	 * @return {@link #walkRangeTiles}
	 */
	public HashMap<Tile,Integer> getWalkRangeTiles() {
		return walkRangeTiles;
	}
	
	/**
	 * Sets the {@link #walkRangeTiles} to the given HasMap. Updates all the graphics for the Tiles.
	 * @param walkRangeTiles The new walkRangeTiles (can be null).
	 */
	public void setWalkRangeTiles(HashMap<Tile, Integer> walkRangeTiles) {
		if (this.walkRangeTiles != null)
			for (Tile tile : this.walkRangeTiles.keySet())
				tile.setNeedsGraphicsUpdate();
		if (walkRangeTiles != null)
			for (Tile tile : walkRangeTiles.keySet())
				tile.setNeedsGraphicsUpdate();
		this.walkRangeTiles = walkRangeTiles;
	}
	
	/**
	 * These Tiles represent the attacking range if a Character's Weapon is selected.
	 */
	private Set<Tile> attackRangeTiles;
	
	/**
	 * @return {@link #attackRangeTiles}.
	 */
	public Set<Tile> getAttackRangeTiles() {
		return attackRangeTiles;
	}
	
	/**
	 * Sets the {@link #attackRangeTiles} to the given Set of Tiles. Updates all the graphics for the Tiles.
	 * @param attackRangeTiles The new attackRangeTiles (can be null).
	 */
	public void setAttackRangeTiles(Set<Tile> attackRangeTiles) {
		if (this.attackRangeTiles != null)
			for (Tile tile : this.attackRangeTiles)
				tile.setNeedsGraphicsUpdate();
		if (attackRangeTiles != null)
			for (Tile tile : attackRangeTiles)
				tile.setNeedsGraphicsUpdate();
		this.attackRangeTiles = attackRangeTiles;
	}
	
	/**
	 * The panel where the game map is drawn in.
	 */
	private MainGamePanel mainGamePanel;
	
	/**
	 * @return {@link #mainGamePanel}.
	 */
	public MainGamePanel getMainGamePanel() {
		return mainGamePanel;
	}
	
	/**
	 * The {@link GameInfoPanel} of this Window.
	 */
	private GameInfoPanel gameInfoPanel;
	
	/**
	 * @return {@link #gameInfoPanel}.
	 */
	public GameInfoPanel getGameInfoPanel() {
		return gameInfoPanel;
	}
	//endregion
	
	
	public Window(ClientGameController clientGameController, World world, String title) {
		this.clientGameController = clientGameController;
		this.world = world;
		
		this.setLayout(new BorderLayout());
		
		mainGamePanel = new MainGamePanel(this);
		this.add(mainGamePanel, BorderLayout.CENTER);
		
		gameInfoPanel = new GameInfoPanel(world, this);
		this.add(gameInfoPanel, BorderLayout.PAGE_END);
		
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(this);
		setTitle(title);
		
		
		setVisible(true);
		
		mainGamePanel.repaintImage();
		
	}
	
	
	
	
	
	/**
	 * This method selects the Weapon carried by the selected Character. The selected Character has to carry a weapon!
	 * TODO(M4) Cycle between Weapons if the Character has multiple Weapons.
	 */
	protected void selectWeapon() {
		world.setSelectionType(SelectionType.OWNED_WEAPON);
		Weapon selectedWeapon = world.getSelectedTile().getCharacter().getWeapon();
		if (selectedWeapon == null) {
			System.err.println("Window#selectWeapon - ERROR: selected Character carries no Weapon!");
		}
		attackRangeTiles = world.getSelectedTile().getAllTilesInRange(selectedWeapon.getRange(), false).keySet();
		for (Tile tile : attackRangeTiles) {
			tile.setNeedsGraphicsUpdate();
		}
	}
	
	/**
	 * Displays the directions in which the Character can push another Character.
	 */
	protected void selectPushing() {
		try {
			if (world.getSelectionType() == SelectionType.CHARACTER
					&& world.getSelectedTile().hasCharacter()) {
				mainGamePanel.setPushingSelected(true);
				attackRangeTiles = new HashSet<>();
				
				for (Tile tile : world.getSelectedTile().getNeighbours(false)) {
					if (tile != null
							&& tile.isWalkable(false)
							&& (tile.hasCharacter() == false || tile.getCharacter().isOnSameTeamAs(world.getSelectedTile().getCharacter()) == false)
							) {
						attackRangeTiles.add(tile);
					}
				}
				world.setSelectionType(SelectionType.OWNED_WEAPON);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @return The player which represent's this User in the game. Could be null if this User isn't playing the game.
	 */
	public Player getThisUsersPlayer() {
		for (Player player : world.getTurnController().getPlayers()) {
			if (player.getUser() == Client.getThisUser())
				return player;
		}
		return null;
	}
	
	
	
	/**
	 * Invoked when the user attempts to close the window
	 * from the window's system menu.
	 *
	 * @param e The WindowEvent
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		clientGameController.askToLeaveGame();
	}
	
	
	
	
	/**
	 * Invoked the first time a window is made visible.
	 *
	 * @param e The WindowEvent
	 */
	@Override
	public void windowOpened(WindowEvent e) {
	
	}
	
	/**
	 * Invoked when a window has been closed as the result
	 * of calling dispose on the window.
	 *
	 * @param e The WindowEvent
	 */
	@Override
	public void windowClosed(WindowEvent e) {
	
	}
	
	/**
	 * Invoked when a window is changed from a normal to a
	 * minimized state. For many platforms, a minimized window
	 * is displayed as the icon specified in the window's
	 * iconImage property.
	 *
	 * @param e The WindowEvent
	 * @see Frame#setIconImage
	 */
	@Override
	public void windowIconified(WindowEvent e) {
	
	}
	
	/**
	 * Invoked when a window is changed from a minimized
	 * to a normal state.
	 *
	 * @param e The WindowEvent
	 */
	@Override
	public void windowDeiconified(WindowEvent e) {
	
	}
	
	/**
	 * Invoked when the Window is set to be the active Window. Only a Frame or
	 * a Dialog can be the active Window. The native windowing system may
	 * denote the active Window or its children with special decorations, such
	 * as a highlighted title bar. The active Window is always either the
	 * focused Window, or the first Frame or Dialog that is an owner of the
	 * focused Window.
	 *
	 * @param e The WindowEvent
	 */
	@Override
	public void windowActivated(WindowEvent e) {
	
	}
	
	/**
	 * Invoked when a Window is no longer the active Window. Only a Frame or a
	 * Dialog can be the active Window. The native windowing system may denote
	 * the active Window or its children with special decorations, such as a
	 * highlighted title bar. The active Window is always either the focused
	 * Window, or the first Frame or Dialog that is an owner of the focused
	 * Window.
	 *
	 * @param e The WindowEvent
	 */
	@Override
	public void windowDeactivated(WindowEvent e) {
	
	}
}
