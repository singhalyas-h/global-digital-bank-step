package com.gdb.tests;

import com.gdb.domain.CurrentAccount;
import com.gdb.domain.FixedDepositAccount;
import com.gdb.domain.SalaryAccount;
import com.gdb.domain.SavingsAccount;

public class testSubclassAccounts {
    public static void main(String[] args) {
        try {
            FixedDepositAccount fixedDepositAccount = null;
            try {
                fixedDepositAccount = new FixedDepositAccount(67890, "Jane Smith", 40, 10000.0, "Current");
            } catch (IllegalArgumentException e) {
                System.out.println("Fixed deposit account could not be created: " + e.getMessage());
            }
            CurrentAccount currentAccount = new CurrentAccount(54321, "Alice Johnson", 25, 2000.0, "Current");
            SavingsAccount savingsAccount = new SavingsAccount(98765, "Bob Brown", 35, 3000.0, "Savings");
            SalaryAccount salaryAccount = null;
            try {
                salaryAccount = new SalaryAccount(12345, "John Doe", 30, 5000.0, "SALARY");
                salaryAccount.setEmployerName("Infosys");
            } catch (IllegalArgumentException e) {
                System.out.println("Salary account could not be created: " + e.getMessage());
            }

            System.out.println("Savings Account Created: Balance: " + savingsAccount.getBalance() + " | Min Balance: " + savingsAccount.getMinBalance()
                    + "\nCurrent Account Created: Overdraft Limit " + currentAccount.getOverDraftLimit()
                    + (fixedDepositAccount != null ? "\nFixed Deposit Account Created: Tenure " + fixedDepositAccount.getTenureMonths() + " months | Interest Rate: " + fixedDepositAccount.getInterestRate() + "%" : "")
                    + (salaryAccount != null ? "\nSalary Account Created: Employer " + salaryAccount.getEmployerName() : ""));
        } catch (IllegalArgumentException e) {
            System.out.println("Error creating Accounts: " + e.getMessage());
        }
    }
}
