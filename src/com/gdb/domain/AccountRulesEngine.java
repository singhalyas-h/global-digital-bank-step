package com.gdb.domain;

import java.time.LocalDateTime;
import java.util.*;

public class AccountRulesEngine {

    public enum TenureTier {
        NEW,
        STANDARD,
        PREMIUM,
        PRIVILEGE
    }

    private static final String SAVINGS_CONFIG_PATH = "src/main/resources/config/rules/savings.properties";
    private static final String CURRENT_CONFIG_PATH = "src/main/resources/config/rules/current.properties";
    private static final String FD_CONFIG_PATH = "src/main/resources/config/rules/fixeddeposit.properties";
    private static final String SALARY_CONFIG_PATH = "src/main/resources/config/rules/salary.properties";

    private static final AccountRulesEngine INSTANCE = new AccountRulesEngine();

    private AccountRulesPropertiesLoader savingsLoader;
    private AccountRulesPropertiesLoader currentLoader;
    private AccountRulesPropertiesLoader fdLoader;
    private AccountRulesPropertiesLoader salaryLoader;

    public AccountRulesEngine() {
    }

    public static AccountRulesEngine getInstance() {
        return INSTANCE;
    }

    public synchronized AccountRulesPropertiesLoader getSavingsLoader() {
        if (savingsLoader == null) {
            savingsLoader = new AccountRulesPropertiesLoader(SAVINGS_CONFIG_PATH);
        }
        return savingsLoader;
    }

    public synchronized AccountRulesPropertiesLoader getCurrentLoader() {
        if (currentLoader == null) {
            currentLoader = new AccountRulesPropertiesLoader(CURRENT_CONFIG_PATH);
        }
        return currentLoader;
    }

    public synchronized AccountRulesPropertiesLoader getFdLoader() {
        if (fdLoader == null) {
            fdLoader = new AccountRulesPropertiesLoader(FD_CONFIG_PATH);
        }
        return fdLoader;
    }

    public synchronized AccountRulesPropertiesLoader getSalaryLoader() {
        if (salaryLoader == null) {
            salaryLoader = new AccountRulesPropertiesLoader(SALARY_CONFIG_PATH);
        }
        return salaryLoader;
    }

    public void loadAllRules() {
        System.out.println("📂 Loading account rules from properties files...");
        System.out.println("--------------------------------------------------");
        
        getSavingsLoader();
        System.out.println("✅ Loaded rules for: SAVINGS (" + savingsLoader.getTenureBucketCount() + " tenure buckets)");
        
        getCurrentLoader();
        System.out.println("✅ Loaded rules for: CURRENT (" + currentLoader.getTenureBucketCount() + " tenure buckets)");
        
        getFdLoader();
        System.out.println("✅ Loaded rules for: FIXEDDEPOSIT (" + fdLoader.getTenureBucketCount() + " tenure buckets)");
        
        getSalaryLoader();
        System.out.println("✅ Loaded rules for: SALARY (" + salaryLoader.getTenureBucketCount() + " tenure buckets)");
        
        System.out.println("--------------------------------------------------");
        System.out.println("✅ All rules loaded successfully!");
        System.out.println("   Loaded at: " + LocalDateTime.now());
        System.out.println("   Account types: [SALARY, SAVINGS, FIXEDDEPOSIT, CURRENT]");
    }

    private String normalizeAccountType(String accountType) {
        if (accountType == null) return "";
        String upper = accountType.toUpperCase().replace("_", "").replace(" ", "");
        if (upper.contains("SAVING")) return "SAVINGS";
        if (upper.contains("CURRENT")) return "CURRENT";
        if (upper.contains("FIXED") || upper.contains("FD")) return "FIXEDDEPOSIT";
        if (upper.contains("SALARY")) return "SALARY";
        return upper;
    }

    private AccountRulesPropertiesLoader getLoaderForType(String normalizedType) {
        return switch (normalizedType) {
            case "SAVINGS" -> getSavingsLoader();
            case "CURRENT" -> getCurrentLoader();
            case "FIXEDDEPOSIT" -> getFdLoader();
            case "SALARY" -> getSalaryLoader();
            default -> null;
        };
    }

    public static TenureTier getTenureTier(int tenureYears) {
        if (tenureYears >= 5) {
            return TenureTier.PRIVILEGE;
        } else if (tenureYears >= 3) {
            return TenureTier.PREMIUM;
        } else if (tenureYears >= 1) {
            return TenureTier.STANDARD;
        } else {
            return TenureTier.NEW;
        }
    }

    public Object getAdditionalFeature(String accountType, int tenureYears, String featureName) {
        if (accountType == null) {
            return null;
        }
        String normalizedType = normalizeAccountType(accountType);
        AccountRulesPropertiesLoader loader = getLoaderForType(normalizedType);
        if (loader == null) {
            return null;
        }
        TenureTier tier = getTenureTier(tenureYears);
        String tierKey = tier.name().toLowerCase();
        
        String propKey;
        if ("dailyTransferLimit".equalsIgnoreCase(featureName)) {
            propKey = "daily.transfer.limit." + tierKey;
        } else if ("overdraftLimit".equalsIgnoreCase(featureName)) {
            propKey = "overdraft.limit." + tierKey;
        } else {
            propKey = featureName + "." + tierKey;
        }
        
        String val = loader.getProperty(propKey);
        if (val == null) {
            val = loader.getProperty(featureName);
        }
        if (val != null) {
            try {
                return Double.valueOf(val.trim());
            } catch (NumberFormatException e) {
                return val;
            }
        }
        return null;
    }

    public double getDailyTransferLimit(String accountType, int tenureYears) {
        Object feature = getAdditionalFeature(accountType, tenureYears, "dailyTransferLimit");
        if (feature == null) {
            return 0.0;
        }
        if (feature instanceof Double) {
            return (Double) feature;
        }
        if (feature instanceof Number) {
            return ((Number) feature).doubleValue();
        }
        try {
            return Double.parseDouble(feature.toString());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public double getDailyTransferLimit(String accountType) {
        return getDailyTransferLimit(accountType, 0);
    }

    public static double getSavingsMinBalance(TenureTier tier) {
        String key = "min.balance." + tier.name().toLowerCase();
        return getInstance().getSavingsLoader().getDouble(key, 10000.0);
    }

    public static double getSavingsMinBalance(int tenureYears) {
        return getSavingsMinBalance(getTenureTier(tenureYears));
    }

    public static double getSavingsMinBalance(String tierName) {
        try {
            return getSavingsMinBalance(TenureTier.valueOf(tierName.toUpperCase()));
        } catch (Exception e) {
            return 10000.0;
        }
    }

    public static double getSavingsInterestRate(TenureTier tier) {
        String key = "interest.rate." + tier.name().toLowerCase();
        return getInstance().getSavingsLoader().getDouble(key, 2.70);
    }

    public static double getSavingsInterestRate(int tenureYears) {
        return getSavingsInterestRate(getTenureTier(tenureYears));
    }

    public static double getSavingsInterestRate(String tierName) {
        try {
            return getSavingsInterestRate(TenureTier.valueOf(tierName.toUpperCase()));
        } catch (Exception e) {
            return 2.70;
        }
    }

    public static double getCurrentOverdraftLimit(double monthlyTurnover) {
        double defaultLimit = getInstance().getCurrentLoader().getDouble("overdraft.limit.default", 25000.0);
        double multiplier = getInstance().getCurrentLoader().getDouble("overdraft.turnover.multiplier", 2.5);
        return Math.max(defaultLimit, monthlyTurnover * multiplier);
    }

    public static double getFDInterestRate(int months) {
        if (months >= 12) {
            return getInstance().getFdLoader().getDouble("interest.rate.months.12", 6.5);
        } else if (months >= 6) {
            return getInstance().getFdLoader().getDouble("interest.rate.months.6", 5.5);
        } else if (months >= 3) {
            return getInstance().getFdLoader().getDouble("interest.rate.months.3", 4.5);
        } else {
            return getInstance().getFdLoader().getDouble("interest.rate.months.0", 4.0);
        }
    }

    public static Map<TenureTier, Double> getSavingsMinBalanceMap() {
        Map<TenureTier, Double> map = new HashMap<>();
        for (TenureTier tier : TenureTier.values()) {
            map.put(tier, getSavingsMinBalance(tier));
        }
        return Collections.unmodifiableMap(map);
    }

    public static Map<TenureTier, Double> getSavingsInterestRateMap() {
        Map<TenureTier, Double> map = new HashMap<>();
        for (TenureTier tier : TenureTier.values()) {
            map.put(tier, getSavingsInterestRate(tier));
        }
        return Collections.unmodifiableMap(map);
    }

    public static Map<Integer, Double> getFDInterestRateMap() {
        Map<Integer, Double> map = new TreeMap<>();
        map.put(0, getFDInterestRate(0));
        map.put(3, getFDInterestRate(3));
        map.put(6, getFDInterestRate(6));
        map.put(12, getFDInterestRate(12));
        return Collections.unmodifiableMap(map);
    }
}
