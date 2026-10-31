package com.gdb.tests;

import com.gdb.domain.AccountEnhanced;

public class TestAccountEnhanced {
        public static void main(String[] args) {
                AccountEnhanced account = new AccountEnhanced(1001, "John Doe", 25, 0, 1000.0, "Savings");
                System.out.println("==================================================");
                System.out.println("ENHANCED ACCOUNT TEST (BOOLEAN RETURNS)");
                System.out.println("==================================================");
                System.out.println(">>> Test 1: Valid Account Creation");
                printAccountDetails(account);
                System.out.println(
                                ">>> Test 2: Invalid Age (under 18)\nCreating account with age 16\nAge auto-corrected to 18");
                AccountEnhanced account2 = new AccountEnhanced(1002, "Young Kid", 16, 0, 500.0, "Savings");
                printAccountDetails(account2);
                System.out.println(
                                ">>> Test 3: Invalid Account Type\nCreating account with type 'Invalid'\nAccount type defaulted to: 'Savings");
                AccountEnhanced account3 = new AccountEnhanced(1003, "Test User", 25, 0, 500, "Invalid");
                printAccountDetails(account3);
                System.out.println(
                                ">>> Test 4: Minimum Balance Enforcement on Creation\nCreating Savings account with Rs300 (below minimum)\nBalance auto-corrected to minimum: Rs500.0");
                AccountEnhanced account4 = new AccountEnhanced(1004, "Bob Wilson", 25, 0, 300, "Savings");
                printAccountDetails(account4);
                System.out.print(">>> Test 5: Withdrawal with Minimum Balance\nInitial: ");
                AccountEnhanced account5 = new AccountEnhanced(1005, "Alice Brown", 30, 0, 1200, "Current");
                printAccountDetails(account5);
                System.out.print("Withdrawing Rs200.0:"
                                + ((account5.withdraw(200, null)) ? "SUCCESS" : "FAILED (Minimum balance violation)")
                                + "\nNew Balance: Rs" + account5.getBalance() + "\nAfter withdrawal: ");
                printAccountDetails(account5);
                System.out.println("Withdrawing Rs 900 (would leave Rs100): "
                                + ((account5.withdraw(900, null)) ? "SUCCESS" : "FAILED (Minimum balance violation)")
                                + "\nCurrent balance: Rs" + account5.getBalance());
                System.out.print(">>> Test 6: Account Status Management\nInitial: ");
                AccountEnhanced account6 = new AccountEnhanced(1006, "Charlie Green", 35, 0, 2000, "Savings");
                printAccountDetails(account6);
                System.out.print("Closing account: "
                                + ((account6.closeAccount()) ? "SUCCESS" : "FAILED (Account already closed)")
                                + "\nAfter close: ");
                printAccountDetails(account6);
                System.out.println("Depositing Rs500 to closed account: "
                                + ((account6.deposit(500)) ? "SUCCESS" : "FAILED(Account inactive)")
                                + "\nReopening account: "
                                + ((account6.reopenAccount()) ? "SUCCESS" : "FAILED (Account already in open status)")
                                + "\nAfter reopen: ");
                printAccountDetails(account6);
                System.out.print(">>> Test 7: PIN Protection\nInitial: ");
                AccountEnhanced account7 = new AccountEnhanced(1007, "Diana Prince", 28, 1234, 1500, "Current");
                printAccountDetails(account7);
                System.out.println("Withdrawing Rs200 with correct PIN(1234): "
                                + ((account7.withdraw(200, 1234)) ? "SUCCESS" : "FAILED (Incorrect PIN)")
                                + "\nNew Balance: Rs" + account7.getBalance()
                                + "\nWithdrawing Rs200 with incorrect PIN(9999): "
                                + ((account7.withdraw(200, 9999)) ? "SUCCESS" : "FAILED (Incorrect PIN)")
                                + "\nWithdrawing Rs100 with PIN not set: "
                                + ((account7.withdraw(100, null)) ? "SUCCESS" : "FAILED (PIN not set)"));
                System.out.println(">>> Test 8: All Accounts Summary");
                printAccountDetails(account);
                printAccountDetails(account2);
                printAccountDetails(account3);
                printAccountDetails(account4);
                printAccountDetails(account5);
                printAccountDetails(account6);
                printAccountDetails(account7);
                System.out.println("==================================================");
                System.out.println("ENHANCED TEST COMPLETE");
                System.out.println("==================================================");
        }

        private static void printAccountDetails(AccountEnhanced account) {
                System.out.println("Account #" + account.getAccountNumber() + " | " + account.getName() + " ("
                                + account.getAge() + " yrs) | " + account.getAccountType() + " | \u20B9"
                                + account.getBalance() + " | "
                                + account.getStatus() + " | PIN: " + (account.hasPin() ? "Yes\n" : "No\n"));
        }
}
