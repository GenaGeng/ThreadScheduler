package test;

/**
 * @author GN
 * @description
 * @date 2019/12/10
 */
public class Counter {
    static int a = 0;
    static int b = 2;
    public static void main(String[] args) throws InterruptedException{

        new Thread(new Runnable() {
            public void run() {
                try{
                    Thread.sleep(10);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }

                a = 1;
            }
        }).start();

        Thread.sleep(10);

        if (a == 0){
            b =5;
        }

        System.out.println("a="+a+",b="+ b);
    }
}
