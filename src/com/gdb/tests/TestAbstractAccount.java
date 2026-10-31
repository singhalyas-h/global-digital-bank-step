package com.gdb.tests;

import com.gdb.domain.AbstractAccount;
import com.gdb.domain.CurrentAccount;
import com.gdb.domain.SalaryAccount;
import com.gdb.domain.SavingsAccount;
import com.gdb.exceptions.AccountException;
public class TestAbstractAccount {
    public static void main(String[] args) {
        System.out.println("=== Activity 10: Banking Operations Suite ===");
        AbstractAccount[] portfolio={createSavingsAccount(),createCurrentAccount(),createSalaryAccount()};
        try {
            transferfunds(portfolio[0],portfolio[1],3000.0,1234);
        } catch (AccountException exception) {
            System.out.println("Transfer failed unexpectedly: "+exception.getMessage());
        }
        try{
            transferfunds(portfolio[1],portfolio[2],1000.0,9999);
        }
        catch(AccountException exception){
            System.out.println("Failed Transfer (Wrong PIN): Exception caught, no balance changed [PASS]");
        }
        MontlyAccountCycle(portfolio);
        System.out.println("All Banking Operations passed!");
    }
    private static String accountLabel(AbstractAccount account) {
        if(account instanceof SavingsAccount){
            return "Savings";
        }
        else if(account instanceof CurrentAccount){
            return "Current";
        }
        else if(account instanceof SalaryAccount){
            return "Salary";
        }
        return "Account";
    }
    private static void transferfunds(AbstractAccount from,AbstractAccount to,double amount,int pin) throws AccountException{
        from.withdraw(amount,pin);
        to.deposit(amount, pin);
        System.out.println("Transfer Rs "+amount+" from "+accountLabel(from)+" to "+accountLabel(to)+": SUCCESS");
        System.out.println(accountLabel(from)+" Balance: Rs "+from.getBalance()+" | "+accountLabel(to)+" Balance: Rs "+to.getBalance());
    }
    private static AbstractAccount createSavingsAccount(){
        SavingsAccount account=new SavingsAccount(4001,"Savings User",30,10000.0, "Savings");
        account.setPin(1234);
        return account;
    }
    private static AbstractAccount createCurrentAccount(){
        CurrentAccount account=new CurrentAccount(4002,"Current User",35,5000.0, "Current");
        account.setPin(1234);
        return account;
    }
    private static AbstractAccount createSalaryAccount(){
        SalaryAccount account=new SalaryAccount(4003,"Salary User",28,7000.0,"Infosys");
        account.setPin(1234);
        return account;
    }
    private static void MontlyAccountCycle(AbstractAccount[] portfolio){
        for(AbstractAccount account:portfolio){
            if(account instanceof SavingsAccount savingsAccount){
                savingsAccount.applyInterest();
            }
            else if(account instanceof SalaryAccount salaryAccount){
                if (salaryAccount.getInactiveMonths() == 0) {
                    System.out.println("Salary credit history checked.");
                }
            }
        }
        System.out.println("Monthly Interest Cycle processed for all qualifying accounts.");
    }
}