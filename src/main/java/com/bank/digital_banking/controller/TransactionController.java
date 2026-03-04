package com.bank.digital_banking.controller;

import com.bank.digital_banking.dto.TransferRequest;
import com.bank.digital_banking.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    public String transfer(@Valid @RequestBody TransferRequest request) {
        transactionService.transfer(request);
        return "Transfer successful";
    }
}
