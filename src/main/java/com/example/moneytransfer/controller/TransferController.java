package com.example.moneytransfer.controller;

import com.example.moneytransfer.dto.TransferRequest;
import com.example.moneytransfer.dto.TransferResponse;
import com.example.moneytransfer.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transfers")
@RequiredArgsConstructor
@Slf4j
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<TransferResponse> createTransfer(@Valid @RequestBody TransferRequest request) {
        log.debug("Received transfer request: from={} to={} amount={}",
                request.getFromAccountId(), request.getToAccountId(), request.getAmount());
        TransferResponse response = transferService.transfer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

