package test.account;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * @author Gena
 * @description
 * @date 2020/2/25 0025
 */
public class AccountTest {

    static Account x, y;



//    @Test
    public static void main(String[] args){

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                x.countInterest(0.01);
            }
        });

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                if (x.isLegal()) {

                    t2.start();

                    y = new Account(x);

                }
            }
        });

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