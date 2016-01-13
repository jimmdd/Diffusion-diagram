/**
 * Wrapper class to simplify Java's Executor Framework
 */

package SimpleExecutor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SimpleFuture<T> {
    //this class wraps the Future<T> type
    Future<T> future;
    
    /**
     * Constructor
     */
    public SimpleFuture(Future<T> obj) {
	future = obj;
    }
    
    /**
     * Method will block on the current task and return a value(if the task is Callable)
     */
    public T get() {
	T val= null;		
	try {
	    val = future.get();
	} catch (InterruptedException e1) {
	    System.err.println("Task Interrupted!");
	    System.exit(1);
	} catch (ExecutionException e1) {
	    System.err.println("An Exception Occurred!");
	    System.exit(1);
	}
	return val;
    }
    
    /**
     * Returns true if the task is done
     */
    public boolean isDone() {
	return future.isDone();
    }
}
