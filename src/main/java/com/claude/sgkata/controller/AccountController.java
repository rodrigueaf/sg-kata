package com.claude.sgkata.controller;

import com.claude.sgkata.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("accounts/{number}/transactions")
    public ResponseEntity addATransaction(@PathVariable String number, @RequestBody double amount) {
        if (amount == 0) throw new IllegalArgumentException("Amount must not be equal to 0");
        return ResponseEntity.ok(this.accountService.addAtransaction(number, amount));
    }

    @GetMapping("accounts/{number}/transactions")
    public ResponseEntity getTransactions(@PathVariable String number,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(this.accountService.getTransactions(number, page, size));
    }
}
