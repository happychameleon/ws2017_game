package game.gamegui;

import game.engine.Tile;
import game.engine.Weapon;
import game.engine.World;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Set;

/**
 * The Window containing the general info about how to display the Game.
 *
 * TODO: Create Pane, draw everything in a panel, add panel to center in pane.
 * TODO: Additional Info pane at the bottom with pane.add(component, BorderLayout.PAGE_END), first pane.setLayout(new BorderLayout())
 *
 * Created by flavia on 04.03.17.
 */
public class Window extends JFrame {
	
	//region Data
	/**
	 * The World this Window displays.
	 */
	private World world;
	
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
	
	public void setWalkRangeTiles(HashMap<Tile, Integer> walkRangeTiles) {
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
	
	public void setAttackRangeTiles(Set<Tile> attackRangeTiles) {
		this.attackRangeTiles = attackRangeTiles;
	}
	
	/**
	 * The panel where the game map is drawn in.
	 */
	private MainGamePanel mainGamePanel;
	
	/**
	 * The {@link GameInfoPanel} of this Window.
	 */
	private GameInfoPanel gameInfoPanel;
	//endregion
	
	
	public Window(World world, String title) {
		this.world = world;
		
		this.setLayout(new BorderLayout());
		
		mainGamePanel = new MainGamePanel(world, this);
		this.add(mainGamePanel, BorderLayout.CENTER);
		
		gameInfoPanel = new GameInfoPanel(world, this);
		this.add(gameInfoPanel, BorderLayout.PAGE_END);
		
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle(title);
		
		setVisible(true);
		
		mainGamePanel.repaintImage();
		
	}
	
	
	
	
	
	/**
	 * This method selects the Weapon carried by the selected Character. The selected Character has to carry a weapon!
	 * TODO: Cycle between Weapons if the Character has multiple Weapons.
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
	
	
}
