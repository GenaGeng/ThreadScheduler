package test.operation;

/**
 * @author GN
 * @description
 * @date 2019/12/23
 */
public class TimeState {
    static ThreadLocal<Long> t = new ThreadLocal<Long>();

//    public static void start(){
//        //set(T)方法为当前线程设置一个值
//        t.set(System.currentTimeMillis());
//    }
//
//    public static void end(){
//        // getStackTrace()[0]得到当前最recent的方法，getStackTrace()[1]是调用方法的类，getStackTrace()[2]代表调用序列中最远的一个方法
//        System.out.println("spends:" + (System.currentTimeMillis() - t.get()));//get(T)方法获得原始的赋值
//    }
    public static void countTime(){
        try {
            t.set(System.currentTimeMillis());
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getStackTrace()[2] + "spends:" + (System.currentTimeMillis()-t.get()));
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
