package SimpleExecutor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SimpleTask {
    
    /**
     * Future that can be used to wait for this task's completion
     * (null if task hasn't started yet)
     */
    Future<?> future;
    
    /**
     * Constructor
     * @param i 
     */

    public SimpleTask() {
    future = null;
	}


	/**
     * Method to overide when defining the task
     */
    public void run() {
    }
    
    /**
     * Returns true if the task is done
     */
    public boolean isDone() {
	if(future == null)
	    return false;
	return future.isDone();
    }

    /**
     * Blocks until the task is complete
     */
    public void finish() {
	try {
	    future.get();
	} catch (InterruptedException e1) {
	    System.err.println("SimpleTask interrupted during finish!");
	    System.exit(1);
	} catch (ExecutionException e1) {
	    System.err.println("An exception occurred while finishing SimpleTask!");
	    System.exit(1);
	}
    }
}
