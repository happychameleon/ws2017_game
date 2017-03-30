package game.startscreen;

import game.engine.Weapon;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * This Panel represents one Child on the {@link StartScreen}.
 * On this panel you can add a weapon or other stuff to the child.
 *
 * Created by flavia on 28.03.17.
 */
public class ChildPanel extends JPanel implements ActionListener, PopupMenuListener {
	
	/**
	 * Calculates the amount this Child with all it's currently selected gear costs.
	 * @return This Child's current point cost.
	 */
	protected int getPointCost() {
		return baseChildCost + ((Weapon)weaponSelection.getItemAt(weaponSelection.getSelectedIndex())).getPointCost();
	}
	
	/**
	 * How many Points a Child costs without any gear.
	 */
	protected static final int baseChildCost = 10;
	
	/**
	 * The screen this is in.
	 */
	private final StartScreen startScreen;
	
	/**
	 * The separator created for this ChildPanel. Used when removing this to also remove the separator.
	 */
	private final JSeparator separator;
	
	/**
	 * The name the user can choose for this Child.
	 */
	private JTextField nameTextField = new JTextField("Enter a Name");
	
	/**
	 * @return The name entered in the nameTextField, without any space or ' characters.
	 */
	public String getCharacterName() {
		String name = "";
		for (char c : nameTextField.getText().toCharArray()) {
			if (c != ' ' && c != '\'')
				name += c;
		}
		if (name.isEmpty() == false) {
			return name;
		} else {
			return names[0];
		}
	}
	
	/**
	 * One of these names is randomly proposed as the name of the Child.
	 */
	static final String[] names = new String[] {"Chloe", "Max", "Rachel", "Kate", "Frank", "Mark", "Nathan", "Victoria", "Warren", "Brooke", "Luke", "Dana", "Alyssa", "Joyce", "Trevor", "Samuel"};
	
	/**
	 * Deletes this panel and updates the points available in the StartScreen.
	 */
	private JButton removeChildButton = new JButton("Remove this Child");
	
	/**
	 * A drop down menu to choose the weapon from.
	 */
	private JComboBox weaponSelection;
	
	/**
	 * @param startScreen the startScreen this is displayed in.
	 * @param separator The {@link #separator}.
	 */
	public ChildPanel(StartScreen startScreen, JSeparator separator) {
		this.startScreen = startScreen;
		this.separator = separator;
		
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(nameTextField);
		int randIndex = new Random().nextInt(names.length);
		nameTextField.setText(names[randIndex]);
		
		createWeaponSelection();
		weaponSelection.addActionListener(this);
		this.add(weaponSelection);
		
		this.add(removeChildButton);
		removeChildButton.addActionListener(this);
	}
	
	/**
	 * Creates the Weapon Selection Box with the Weapons as Objects and custom Strings,
	 * but without using the toString() method (link explains why).
	 *
	 * Taken from http://stackoverflow.com/questions/19094845/item-in-jcombobox-instance-of-an-object.
	 */
	private void createWeaponSelection() {
		weaponSelection = new JComboBox(new DefaultComboBoxModel(Weapon.getWeaponPrototypesArray()));
		weaponSelection.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if(value instanceof Weapon){
					Weapon weapon = (Weapon) value;
					setText(weapon.getStartScreenString());
				}
				return this;
			}
		} );
		weaponSelection.addPopupMenuListener(this);
	}
	
	/**
	 * @return The weapon selected for this Character.
	 */
	public Weapon getCharacterWeapon() {
		return (Weapon)weaponSelection.getSelectedItem();
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == removeChildButton) {
			startScreen.removeChild(this, separator);
		}
		
	}
	
	
	/**
	 * Recalculates the points when the selected gear changed.
	 */
	@Override
	public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
		
		if (e.getSource() == weaponSelection) {
			startScreen.recalculatePoints();
		}
		
	}
	
	
	@Override
	public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
	}
	
	@Override
	public void popupMenuCanceled(PopupMenuEvent e) {
	
	}
	
	
}
