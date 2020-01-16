package test.Counter;

public class Counter
{
    public static int counter;
    public final static int MAX=5;

    public static void main(String[] args) throws InterruptedException
    {

        counter = 0;
        Thread inc = new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i=0; i<MAX; i++){
                    counter = counter +1;
                }
            }
        });
        Thread dec = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<MAX; i++){
                    counter = counter - 2;
                }
            }
        });

        inc.start();
        dec.start();

        inc.join();
        dec.join();

        System.out.println(counter);
    }

}