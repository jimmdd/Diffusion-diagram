package SerialHeat;

import SimpleExecutor.SimpleExecutor;
import SimpleExecutor.SimpleTask;

public class Program {
      public class T1 extends SimpleTask {
        public void run() {
          //A();
        	System.out.println("A");
          se.submit(new T2());
        }
}
      public class T2 extends SimpleTask {
        public void run() {
        	//B();
        	System.out.println("B");

        }
}
      private SimpleExecutor se;
      public Program() {
        se = new SimpleExecutor();
}
      public void runIt() {
        SimpleTask t = new T1();
        se.submit(t);
        //C();
    	System.out.println("C");

        t.finish();
        //D();
    	System.out.println("D");

        se.terminate();
        //E();
    	System.out.println("E");

}
      public static void main(String[] args) {
        Program p = new Program();
        p.runIt();
} }

