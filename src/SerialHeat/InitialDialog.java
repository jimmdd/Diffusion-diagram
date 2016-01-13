package SerialHeat;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class allows for the creation of the initial dialog box.
 * We are going to use it to prompt the user for the size of
 * the simulation area.
 * 
 * @author Graf
 *
 */

public class InitialDialog extends JPanel{

	// Instance variables.
	private JComboBox<Integer> widthComboBox;
	private JComboBox<Integer> heightComboBox;
	
	// Constants.
	private final Integer[] OPTIONS = {100,200,300,400,500,600,700,800,900};
	private final int WIDTH_SELECTED_INDEX = 8;
	private final int HEIGHT_SELECTED_INDEX = 5;
	
	/**
	 * Class constructor.
	 */
	public InitialDialog()
	{
		// Create the drop-down menus.
		widthComboBox = new JComboBox<Integer>(OPTIONS);
		heightComboBox = new JComboBox<Integer>(OPTIONS);
		widthComboBox.setSelectedIndex(WIDTH_SELECTED_INDEX);
		heightComboBox.setSelectedIndex(HEIGHT_SELECTED_INDEX);
		
		// Add the width drop-down.
		this.add(new JLabel("Width:"));
		this.add(widthComboBox);
		
		// Give ourselves some space.
		this.add(Box.createHorizontalStrut(15));
		
		// Add the height drop-down.
		this.add(new JLabel("Height:"));
		this.add(heightComboBox);	
	}
	
	/**
	 * Getter for the width JComboBox 
	 * 
	 * @return	The width the user selected.
	 */
	public int getWidthOption() {
		return (Integer)widthComboBox.getSelectedItem();
	}
	
	/**
	 * Getter for the height JComboBox 
	 * 
	 * @return	The height the user selected.
	 */
	public int getHeightOption() {
		return (Integer)heightComboBox.getSelectedItem();
	}
}
