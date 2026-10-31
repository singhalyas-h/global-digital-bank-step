# Activity 20: Menu-Driven Console User Interface (AccountUI)

## 📌 Overview
Activity 20 introduces `AccountUI`, an interactive, menu-driven console user interface operating on top of `AccountService`. This layer bridges human interaction with the underlying domain entities, business rules, and logging infrastructure.

---

## 🏗️ Architecture Diagram

```
┌─────────────────────────────────────────┐
│              AccountUI                  │  ← Presentation Layer (Console Menu)
└────────────────────┬────────────────────┘
                     │ delegates
                     ▼
┌─────────────────────────────────────────┐
│            AccountService               │  ← Orchestration / Service Layer
└────────┬───────────┬───────────┬────────┘
         │           │           │
         ▼           ▼           ▼
   AccountFactory  Transfer   Transaction
                   Service      Logger
```

---

## 📋 Features

1. **Interactive Menu Navigation**
   - Option 1: Open New Account (Savings, Current, Salary, Fixed Deposit)
   - Option 2: Deposit Funds
   - Option 3: Withdraw Funds (PIN-protected)
   - Option 4: Transfer Funds (PIN-protected, tenure daily limit enforced)
   - Option 5: View Account Details
   - Option 6: List All Registered Accounts
   - Option 7: View Transaction Audit History
   - Option 8: Close Account
   - Option 0: Exit Application

2. **Decoupled Stream I/O**
   - Supports dependency injection of `InputStream` and `PrintStream` for automated testing and simulation.

3. **Exception Safety**
   - Intercepts and displays domain-specific errors (`AccountException`, `InvalidPinException`, `InsufficientBalanceException`, `InactiveAccountException`, `InvalidAmountException`, etc.) without application termination.

---

## ⚙️ Compilation & Execution

### Windows (PowerShell)
```powershell
New-Item bin -ItemType Directory -Force | Out-Null
Copy-Item src\main\resources\config bin -Recurse -Force
javac -encoding UTF-8 -d bin (Get-ChildItem -Recurse -Filter *.java src).FullName
java -cp bin com.gdb.tests.TestAccountUI
```

### Run Interactive Console App
```powershell
java -cp bin com.gdb.Main
```

### Linux & macOS (Bash)
```bash
mkdir -p bin && cp -r src/main/resources/config bin/
javac -encoding UTF-8 -d bin $(find src -name "*.java")
java -cp bin com.gdb.tests.TestAccountUI
```
