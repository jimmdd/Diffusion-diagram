/**
 * Wrapper class to simplify Java's Executor Framework
 */

package SimpleExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class SimpleExecutor {

    private ExecutorService e;  //This class wraps an ExecutorService

    private int cores;    //detected number of cores

    private class InteriorTask implements Runnable {
	//class for submission of SimpleTask objects to e
	
	private SimpleTask t;  

	public InteriorTask(SimpleTask t) {
	    this.t = t;
	}
	
	public void run() {
	    t.run();
	}
    }
	
    /**
     * Constructor: Creates an ExecutorService object with a fixed size thread pool.
     * 		This thread pool contains the same number of threads as available processors
     * 		on the machine.
     */
    public SimpleExecutor() {
	cores = Runtime.getRuntime().availableProcessors();
	e = Executors.newFixedThreadPool(cores);
    }
    
    /**
     * Method that both shuts down and terminates the
     * Executor. Shutting down cleans up the pool, while terminating
     * waits until the shutdown process is complete.  This method will
     * block until this happens.
     */
    public void terminate() {
	//call shutdown
	e.shutdown();
	try {
	    while(!e.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS)) //wait for Long.MAX_VALUE seconds
		;
	} catch (InterruptedException ex) {
	    System.err.println("Task Interrupted!");
	    System.exit(1);
	}	
    }

    /**
     * Submits a Callable task to the SimpleExecutor threadpool. Will return an
     * 		instance of type SimpleFuture<T>
     */
    public <T> SimpleFuture<T> submit(Callable<T> task) {
	SimpleFuture<T> val = new SimpleFuture<T>(e.submit(task));
	return val;
    }
    
    /**
     * Submits a Runnable task to the SimpleExecutor threadpool.
     */
    public void submit(SimpleTask task) {
	Runnable r = new InteriorTask(task);
	task.future = e.submit(r);
    }
    
    /**
     * returns true if the executor is shutdown, i.e. it will not accept new tasks
     */
    public boolean isShutdown() {
	return e.isShutdown();
    }
    
    /**
     * returns true if the executor is terminated; i.e. it is shut down AND all tasks have been finished
     */
    public boolean isTerminated() {
	return e.isTerminated();
    }

    /**
     * returns the number of cores detected
     */
    public int getCores() {
	return cores;
    }
}
