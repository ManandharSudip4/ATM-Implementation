package com.progressoft.induction.atm.Impl;

import com.progressoft.induction.atm.ATM;
import com.progressoft.induction.atm.Banknote;
import com.progressoft.induction.atm.exceptions.AccountNotFoundException;
import com.progressoft.induction.atm.exceptions.InsufficientFundsException;
import com.progressoft.induction.atm.exceptions.NotEnoughMoneyInATMException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ATMImpl implements ATM {

  List<Banknote> withdrawalNotes = new ArrayList<>();

  private final BankingSystemImpl bankingSystem = new BankingSystemImpl();

  @Override
  public List<Banknote> withdraw(String accountNumber, BigDecimal amount) {
    // Todo 1: Check if a user exists? : done
    // Todo 2: Check if user has enough money in his/her account? done
    // 2 can be done interface Banking System and its getAccountBalance

    BigDecimal remainingBalance = bankingSystem.getAccountBalance(
      accountNumber
    );

    // try {
    // }
    BigDecimal totalAmount = bankingSystem.sumOfMoneyInAtm();

    if (amount.compareTo(totalAmount) > 0) {
      throw new NotEnoughMoneyInATMException("Not Enough Money in ATM");
    }

    if (remainingBalance == null) {
      throw new AccountNotFoundException("Account Number Does not exists.");
    }

    System.out.println("Accounts have enough money");
    if (amount.compareTo(remainingBalance) <= 0) {
      System.out.println(
        "Account Number: " +
        accountNumber +
        "\nAmount to be withdrawn: " +
        amount +
        "\nAmount in Bank: " +
        remainingBalance
      );

      bankingSystem.debitAccount(accountNumber, amount);

      System.out.println("Cash Map Before: " + bankingSystem.atmCashMap);

      // withdraws logic
      List<Banknote> reverseKeys = new ArrayList<>(
        bankingSystem.atmCashMap.keySet()
      );
      Collections.reverse(reverseKeys);
      for (Banknote entry : reverseKeys) {
        if (amount.compareTo(entry.getValue()) >= 0) {
          int numNotes = amount.divide(entry.getValue()).intValue();
          for (int i = 0; i < numNotes; i++) {
            withdrawalNotes.add(entry);
            bankingSystem.atmCashMap.put(
              entry,
              bankingSystem.atmCashMap.get(entry) - 1
            );
          }
          amount = amount.remainder(entry.getValue());
        }
      }

      


      System.out.println(bankingSystem.atmCashMap.get(Banknote.FIVE_JOD));

      System.out.println("---------------- After Withdrawl -------------");
      System.out.println("Cash Out: " + withdrawalNotes);
      System.out.println(
        "New Balance: " + bankingSystem.getAccountBalance(accountNumber)
      );
      System.out.println(
        "Cash Map After Withdrawl: " + bankingSystem.atmCashMap
      );
    } else {
      throw new InsufficientFundsException(
        "You don't have sufficient balance! Thank You"
      );
    }

    // Needs to return the amount if all criteria is fulfilled.
    return withdrawalNotes;
  }

  @Override
  public BigDecimal checkBalance(String accountNumber) {
    return bankingSystem.getAccountBalance(accountNumber);
  }
}
