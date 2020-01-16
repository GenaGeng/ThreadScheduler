package test.account;

/**
 * @author GN
 * @description
 * @date 2019/12/11
 */
public class Account {

    int checking, saving;

    public Account(int i,int j){
        checking = i;
        saving = j;
    }

    synchronized void transfer(int n){
        checking += n;
        System.out.println(Thread.currentThread().getName() + ",checking=" + checking);
//        Deal.counter();
        saving -= n;
        System.out.println(Thread.currentThread().getName() + ",saving=" + saving);
//        Deal.counter();
    }


}
