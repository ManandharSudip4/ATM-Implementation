package com.progressoft.induction.atm.Impl;

import com.progressoft.induction.atm.ATM;
import com.progressoft.induction.atm.Banknote;
import com.progressoft.induction.atm.exceptions.AccountNotFoundException;
import com.progressoft.induction.atm.exceptions.InsufficientFundsException;
import com.progressoft.induction.atm.exceptions.NotEnoughMoneyInATMException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ATMImpl implements ATM {

  List<Banknote> withdrawalNotes = new ArrayList<>();
  private final BankingSystemImpl bankingSystem = new BankingSystemImpl();

  @Override
  public List<Banknote> withdraw(String accountNumber, BigDecimal amount) {
    BigDecimal totalAmount = bankingSystem.sumOfMoneyInAtm();
    BigDecimal remainingBalance = checkBalance(accountNumber);

    // Checking if withdrawl amount is greater that Money in ATM.
    if (amount.compareTo(totalAmount) > 0) {
      throw new NotEnoughMoneyInATMException("Not Enough Money in ATM");
    }

    // Checking if Account Exists or not.
    if (remainingBalance == null) {
      throw new AccountNotFoundException("Account Number Does not exists.");
    }

    if (amount.compareTo(remainingBalance) <= 0) {
      bankingSystem.debitAccount(accountNumber, amount);

      List<Banknote> reverseKeys = new ArrayList<>(
        bankingSystem.atmCashMap.keySet()
      );
      Collections.reverse(reverseKeys);
      // withdrawing logic
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
    } else {
      throw new InsufficientFundsException(
        "You don't have sufficient balance! Thank You"
      );
    }

    return withdrawalNotes;
  }

  @Override
  public BigDecimal checkBalance(String accountNumber) {
    return bankingSystem.getAccountBalance(accountNumber);
  }
}
