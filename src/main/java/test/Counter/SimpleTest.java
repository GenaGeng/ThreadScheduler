package test.Counter;

/**
 * @author Gena
 * @description
 * @date 2020/3/1 0001
 */
public class SimpleTest {

    public static int money;

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                money+=100;
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                money-=100;
            }
        });
        t1.start();
        t2.start();

        t1.join();
        t2.join();
        System.out.println(money);
    }

}
