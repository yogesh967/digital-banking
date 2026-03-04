package com.bank.digital_banking.controller;

import com.bank.digital_banking.dto.OpenAccountRequest;
import com.bank.digital_banking.entity.Account;
import com.bank.digital_banking.entity.User;
import com.bank.digital_banking.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;


    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/open")
    public Account openAccount(@Valid @RequestBody OpenAccountRequest openAccountRequest) {
        return accountService.openAccount(openAccountRequest.getUserId(), openAccountRequest.getAccountType());
    }
}
