# Global Digital Bank (GDB) Engine

An enterprise-grade, modular banking system designed and implemented in Java. GDB models real-world core banking features with strict object-oriented design principles, robust custom exception hierarchies, configurable rule engines, and software design patterns.

---

## 🏛️ Architecture Overview

The system is architected across discrete, decoupled layers:

```
src/com/gdb/
├── Main.java       # Application bootstrapper launching interactive AccountUI
├── command/        # Command Pattern implementation for banking operations
│   ├── DepositCommand.java
│   ├── WithdrawCommand.java
│   ├── TransferCommand.java
│   └── TransactionCommand.java
├── database/       # In-memory simulated persistence engine
│   └── SimulatedDatabase.java
├── domain/         # Core banking entities, account hierarchies & rules engine
│   ├── Account.java
│   ├── AbstractAccount.java
│   ├── AccountFactory.java
│   ├── AccountRulesEngine.java
│   ├── AccountRulesPropertiesLoader.java
│   ├── CurrentAccount.java
│   ├── FixedDepositAccount.java
│   ├── IAccount.java
│   ├── SalaryAccount.java
│   ├── SavingsAccount.java
│   ├── Transaction.java
│   └── TransactionType.java
├── exceptions/     # Domain-specific checked banking exception hierarchy
│   ├── AccountException.java
│   ├── InactiveAccountException.java
│   ├── InsufficientBalanceException.java
│   ├── InvalidAmountException.java
│   ├── InvalidPinException.java
│   └── MinimumBalanceViolationException.java
├── logging/        # Bridge Pattern logging backends & file persistence
│   ├── DatabaseLogDestination.java
│   ├── FileLogDestination.java
│   ├── LogDestination.java
│   ├── MemoryLogDestination.java
│   ├── TransactionLog.java
│   └── TransactionLogger.java
├── service/        # Business workflows and service orchestration
│   ├── AccountService.java
│   └── TransferService.java
├── ui/             # Menu-driven interactive console presentation layer
│   └── AccountUI.java
└── tests/          # Activity test suites and validation drivers
    ├── TestAbstractAccount.java
    ├── TestAccountEnhanced.java
    ├── TestAccountExceptions.java
    ├── TestAccountRulesEngine.java
    ├── TestAccountRulesEngineProperties.java
    ├── TestAccountService.java
    ├── TestAccountUI.java
    ├── TestBridgeLogging.java
    ├── TestCommandLogging.java
    ├── TestDynamicAccountRules.java
    ├── TestInterfaceFactory.java
    ├── testSubclassAccounts.java
    ├── TestTransactionModel.java
    └── TestTransfer.java
```

---

## 🚀 Design Patterns & Architectural Highlights

1. **Presentation & UI Layer (`AccountUI`)**
   - Menu-driven console interface supporting account opening, deposits, withdrawals, fund transfers, balance lookups, and audit history.
   - Stream-decoupled architecture for seamless unit and integration testing.

2. **Service Orchestration Layer (`AccountService`)**
   - Single point of coordination for banking operations.
   - Manages account lifecycles, executes commands, and logs transactions through the Bridge logger.

3. **Bridge Pattern (Logging)**
   - **Abstraction**: `TransactionLogger`
   - **Implementor**: `LogDestination`
   - **Concrete Implementors**: `FileLogDestination`, `DatabaseLogDestination`, `MemoryLogDestination`
   - **Benefit**: Pluggable storage backends switchable at runtime without altering client code.

4. **Command Pattern (Transactions)**
   - **Command Interface**: `TransactionCommand` (executable & serializable)
   - **Concrete Commands**: `DepositCommand`, `WithdrawCommand`, `TransferCommand`
   - **Benefit**: Decouples transaction invocation from execution, enabling audit logging and command replay.

5. **Factory Method Pattern (Account Creation)**
   - **Factory**: `AccountFactory`
   - **Interface**: `IAccount`
   - **Benefit**: Encapsulates account instantiation based on type and tenure.

6. **Singleton Pattern (Rules Engine)**
   - **Singleton**: `AccountRulesEngine`
   - **Benefit**: Centralized, thread-safe access to dynamic banking rules loaded from `.properties` files.

---

## 🛠️ Tech Stack & Prerequisites

- **Language**: Java 17+
- **Configuration**: Java Properties (`.properties`)
- **Persistence**: Binary Object Serialization (`.ser`) & In-Memory Store
- **Build / Run Tool**: `javac`, `java`, PowerShell / Bash

---

## ⚙️ Compilation & Execution

### Windows (PowerShell)
```powershell
New-Item bin -ItemType Directory -Force | Out-Null
Copy-Item src\main\resources\config bin -Recurse -Force
javac -encoding UTF-8 -d bin (Get-ChildItem -Recurse -Filter *.java src).FullName
java "-Dfile.encoding=UTF-8" -cp bin com.gdb.tests.TestAccountUI
```

### Run Interactive Console App
```powershell
java "-Dfile.encoding=UTF-8" -cp bin com.gdb.Main
```

### Linux & macOS (Bash)
```bash
mkdir -p bin && cp -r src/main/resources/config bin/
javac -encoding UTF-8 -d bin $(find src -name "*.java")
java -cp bin com.gdb.tests.TestAccountUI

# Interactive app:
java -cp bin com.gdb.Main
```
