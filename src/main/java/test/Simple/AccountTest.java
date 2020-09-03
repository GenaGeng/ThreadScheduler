package test.Simple;

import junit.framework.TestCase;
import org.junit.Test;


public class AccountTest extends TestCase {

    Account x, y;

    Thread t1 = new Thread(){

        @Override
        public void run(){

//            System.out.println("t1 Thread");
            if (x.isLegal()) {

                t2.start();

                y = new Account(x);

//                try {
//                    Thread.sleep(1000);
//                    System.out.println("先改变x");
//                }catch (InterruptedException e){
//                    e.printStackTrace();
//                }
            }
        }
    };

    Thread t2 = new Thread(){

        @Override
        public void run(){

//            System.out.println("t2 Thread");
//            try {
//                Thread.sleep(2000);
//                System.out.println("先不算x");
//            }catch (InterruptedException e){
//                e.printStackTrace();
//            }

            x.countInterest(0.01);
        }
    };

    @Test
    public void testCase(){

        x = new Account(400,700);

        t1.start();

//        try {
//            Thread.sleep(5000);
//            System.out.println(y.checking +";" + y.saving);
//        }catch (InterruptedException e){
//            e.printStackTrace();
//        }
    }
}