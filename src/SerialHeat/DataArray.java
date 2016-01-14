/**
 * The DataArray class stores all of the data and methods necessary 
 * for running the heat simulation.
 * 
 * @author Graf
 *
 */

package SerialHeat;

public class DataArray {
	
	// Instance variables.
	private double[] currArr;
	private double[] nextArr;
	private boolean[] isHeater;
	private boolean[] isInsulator;
	private int X;
	private int Y;
	
	// Constants.
	private final double MAX_HEAT = 999;
	
	/**
	 * Constructor.
	 * 
	 * @param x	The width of the simulation area.
	 * @param y The height of the simulation area.
	 */
	public DataArray(int x, int y) {
		X = x;
		Y = y;
		currArr= new double[X*Y];
		nextArr = new double[X*Y];
		isHeater = new boolean[X*Y];
		isInsulator = new boolean[X*Y];
	}
	
	/**
	 * This method sets a region of cells' heat value to maximum (MAX_HEAT). It
	 * is currently not used in the application.
	 * 
	 * @param sx	The starting x location.
	 * @param sy	The starting y location.
	 * @param ex	The ending x location.
	 * @param ey	The ending y location.
	 */
	public void insertSquare(int sx, int sy, int ex, int ey) {
		
		// Check bounds.
		if (sx<0) {
			throw new IllegalArgumentException("Invalid x coordinate");
		} else if(sy<0) {
			throw new IllegalArgumentException("Invalid y coordinate");
		}	
		if (ex > X) {
			throw new IllegalArgumentException("Out of bounds!");
		} else if (ey > Y) {
			throw new IllegalArgumentException("Out of bounds!");
		}
		
		for (int i=sy;i<ey;i++) {
			for (int j=sx;j<ex;j++) {
				this.currArr[i*X+j] = MAX_HEAT;
			}
		}
	}
	
	/**
	 * This method sets all of the cells heat value to 0. It also
	 * removes all insulators and heaters.
	 */
	public void clear() {
		for (int i=0;i<Y;i++) {
			for (int j=0;j<X;j++) {
				this.currArr[i*X+j] = 0;
				this.isInsulator[i*X+j] = false;
				this.isHeater[i*X+j] = false;
			}
		}
	}
	
	/**
	 * This method labels a region of cells as a heater. These cells will maintain
	 * a heat value of MAX_HEAT and continuously pump heat into the simulation.
	 * 
	 * @param sx	The starting x location.
	 * @param sy	The starting y location.
	 * @param ex	The ending x location.
	 * @param ey	The ending y location.
	 */
	public void insertHeater(int sx, int sy, int ex, int ey) {
		
		// Check bounds.
		if (sx<0) {
			throw new IllegalArgumentException("Invalid x coordinate");
		} else if(sy<0) {
			throw new IllegalArgumentException("Invalid y coordinate");
		}
			
		if (ex > X) {
			throw new IllegalArgumentException("Out of bounds!");
		} else if (ey > Y) {
			throw new IllegalArgumentException("Out of bounds!");
		}
		
		for (int i=sy;i<ey;i++) {
			for (int j=sx;j<ex;j++) {
				this.isHeater[i*X+j] = true;
				this.currArr[i*X+j] = MAX_HEAT;
			}
		}
	}
	
	/**
	 * This method labels a region of cells as an insulator. These cells will have
	 * no heat value of MAX_HEAT perfectly reflect any heat diffused to them back to where
	 * it came from.
	 * 
	 * @param sx	The starting x location.
	 * @param sy	The starting y location.
	 * @param ex	The ending x location.
	 * @param ey	The ending y location.
	 */
	public void insertInsulator(int sx, int sy, int ex, int ey) {	
		
		// Check bounds.
		if (sx<0) {
			throw new IllegalArgumentException("Invalid x coordinate");
		} else if(sy<0) {
			throw new IllegalArgumentException("Invalid y coordinate");
		}	
		if (ex > X) {
			throw new IllegalArgumentException("Out of bounds!");
		} else if (ey > Y) {
			throw new IllegalArgumentException("Out of bounds!");
		}
		
		for (int i=sy;i<ey;i++) {
			for (int j=sx;j<ex;j++) {
				isInsulator[i*X+j] = true;
			}
		}
	}
	
	/* Helper method for diffuse(). This method takes the coordinates of a cell
	 * and then based on the heat values of that cell and the surrounding cells, 
	 * sets the cells value in the nextArr
	 */
	private void diffusePixel(int x, int y) {
		
		// Insulators have no heat to diffuse.
		if (isInsulator[y*X+x])
			return;

		double oldHeat;
		double neighborHeat = 0.0;
		
		// Heaters will always pump out max heat.
		if (isHeater[y*X+x])
			oldHeat = MAX_HEAT;
		else
			oldHeat = currArr[y*X+x];
		
		neighborHeat += oldHeat/4;
		
		double reflect = (oldHeat-neighborHeat)/8; //heat reflected if neighbor is insulator
		
		if (x!=0) {
			if (!isInsulator[y*X+x-1])
				neighborHeat += (currArr[y*X+x-1] * 3 / 32);
			else
				neighborHeat += reflect;
		}
		if (x!=X-1) {
			if (!isInsulator[y*X+x+1])
				neighborHeat += (currArr[y*X+x+1] * 3 / 32);
			else
				neighborHeat += reflect;
		}

		if (y!=0) {
			if (!isInsulator[(y-1)*X+x])
				neighborHeat += (currArr[(y-1)*X+x] * 3 / 32);
			else 
				neighborHeat += reflect;
		}
		if (y!=Y-1){
			if (!isInsulator[(y+1)*X+x])
				neighborHeat += (currArr[(y+1)*X+x] * 3 / 32);
			else 
				neighborHeat += reflect;
		}
		if (x!=0 && y!=0) {
			if (!isInsulator[(y-1)*X+x-1])
				neighborHeat += (currArr[(y-1)*X+x-1] * 3 / 32);
			else 
				neighborHeat += reflect;
		}
		if (x!=(X-1) && y!=0) {
			if (!isInsulator[(y-1)*X+x+1])
				neighborHeat += (currArr[(y-1)*X+x+1] * 3 / 32);
			else 
				neighborHeat += reflect;
		}
		if (x!=0 && y!=(Y-1)) {
			if (!isInsulator[(y+1)*X+x-1])
				neighborHeat += (currArr[(y+1)*X+x-1] * 3 / 32);
			else 
				neighborHeat += reflect;
		}
		if (x!=(X-1) && y!=(Y-1)) {
			if (!isInsulator[(y+1)*X+x+1])
				neighborHeat += (currArr[(y+1)*X+x+1] * 3 / 32);
			else 
				neighborHeat += reflect;
		}
		nextArr[y*X+x] = neighborHeat;
	}

	/**
	 * This method calls diffusePixel on every cell in the simulation. It 
	 * prepares nextArr to be swapped and painted and effectively advances
	 * the simulation by one time-step.
	 */
	public void diffuse(int startX, int endX) {	
		//this is what to do parallel
		//make a task here, 
		for (int y=0;y<Y;y++) {
			for (int x=startX;x<endX;x++) {
				//System.out.println("x is :" + x);
				diffusePixel(x,y);
			}
			//System.out.println("y is: "+ y);
		}
	}
		
	/**
	 *  Setter method for currArr
	 * @param arr	The array to be currArr.
	 */
	public void setA(double[] arr) {
		currArr = arr;
	}
	
	/**
	 * This method prints a text representation of currArr (not feasible for large arrays).
	 * 
	 * @param t
	 */
	public void printArr(boolean t) {
		if (t) {
			for (int y=0;y<Y;y++){	
				for (int x=0;x<X;x++) {
					if (currArr[y*X+x]<10) {
						System.out.printf("  %.3f ",currArr[y*X+x]);
					} else if(currArr[y*X+x]<100) {
						System.out.printf(" %.3f ",currArr[y*X+x]);
					} else {
						System.out.printf("%.3f ",currArr[y*X+x]);
					}
				}
				System.out.println();
			}
		}else {
			for (int y=0;y<Y;y++){	
				for (int x=0;x<X;x++) {
					if (nextArr[y*X+x]<10) {
						System.out.printf("  %.3f ",nextArr[y*X+x]);
					} else if(nextArr[y*X+x]<100) {
						System.out.printf(" %.3f ",nextArr[y*X+x]);
					} else {
						System.out.printf("%.3f ",nextArr[y*X+x]);
					}
				}
				System.out.println();
			}
		}
	}

	/**
	 * Method to add heat to the specified section of the array. The size
	 * of the section depends on the brushSize.
	 * 
	 * @param x	The x location of the hot spot.
	 * @param y	The y location of the hot spot.
	 * @param brushSize	The size of the brush drawing this object.
	 */
	public void drawHotSpot(int x, int y, int brushSize) {
		for (int i=y-brushSize;i<y+brushSize;i++) {
			int k=i;
			if (i<0)
				k=0;
			if (i>=this.Y)
				k = this.Y-1;
			for (int j=x-brushSize;j<x+brushSize;j++) {
				int l=j;
				if (j<0)
					l=0;
				if (j>=this.X)
					l = this.X-1;
				currArr[k*X+l] = MAX_HEAT;
			}
		}
	}
	
	/**
	 * Method to add an insulator to the specified section of the array. The size
	 * of the section depends on the brushSize.
	 * 
	 * @param x	The x location of the insulator.
	 * @param y	The y location of the insulator.
	 * @param brushSize	The size of the brush drawing this object.
	 */
	public void drawInsulator(int x, int y, int brushSize) {
		for (int i=y-brushSize;i<y+brushSize;i++) {
			int k =i;
			if (i<0)
				k=0;
			if (i>=this.Y)
				k = this.Y-1;
			for (int j=x-brushSize;j<x+brushSize;j++) {
				int l=j;
				if (j<0)
					l=0;
				if (j>=this.X)
					l = this.X-1;
				isInsulator[k*X+l] = true;
			}
		}
	}
	
	/**
	 * Method to add a heater to the specified section of the array. The size
	 * of the section depends on the brushSize.
	 * 
	 * @param x	The x location of the heater.
	 * @param y	The y location of the heater.
	 * @param brushSize	The size of the brush drawing this object.
	 */
	public void drawHeater(int x, int y, int brushSize) {
		for (int i=y-brushSize;i<y+brushSize;i++) {
			int k=i;
			if (i<0)
				k=0;
			if (i>=this.Y)
				k = this.Y-1;
			for (int j=x-brushSize;j<x+brushSize;j++) {
				int l = j;
				if (j<0)
					l=0;
				if (j>=this.X)
					l = this.X-1;
				isHeater[k*X+l] = true;
				currArr[k*X+l] = MAX_HEAT;
			}
		}
	}
	
	public double[] getCurrentArray(){
		return currArr;
	}
	
	public void setCurrentArray(double[] ca){
		this.currArr = ca;
	}
	
	public double[] getNextArray(){
		return nextArr;
	}
	
	public void setNextArray(double[] na){
		this.nextArr = na;
	}
	
	public boolean[] getInsulatorArray(){
		return isInsulator;
	}
}


