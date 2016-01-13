package SerialHeat;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JPanel;

/**
 * This class is responsible for drawing the heat data found in the DataArray
 * class. It is a JPanel which contains a BufferedImage that stores the image
 * data.
 * 
 * @author Graf
 *
 */

public class GUIPanel extends JPanel {
	
	// Instance variables.
	private DataArray d;
	private BufferedImage pic;
	private int X;
	private int Y;
	private Color mouseColor;
	private int mouseX;
	private int mouseY;
	private int brushSize;
	
	// Constants.
	final int[] COLORS = {0xFF0000,0xFF0033,0xFF0055,0xFF0099,0xFF00A6, 0xFF00DD,
		0xFF00FB,0xE600FF,0xD400FF,0xBB00FF,0xAA00FF, 0x9900FF,
		0x7300FF,0x5D00FF,0x4000FF};	
	final int BLACK = 0x000000;
	final int RED = 0xFF0000;
	
	/**
	 * Constructor for this class.
	 * 
	 * @param data	The DataArray object to be used.
	 * @param x	The width of the simulation grid.
	 * @param y	The height of the simulation grid.
	 */
	public GUIPanel(DataArray data,int x, int y) {		
		// Create a BufferedImage for the "view".
		pic = new BufferedImage(x,y,BufferedImage.TYPE_INT_RGB);

		X = x;
		Y= y;
		d = data;
		brushSize = 3;
	}
	
	/**
	 * This method allows the surrounding JFrame to know how
	 * big a GUIPanel would like to be.
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(X,Y);
	}

	/**
	 * This method explains how a GUIPanel should be drawn.
	 */
	@Override
	public void paintComponent(Graphics g) {
		this.drawBoard(g);  // Delegate to our drawBoard() method.
		
		// Draw the mouse (if necessary).
		if (mouseColor != null) {
			Color tempCol = g.getColor();
			g.setColor(mouseColor);
			g.fillRect(mouseX - brushSize, mouseY - brushSize, (brushSize * 2), (brushSize * 2));
			
			g.setColor(tempCol);
		}
	}
	
	/**
	 * This method explains how a GUIPanel should be updated.
	 */
	@Override
	public void update(Graphics g) {
    	paintComponent(g);
    }
	
	/**
	 * This method defines how the BufferedImage should be drawn.
	 * It examines the values in DataArray and will hash a given
	 * value into an array of colors from blue(cold) to red(hot)
	 * 
	 * @param g	Graphics object to draw with.
	 */
	public void drawBoard(Graphics g) {
		
		// Grab the raw pixel data.
		DataBufferInt buf = (DataBufferInt) pic.getRaster().getDataBuffer();
		int[] pixels = buf.getData();
		double currElement;
		double[] currArr = d.getCurrentArray();
		boolean[] isInsulator = d.getInsulatorArray();
		
		// Iterate through the Current Data Array and paint pixels.
		for (int i=0;i<Y;i++) {
    		for (int j=0;j<X;j++) {
    			currElement = currArr[i*X+j];
				int col;
				if (isInsulator[i*X+j]) {
					col = 0x000000;
				} 	else {
					if (currElement > 1000)
						currElement = 1000;
					col = COLORS[((1000-(int)currElement)/71)];
				}
				pixels[i*X+j] = col;
        	}
    	}
		
		// Draw the new image in the GUIPanel.
		g.drawImage(pic,0,0,this);
    }
	
	/**
	 * Increment the brush size.
	 */
	public void increaseBrushSize() {
		brushSize++;
	}
	
	/**
	 * Decrement the brush size.
	 */
	public void decreaseBrushSize() {
		brushSize--;
	}
	
	/**
	 * Getter for brushSize.
	 * @return	The size of the brush.
	 */
	public int getBrushSize() {
		return brushSize;
	}
	
	/**
	 * Signals to this class that mouse is out of the pane.
	 */
	public void removeMouse() {
		mouseColor = null;
		mouseX = -1;
		mouseY = -1;
	}
	
	/**
	 * Lets this class know where the mouse is, and what color needs to be drawn.
	 * @param x	The x location of the mouse.
	 * @param y	The y location of the mouse.
	 * @param col	The color the mouse needs to be.
	 */
	public void setMouseLocation(int x, int y, Color col) {
		mouseX = x;
		mouseY = y;
		mouseColor = col;
	}
}
