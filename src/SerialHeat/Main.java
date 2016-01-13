package SerialHeat;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Main {

	public static void main(String[] args) {

		// Prompt the user for the size.
		InitialDialog id = new InitialDialog();
		int result = JOptionPane.showConfirmDialog(null, id, 
	               "Heat Diffusion Simulator", JOptionPane.OK_CANCEL_OPTION);
		
		// If the user selects "OK".
		if (result == JOptionPane.OK_OPTION) {
			
			// Spawn a GUI for the Simulator.
			SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	SGUI sg = new SGUI(id.getWidthOption(),id.getHeightOption());
	            	sg.createAndShowGUI(sg); //schedules the creation of the GUI to run on Java's
	            }						//EDT (Event Dispatch Thread)
	        });
			
			// If the user selects "Cancel".
		} else if (result == JOptionPane.OK_CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
			
			// Kill the program.
			System.exit(0);
		}
	}
	
}
