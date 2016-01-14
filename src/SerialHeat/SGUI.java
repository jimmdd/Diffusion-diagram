package SerialHeat;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import SimpleExecutor.SimpleExecutor;
import SimpleExecutor.SimpleTask;

/**
 * This class creates the GUI the user can interact with.
 * 
 * @author Graf
 *
 */

public class SGUI implements MouseListener, MouseMotionListener  {
	
	// Instance variables.
	private GUIPanel gp;
	private DataArray d;
	private JButton runB;
	private JButton timeStepB;
	private JButton exitB;
	private JButton hotSpotB;
	private JButton insulatorB;
	private JButton heaterB;
	private JButton clearB;
	private JButton aboutB;
	private JButton brushUpB;
	private JButton brushDownB;
	private static JTextField timeStepBox;
	private int timeSteps = 5000;
	private boolean heaterButtonActive = false;
	private boolean insulatorButtonActive = false;
	private boolean hotSpotButtonActive = false;
	private int guiWidth;
	private int guiHeight;
	private boolean isRunning;
	public int count=0;
	
	// Constants.
	private final int HORIZONTAL_PADDING = 18;
	private final int VERTICAL_PADDING = 75;
	private final int BUTTON_HEIGHT = 26;
	private final int BUTTON_WIDTH = 115;
	private final int MIN_RUN_WIDTH = 60;
	private final String ABOUT_MESSAGE = "Heat Diffusion Simulator\n\nCreated by: Michael Graf"
			+ " and David Bunde\nA project to illustrate the the benefits of parallelism in Java.";

	/**
	 * Class constructor.
	 * 
	 * @param x	The width of the simulation area.
	 * @param y	The height of the simulation area.
	 */
	public SGUI(int x, int y) {
		guiWidth = x;
		guiHeight = y;
		isRunning = false;
	}
	
	/** This method creates the JFrame and its components. It adds them to the frame. Once the JFrame
	 *  has been completed, this method will implicitly loop until the application is explicitly killed.
	 * 
	 */
	public void createAndShowGUI(SGUI sg) {
		
		// JFrame.
		JFrame frame = new JFrame();
		frame.setTitle("Heat Diffusion Simulator");	
		
		// Create array to store baseline components.
		ArrayList<JComponent> componentArr = new ArrayList<>();
				
		/* Buttons. 
		 * We are using Lambda Expressions for the listeners. 
		 */
		
		// Hot spot button.
		hotSpotB = new JButton("Add Hot Spot");
		hotSpotB.addActionListener( e -> {
			try {
				gp.addMouseListener(sg);
				if (!hotSpotButtonActive) {				
					hotSpotButtonActive = true;
					hotSpotB.setBackground(Color.red);
					if (insulatorButtonActive) {
						insulatorButtonActive = false;
						insulatorB.setBackground(null);
					}
					if (heaterButtonActive) {
						heaterButtonActive = false;
						heaterB.setBackground(null);
					}
					gp.validate();
					gp.repaint();
					gp.addMouseListener(sg);
					gp.addMouseMotionListener(sg);
				} else {
					hotSpotButtonActive = false;
					gp.removeMouseListener(sg);
					gp.removeMouseMotionListener(sg);
					hotSpotB.setBackground(null);
					gp.validate();
					gp.repaint();
				}
			} catch(Exception exc) {
				
			}
		});
		componentArr.add(hotSpotB);
		
		// Heater button.
		heaterB = new JButton("Add Heater");
		heaterB.addActionListener( e -> {
			try {
				if (!heaterButtonActive) {				
					heaterButtonActive = true;
					heaterB.setBackground(Color.red);
					if (insulatorButtonActive) {
						insulatorButtonActive = false;
						insulatorB.setBackground(null);
					}
					if (hotSpotButtonActive) {
						hotSpotButtonActive = false;
						hotSpotB.setBackground(null);
					}
					gp.validate();
					gp.repaint();
					gp.addMouseListener(sg);
					gp.addMouseMotionListener(sg);
				} else {
					heaterButtonActive = false;
					gp.removeMouseListener(sg);
					gp.removeMouseMotionListener(sg);
					heaterB.setBackground(null);
					gp.validate();
					gp.repaint();
				}
			} catch(Exception exc) {
				
			}
		});
		componentArr.add(heaterB);

		// Insulator button.
		insulatorB = new JButton("Add Insulator");
		insulatorB.addActionListener( e-> {
			try {
				if (!insulatorButtonActive) {				
					insulatorButtonActive = true;
					insulatorB.setBackground(Color.red);
					if (heaterButtonActive) {
						heaterButtonActive = false;
						heaterB.setBackground(null);
					}
					if (hotSpotButtonActive) {
						hotSpotButtonActive = false;
						hotSpotB.setBackground(null);
					}
					gp.validate();
					gp.repaint();
					gp.addMouseListener(sg);
					gp.addMouseMotionListener(sg);
				} else {
					insulatorButtonActive = false;
					gp.removeMouseListener(sg);
					gp.removeMouseMotionListener(sg);
					insulatorB.setBackground(null);
					gp.validate();
					gp.repaint();
				}
			} catch(Exception exc) {

			}
		});
		componentArr.add(insulatorB);

		// Brush+ button.
		brushUpB = new JButton("Brush +");
		brushUpB.addActionListener( e -> { gp.increaseBrushSize(); });
		componentArr.add(brushUpB);

		// Brush- button.
		brushDownB = new JButton("Brush -");
		brushDownB.addActionListener( e-> { gp.decreaseBrushSize(); });
		componentArr.add(brushDownB);

		// Clear button.
		clearB = new JButton("Clear");
		clearB.addActionListener( e-> {
				d.clear();
				gp.repaint();
		});
		componentArr.add(clearB);
		
		// Timestep button.
		timeStepB = new JButton("# Steps");
		timeStepB.addActionListener( e -> {
			String str =JOptionPane.showInputDialog(frame, "Enter Number of Timesteps");
			try {		
				int steps = Integer.parseInt(str);
				this.timeSteps = steps;
				timeStepBox.setText("Timesteps: 0/" +timeSteps);
			} catch(Exception exc) {
				JOptionPane.showMessageDialog(frame,"Incorrect Input!\nPlease Enter An Integer.");
			}
		});
		componentArr.add(timeStepB);	

		// Run button.
		runB = new JButton("Run");
		runB.addActionListener( e -> {
			try {
				if (isRunning) {
					isRunning = false;
				} else {
					isRunning = true;

					// Kick off the sim in a new thread.
					Thread r = new Thread()
					{
						public void run()
						{
							sg.runSim();
						}
					};

					r.start();
				}

			} catch(Exception exc) {

			} finally {
				this.toggleRunButton();
			}
		});
		componentArr.add(runB);	

		// About button.
		aboutB = new JButton("About");
		aboutB.addActionListener( e-> {
			JOptionPane.showMessageDialog(frame,ABOUT_MESSAGE);
		});
		componentArr.add(aboutB);	
			
		// Exit button.
		exitB = new JButton("Exit");
		exitB.addActionListener( e-> {
			System.exit(0);
		});
		componentArr.add(exitB);	
		
		// Text field.
		timeStepBox = new JTextField("Time: 0/" +timeSteps,10);
		timeStepBox.setEditable(false);
		componentArr.add(timeStepBox);	
				
		// Grab Content Pane.
		Container pane = frame.getContentPane();
		pane.setLayout(new GridBagLayout());
				
		// Set image constraints.
		GridBagConstraints ic = new GridBagConstraints();
		ic.anchor= GridBagConstraints.CENTER;
		ic.gridwidth = 11;
		ic.gridheight = 11;
		ic.gridx =0;
		ic.gridy = 0;
		ic.weightx = 1;
		ic. weighty = 1;
		
		// Create a DataArray for the "model".
		d= new DataArray(guiWidth,guiHeight);
		
		// Create a GUIPanel for the "view".
		gp = new GUIPanel(d,guiWidth, guiHeight);

		// Add things to the pane.
		gp.setDoubleBuffered(true);
		pane.add(gp,ic);
		this.addBaselineComponents(pane, componentArr);

		// Finalize JFrame.
		frame.setSize(guiWidth + HORIZONTAL_PADDING,guiHeight + VERTICAL_PADDING);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();	
		frame.setMinimumSize(frame.getSize());
	}
	
	/**
	 *  This method will run the heat simulation for the specified number of time steps. It will turn off
	 *   any active buttons prior to starting.
	 * 
	 */
	public class MyTask extends SimpleTask{
		private int startX;
		private int endX;
		
		public MyTask(int startX, int endX) {
			super();
			this.startX = startX;
			this.endX = endX;

		}


		@Override
		public void run() {//update part of the board
				// Calculate the heat values for the next time step.
				//calculating part of the stuff
				d.diffuse(startX, endX);		
		}
		
	}
	public void runSim() {	
		// Check which button are active.
				long start = System.currentTimeMillis();
				if (heaterButtonActive) {
					heaterButtonActive = false;
					heaterB.setBackground(null);
					gp.removeMouseListener(this);
					gp.removeMouseMotionListener(this);
					gp.validate();
					gp.repaint();
				} else if (hotSpotButtonActive) {
					hotSpotButtonActive = false;
					hotSpotB.setBackground(null);
					gp.removeMouseListener(this);
					gp.removeMouseMotionListener(this);
					gp.validate();
					gp.repaint();
				} else if (insulatorButtonActive) {
					insulatorButtonActive = false;
					insulatorB.setBackground(null);
					gp.removeMouseListener(this);
					gp.removeMouseMotionListener(this);
					gp.validate();
					gp.repaint();
				}
				
				int x = guiWidth;
			    double[] temp;
				SimpleExecutor e = new SimpleExecutor();
			
				MyTask t1 = new MyTask(0,x/2);
				MyTask t2 = new MyTask(x/2,x);
				
				// Begin running the simulation.
				for (int i=0;i<timeSteps;i++) {
					// Check if the sim has stopped.
					if (!isRunning) {
						isRunning = false;
						return;
					}
					
					// Update the time step counter.
					timeStepBox.setText("Time: "+(i+1)+"/"+timeSteps);
					

					//submit them to simple executor
					e.submit(t1);
					e.submit(t2);
					
					t1.finish();
					t2.finish();
					
					// Rotate the backing arrays.
					temp = d.getCurrentArray();
					d.setCurrentArray(d.getNextArray());
					d.setNextArray(temp);
					count++;
					// Repaint the JPanel.
					gp.validate();
					gp.repaint();
					
				}
			    System.out.println("total running times: "+count);
			    
				// Turn off the run button.
				runB.setBackground(null);
				gp.validate(); 
				gp.repaint();
				System.out.println("Sim took " + (System.currentTimeMillis() - start));
			}
	
		
	
	/* Helper method to add the buttons to the JFrame */
	private void addBaselineComponents(Container pane, ArrayList<JComponent> list) {
		GridBagConstraints gbc;
		int len = list.size();
		
		// What is the shape of the frame?
		if (guiWidth > guiHeight) {
			for (int i=0; i < len; i++) {
				
				// Create the constraints.
				gbc = new GridBagConstraints();
				gbc.anchor= GridBagConstraints.NORTHWEST;
				gbc.gridwidth = 1;
				gbc.gridheight = GridBagConstraints.REMAINDER;
				gbc.gridx =i;
				gbc.gridy = 11;
				gbc.weightx = 0;
				gbc.weighty = 1;
				
				pane.add(list.get(i),gbc);
			}
			
			// Fix the run button.
			list.get(7).setPreferredSize(new Dimension(MIN_RUN_WIDTH,BUTTON_HEIGHT));
		} else if (guiWidth < guiHeight) {
			for (int i=0; i < len; i++) {
				
				// Create the constraints.
				gbc = new GridBagConstraints();
				gbc.anchor= GridBagConstraints.NORTHWEST;
				gbc.gridwidth = GridBagConstraints.REMAINDER;
				gbc.gridheight = 1;
				gbc.gridx =11;
				gbc.gridy = i;
				gbc.weightx = 1;
				gbc.weighty = 0;
				
				pane.add(list.get(i),gbc);
			}
			for (JComponent comp : list){
				Dimension dim = new Dimension(BUTTON_WIDTH,BUTTON_HEIGHT);
				comp.setPreferredSize(dim);
				comp.setMinimumSize(dim);
				comp.setMaximumSize(dim);
			}
		// We have a square frame.
		} else {
			for (int i=0; i < len; i++) {
				gbc = new GridBagConstraints();
				gbc.anchor= GridBagConstraints.NORTHWEST;
				gbc.gridwidth = 1;
				gbc.gridheight = GridBagConstraints.REMAINDER;
				gbc.gridx =i;
				gbc.gridy = 11;
				gbc.weightx = 1;
				gbc.weighty = 1;
				
				pane.add(list.get(i),gbc);
			}
			// Fix the run button.
			list.get(7).setPreferredSize(new Dimension(MIN_RUN_WIDTH,BUTTON_HEIGHT));
		}
	}
	
	/* Helper method that takes care of graphical side of toggling run button */
	private void toggleRunButton() {
		if (runB.getText().equals("Run")) {
			runB.setBackground(Color.RED);
			runB.setText("Stop");
		} else {
			runB.setBackground(null);
			runB.setText("Run");
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {		
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
			
	}

	@Override
	public void mouseExited(MouseEvent e) {
		gp.removeMouse();	
		gp.repaint();
	}

	@Override
	public void mousePressed(final MouseEvent e) {

	}
	

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (heaterButtonActive) {
			d.drawHeater(e.getX(),e.getY(),gp.getBrushSize());
			gp.repaint();	
		} else if (insulatorButtonActive) {
			d.drawInsulator(e.getX(),e.getY(),gp.getBrushSize());
			gp.repaint();
		} else if (hotSpotButtonActive) {
			d.drawHotSpot(e.getX(),e.getY(),gp.getBrushSize());
			gp.repaint();
		}
	}

	public void mouseMoved(MouseEvent e) {
		
		if (heaterButtonActive) {	
			int x =e.getX();
			int y = e.getY();
			gp.setMouseLocation(x,y,Color.RED);
			gp.repaint();
		} else if (insulatorButtonActive) {
			int x =e.getX();
			int y = e.getY();
			gp.setMouseLocation(x,y,Color.BLACK);
			gp.repaint();
		} else if (hotSpotButtonActive) {
			int x =e.getX();
			int y = e.getY();
			gp.setMouseLocation(x,y,Color.RED);
			gp.repaint();
		}
	}
}
