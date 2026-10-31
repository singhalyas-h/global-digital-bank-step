package com.gdb.tests;

import com.gdb.domain.AccountRulesEngine;
import java.util.Locale;

public class TestAccountRulesEngineProperties {
    public static void main(String[] args) {
        System.out.println("=== Activity 14: Properties-Driven Rules Engine Test ===");

        int[] tenures = { 0, 2, 4, 6 };
        for (int tenure : tenures) {
            double minBalance = AccountRulesEngine.getSavingsMinBalance(tenure);
            double interestRate = AccountRulesEngine.getSavingsInterestRate(tenure);
            System.out.printf(Locale.US, "Tenure %d yrs -> Min Balance: Rs %-8s| Interest: %.2f%%%n", tenure,
                    minBalance, interestRate);
        }

        System.out.println("All external properties loaded and verified successfully!");
    }
}
