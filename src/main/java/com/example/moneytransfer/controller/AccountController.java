package com.example.moneytransfer.controller;

import com.example.moneytransfer.dto.AccountResponse;
import com.example.moneytransfer.dto.TransactionLogResponse;
import com.example.moneytransfer.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable("id") Long id) {
        log.debug("Fetching account details for id={}", id);
        return ResponseEntity.ok(accountService.getAccount(id));
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable("id") Long id) {
        log.debug("Fetching balance for account id={}", id);
        return ResponseEntity.ok(accountService.getBalance(id));
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<TransactionLogResponse>> getTransactions(@PathVariable("id") Long id) {
        log.debug("Fetching transactions for account id={}", id);
        return ResponseEntity.ok(accountService.getTransactions(id));
    }
}

