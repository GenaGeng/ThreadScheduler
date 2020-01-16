package test.account;

/**
 * @author GN
 * @description
 * @date 2019/12/11
 */
public class Deal {

    static int  opCounter = 0;

    static synchronized void counter(){
        opCounter++;
        System.out.println(Thread.currentThread().getName() + ",opCounter=" + opCounter);
    }

}
