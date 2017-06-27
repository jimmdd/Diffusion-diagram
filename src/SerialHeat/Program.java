package SerialHeat;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import SimpleExecutor.SimpleExecutor;
import SimpleExecutor.SimpleTask;

public class Program {
    private Thread t1;
    private Thread t2;
    private Thread t3;
    private CyclicBarrier b;
    public class R1 implements Runnable {
      public void run() {
        A();
        t3.start();
        B();
        try {
			b.await();
		} catch (InterruptedException e) {
			System.out.println("error");
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			System.out.println("error");
			e.printStackTrace();
		}
        C();
} }
    public class R2 implements Runnable {
      public void run() {
        D();
        try {
			b.await();
		} catch (InterruptedException e) {
			System.out.println("error");
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			System.out.println("error");
			e.printStackTrace();
		}
        E();
        try {
			t3.join();
		} catch (InterruptedException e) {
			System.out.println("error");
			e.printStackTrace();
		}
        F();
} }
    public class R3 implements Runnable {
      public void run() {
G();
H(); }
}
    public class R4 implements Runnable {
      public void run() {
I(); }
}
    public void runIt() throws InterruptedException {
      t1 = new Thread(new R1());
      t2 = new Thread(new R2());
      t3 = new Thread(new R3());
      b = new CyclicBarrier(2, new R4());
      t1.start();
      t2.start();
      t1.join();
      t2.join();
}
    public void A(){
    	System.out.println("A");
    }
    public void B(){
    	System.out.println("B");
    }
    public void C(){
    	System.out.println("C");
    }
    public void D(){
    	System.out.println("D");
    }
    public void E(){
    	System.out.println("E");
    }
    public void F(){
    	System.out.println("F");
    }
    public void G(){
    	System.out.println("G");
    }
    public void H(){
    	System.out.println("H");
    }
    public void I(){
    	System.out.println("I");
    }
public static void main(String[] args) throws InterruptedException {
  Program p = new Program();
  p.runIt();
} }