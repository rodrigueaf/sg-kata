package com.claude.sgkata.util;

import com.claude.sgkata.domain.Transaction;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class DataSourceUtil {

    public static List<String> accounts = Arrays.asList("1234567890", "0987654321");

    public static List<Transaction> transactions = DataSourceUtil.getDefalutTransactions();

    private DataSourceUtil() {
    }

    public static void initTransactions() {
        transactions = DataSourceUtil.getDefalutTransactions();
    }

    public static List<Transaction> getDefalutTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(UUID.randomUUID().toString(), accounts.get(0), 30, Instant.now()));
        transactions.add(new Transaction(UUID.randomUUID().toString(), accounts.get(0), 70, Instant.now()));
        transactions.add(new Transaction(UUID.randomUUID().toString(), accounts.get(1), 150, Instant.now()));
        return transactions;
    }
}
