package com.example.moneytransfer.service;

import com.example.moneytransfer.dto.AccountResponse;
import com.example.moneytransfer.dto.TransactionLogResponse;
import com.example.moneytransfer.entity.Account;
import com.example.moneytransfer.entity.TransactionLog;
import com.example.moneytransfer.exception.AccountNotFoundException;
import com.example.moneytransfer.repository.AccountRepository;
import com.example.moneytransfer.repository.TransactionLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionLogRepository transactionLogRepository;

    @Transactional
    public AccountResponse getAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
        return toAccountResponse(account);
    }

    @Transactional
    public BigDecimal getBalance(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
        return account.getBalance();
    }

    @Transactional
    public List<TransactionLogResponse> getTransactions(Long accountId) {
        // Return transactions where the account is either source or destination
        List<TransactionLog> logs = transactionLogRepository
                .findByFromAccountIdOrToAccountIdOrderByCreatedOnDesc(accountId, accountId);

        return logs.stream()
                .map(this::toTransactionLogResponse)
                .collect(Collectors.toList());
    }

    private AccountResponse toAccountResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .holderName(account.getHolderName())
                .balance(account.getBalance())
                .status(account.getStatus())
                .lastUpdated(account.getLastUpdated())
                .build();
    }

    private TransactionLogResponse toTransactionLogResponse(TransactionLog log) {
        return TransactionLogResponse.builder()
                .id(log.getId())
                .fromAccountId(log.getFromAccountId())
                .toAccountId(log.getToAccountId())
                .amount(log.getAmount())
                .status(log.getStatus())
                .failureReason(log.getFailureReason())
                .createdOn(log.getCreatedOn())
                .build();
    }
}

