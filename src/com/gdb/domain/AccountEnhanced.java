package com.gdb.domain;
import java.util.Objects;

public class AccountEnhanced {
  private int accountNumber;
  private String name;
  private int age;
  private double balance;
  private String accountType;
  private String status;
  private Integer pin;
  private final double MIN_SAVINGS_BALANCE = 500.0;
  private final double MIN_CURRENT_BALANCE = 1000.0;
  private final int MIN_AGE = 18;
  private final int MIN_PIN = 1000;
  private final int MAX_PIN = 9999;

  public AccountEnhanced(int accountNumber, String name, int age, Integer pin, double initialBalance,
      String accountType) {
    if (!"Savings".equals(accountType) && !"Current".equals(accountType)) {
      this.accountType = "Savings";
    } else {
      this.accountType = accountType;
    }
    this.accountNumber = accountNumber;
    this.name = name;
    if (pin == null || pin < MIN_PIN || pin > MAX_PIN) {
      this.pin = null;
    } else {
      this.pin = pin;
    }
    if (age < MIN_AGE) {
      this.age = MIN_AGE;
    } else {
      this.age = age;
    }
    if (this.accountType.equals("Savings") && initialBalance < MIN_SAVINGS_BALANCE) {
      this.balance = MIN_SAVINGS_BALANCE;
    } else if (this.accountType.equals("Current") && initialBalance < MIN_CURRENT_BALANCE) {
      this.balance = MIN_CURRENT_BALANCE;
    } else {
      this.balance = initialBalance;
    }
    this.status = "Active";
  }

  public boolean deposit(double amount) {
    if (amount > 0 && this.status.equals("Active")) {
      this.balance += amount;
      return true;
    }
    return false;
  }

  public boolean withdraw(double amount, Integer pin) {
    if (amount > 0 && amount <= this.balance && this.status.equals("Active") && Objects.equals(this.pin, pin)) {
      if (this.accountType.equals("Savings") && (this.balance - amount) < MIN_SAVINGS_BALANCE) {
        return false;
      } else if (this.accountType.equals("Current") && (this.balance - amount) < MIN_CURRENT_BALANCE) {
        return false;
      } else {
        this.balance -= amount;
        return true;
      }
    }
    return false;
  }

  public int getAccountNumber() {
    return this.accountNumber;
  }

  public String getName() {
    return this.name;
  }

  public int getAge() {
    return this.age;
  }

  public double getBalance() {
    return this.balance;
  }

  public String getAccountType() {
    return this.accountType;
  }

  public String getStatus() {
    return this.status;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public boolean closeAccount() {
    if (this.status.equals("Active")) {
      this.status = "Inactive";
      return true;
    }
    return false;
  }

  public boolean reopenAccount() {
    if (this.status.equals("Inactive")) {
      this.status = "Active";
      return true;
    }
    return false;
  }

  public boolean setPin(Integer pin1) {
    if (pin1 == null || pin1 < MIN_PIN || pin1 > MAX_PIN) {
      return false;
    }
    this.pin = pin1;
    return true;
  }

  public boolean verifyPin(int pin1) {
    if (this.pin != null && this.pin.intValue() == pin1) {
      return true;
    }
    return false;
  }

  public boolean hasPin() {
    if (this.pin == null) {
      return false;
    }
    return true;
  }
}
