package test.operation;

/**
 * @author GN
 * @description
 * @date 2019/12/16
 */

public class SecurityChecker {

    public static boolean checkSecurity(){

        System.out.println("SecurityCheck.checkSecurity...");

        if ((System.currentTimeMillis() & 0x1) == 0){
            return false;
        }else {
            return true;
        }
//        return (System.currentTimeMillis() & 0x1) == 0;
    }
}
