package com.progressoft.induction.atm;

import com.progressoft.induction.atm.Impl.ATMImpl;
import com.progressoft.induction.atm.exceptions.AccountNotFoundException;
import com.progressoft.induction.atm.exceptions.InsufficientFundsException;
import com.progressoft.induction.atm.exceptions.NotEnoughMoneyInATMException;
import java.math.BigDecimal;
import java.util.*;

public class Main {

  public static void main(String args[]) {
    ATMImpl atm = new ATMImpl();

    BigDecimal amount = new BigDecimal(10);

    try {
      List<Banknote> myList = atm.withdraw("444444444", amount);

      BigDecimal sumOfAllBanknotes = myList
        .stream()
        .map(Banknote::getValue)
        .reduce(BigDecimal::add)
        .orElse(BigDecimal.ZERO);

      System.out.println("Recieved Notes: " + myList);
      if (sumOfAllBanknotes.compareTo(amount) == 0) {
        System.out.println("Recieved Required Amount!");
      }
    } catch (
      AccountNotFoundException
      | InsufficientFundsException
      | NotEnoughMoneyInATMException e
    ) {
      System.out.println(e.getMessage());
    }
  }
}
