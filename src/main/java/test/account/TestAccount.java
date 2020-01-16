package test.account;

/**
 * @author GN
 * @description
 * @date 2019/12/11
 */
public class TestAccount {

    public static void main(String[] args){

        final Account acc = new Account(4,12);

        Thread T1 = new Thread(){
            @Override
            public void run() {
                acc.transfer(2);
            }
        };

        Thread T2 = new Thread(){
            @Override
            public void run() {
                acc.transfer(5);
            }
        };

        T1.start();

        T2.start();
    }
}
