package test.Simple;

/**
 * @author Gena
 * @description
 * @date 2020/2/25 0025
 */
public class Account {

    double checking = 0;

    double saving = 0;

    Account(double c, double s){

        setChk(c);

        setSav(s);
    }

    Account(Account acc){

        setEqChk(acc);

        setEqSav(acc);
    }

    boolean isLegal(){

        return (checking >= 0) && (saving >= 0);
    }

    synchronized void countInterest(double rate){

        setChk(getChk() * (1.0 + rate));

        setSav(getSav() * (1.0 + rate));
    }

    synchronized void setChk(double c){

        checking = c;
    }

    synchronized double getChk(){
        return checking;
    }

    synchronized void setSav(double s){
        saving = s;
    }

    synchronized double getSav(){
        return saving;
    }

    synchronized void setEqChk(Account acc){
        setChk(acc.checking);
    }

    synchronized void setEqSav(Account acc){
        setSav(acc.saving);
    }
}
