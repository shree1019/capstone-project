package com.example.moneytransfer.service;

import com.example.moneytransfer.dto.TransferRequest;
import com.example.moneytransfer.dto.TransferResponse;
import com.example.moneytransfer.entity.Account;
import com.example.moneytransfer.entity.TransactionLog;
import com.example.moneytransfer.enums.AccountStatus;
import com.example.moneytransfer.enums.TransactionStatus;
import com.example.moneytransfer.exception.InvalidTransferRequestException;
import com.example.moneytransfer.repository.AccountRepository;
import com.example.moneytransfer.repository.TransactionLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionLogRepository transactionLogRepository;

    @InjectMocks
    private TransferService transferService;

    private Account fromAccount;
    private Account toAccount;

    @BeforeEach
    void setUp() {
        fromAccount = Account.builder()
                .id(1L)
                .holderName("Alice")
                .balance(new BigDecimal("100.00"))
                .status(AccountStatus.ACTIVE)
                .lastUpdated(OffsetDateTime.now())
                .build();

        toAccount = Account.builder()
                .id(2L)
                .holderName("Bob")
                .balance(new BigDecimal("50.00"))
                .status(AccountStatus.ACTIVE)
                .lastUpdated(OffsetDateTime.now())
                .build();
    }

    @Test
    void validateTransferShouldRejectSameAccount() {
        TransferRequest request = TransferRequest.builder()
                .fromAccountId(1L)
                .toAccountId(1L)
                .amount(new BigDecimal("10.00"))
                .idempotencyKey("key-1")
                .build();

        assertThrows(InvalidTransferRequestException.class,
                () -> transferService.validateTransfer(request));
    }

    @Test
    void transferShouldDebitAndCreditAccountsAndCreateLog() {
        TransferRequest request = TransferRequest.builder()
                .fromAccountId(1L)
                .toAccountId(2L)
                .amount(new BigDecimal("30.00"))
                .idempotencyKey("unique-key-123")
                .build();

        when(transactionLogRepository.findByIdempotencyKey("unique-key-123"))
                .thenReturn(Optional.empty());
        when(accountRepository.findById(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(toAccount));

        ArgumentCaptor<TransactionLog> logCaptor = ArgumentCaptor.forClass(TransactionLog.class);
        when(transactionLogRepository.save(any(TransactionLog.class))).thenAnswer(invocation -> {
            TransactionLog log = invocation.getArgument(0);
            log.setId(UUID.randomUUID());
            return log;
        });

        TransferResponse response = transferService.transfer(request);

        assertNotNull(response);
        assertEquals(TransactionStatus.SUCCESS, response.getStatus());
        assertEquals(new BigDecimal("30.00"), response.getAmount());

        // fromAccount should be debited
        assertEquals(new BigDecimal("70.00"), fromAccount.getBalance());
        // toAccount should be credited
        assertEquals(new BigDecimal("80.00"), toAccount.getBalance());

        verify(accountRepository).save(fromAccount);
        verify(accountRepository).save(toAccount);
        verify(transactionLogRepository).save(logCaptor.capture());

        TransactionLog savedLog = logCaptor.getValue();
        assertEquals(1L, savedLog.getFromAccountId());
        assertEquals(2L, savedLog.getToAccountId());
        assertEquals(new BigDecimal("30.00"), savedLog.getAmount());
        assertEquals(TransactionStatus.SUCCESS, savedLog.getStatus());
        assertEquals("unique-key-123", savedLog.getIdempotencyKey());
    }
}

