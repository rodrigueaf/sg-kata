package com.claude.sgkata.service;

import com.claude.sgkata.domain.Transaction;
import com.claude.sgkata.exception.InsufficientBalanceException;
import com.claude.sgkata.util.DataSourceUtil;
import com.claude.sgkata.util.ListUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountService {

    public double getBalance(String accountNumber) {
        return DataSourceUtil.transactions.stream()
                .filter(t -> t.getAccountNumber().equals(accountNumber))
                .mapToDouble(Transaction::getAmount).sum();
    }

    public double addAtransaction(String number, double amount) {
        if (amount < 0)
            this.withdraw(number, amount);
        else
            this.deposit(number, amount);
        return getBalance(number);
    }

    private void deposit(String number, double amount) {
        DataSourceUtil.transactions.add(new Transaction(
                UUID.randomUUID().toString(), number, amount, Instant.now()));
    }

    private void withdraw(String number, double amount) {
        if (this.getBalance(number) < Math.abs(amount)) throw new InsufficientBalanceException();
        DataSourceUtil.transactions.add(new Transaction(
                UUID.randomUUID().toString(), number, amount, Instant.now()));
    }

    public List<Transaction> getTransactions(String accountNumber, int page, int size) {
        return ListUtils.partition(DataSourceUtil.transactions.stream()
                .filter(t -> t.getAccountNumber().equals(accountNumber))
                .collect(Collectors.toList()), size).get(page);
    }
}
