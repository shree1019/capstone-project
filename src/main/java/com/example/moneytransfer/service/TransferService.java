package com.example.moneytransfer.service;

import com.example.moneytransfer.dto.TransferRequest;
import com.example.moneytransfer.dto.TransferResponse;
import com.example.moneytransfer.entity.Account;
import com.example.moneytransfer.entity.TransactionLog;
import com.example.moneytransfer.enums.TransactionStatus;
import com.example.moneytransfer.exception.AccountNotActiveException;
import com.example.moneytransfer.exception.AccountNotFoundException;
import com.example.moneytransfer.exception.DuplicateTransferException;
import com.example.moneytransfer.exception.InvalidTransferRequestException;
import com.example.moneytransfer.repository.AccountRepository;
import com.example.moneytransfer.repository.TransactionLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransactionLogRepository transactionLogRepository;

    /**
     * Main entry point for executing a transfer with idempotency and validation.
     */
    @Transactional
    public TransferResponse transfer(TransferRequest request) {
        log.info("Initiating transfer: from={} to={} amount={} key={}",
                request.getFromAccountId(), request.getToAccountId(), request.getAmount(), request.getIdempotencyKey());

        validateTransfer(request);

        Optional<TransactionLog> existing = transactionLogRepository.findByIdempotencyKey(request.getIdempotencyKey());
        if (existing.isPresent()) {
            TransactionLog existingLog = existing.get();
            if (TransactionStatus.SUCCESS.equals(existingLog.getStatus())) {
                log.info("Idempotent transfer request detected. Returning existing successful transaction for key={}",
                        request.getIdempotencyKey());
                return toTransferResponse(existingLog, "Transfer already processed");
            }
            throw new DuplicateTransferException(request.getIdempotencyKey());
        }

        TransactionLog logEntry = executeTransfer(request);
        return toTransferResponse(logEntry, "Transfer successful");
    }

    /**
     * Validates high-level invariants of the transfer request.
     */
    public void validateTransfer(TransferRequest request) {
        if (request.getFromAccountId().equals(request.getToAccountId())) {
            throw new InvalidTransferRequestException("From and to account must be different");
        }
    }

    /**
     * Performs the actual debit and credit operations and creates the transaction log.
     */
    protected TransactionLog executeTransfer(TransferRequest request) {
        Account fromAccount = accountRepository.findById(request.getFromAccountId())
                .orElseThrow(() -> new AccountNotFoundException(request.getFromAccountId()));
        Account toAccount = accountRepository.findById(request.getToAccountId())
                .orElseThrow(() -> new AccountNotFoundException(request.getToAccountId()));

        if (!fromAccount.isActive()) {
            throw new AccountNotActiveException(fromAccount.getId());
        }
        if (!toAccount.isActive()) {
            throw new AccountNotActiveException(toAccount.getId());
        }

        fromAccount.debit(request.getAmount());
        toAccount.credit(request.getAmount());

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        TransactionLog transactionLog = TransactionLog.builder()
                .fromAccountId(fromAccount.getId())
                .toAccountId(toAccount.getId())
                .amount(request.getAmount())
                .status(TransactionStatus.SUCCESS)
                .failureReason(null)
                .idempotencyKey(request.getIdempotencyKey())
                .createdOn(OffsetDateTime.now())
                .build();

        TransactionLog persisted = transactionLogRepository.save(transactionLog);
        log.info("Transfer completed successfully. Transaction id={}", persisted.getId());
        return persisted;
    }

    private TransferResponse toTransferResponse(TransactionLog transactionLog, String message) {
        return TransferResponse.builder()
                .transactionId(transactionLog.getId())
                .fromAccountId(transactionLog.getFromAccountId())
                .toAccountId(transactionLog.getToAccountId())
                .amount(transactionLog.getAmount())
                .status(transactionLog.getStatus())
                .message(message)
                .createdOn(transactionLog.getCreatedOn())
                .build();
    }
}

