package com.bank.digital_banking.service;

import com.bank.digital_banking.entity.Account;
import com.bank.digital_banking.entity.AccountBalance;
import com.bank.digital_banking.entity.AuditLog;
import com.bank.digital_banking.entity.User;
import com.bank.digital_banking.repository.AccountBalanceRepository;
import com.bank.digital_banking.repository.AccountRepository;
import com.bank.digital_banking.repository.AuditLogRepository;
import com.bank.digital_banking.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountBalanceRepository balanceRepository;
    private final AuditLogRepository auditLogRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository, AccountBalanceRepository balanceRepository, AuditLogRepository auditLogRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.balanceRepository = balanceRepository;
        this.auditLogRepository = auditLogRepository;
    }

    public Account openAccount(Long userId, String accountType) {
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Account account = new Account();
        account.setAccountType(accountType);
        account.setAccountNumber(generateAccountNumber());
        account.setUser(user);
        Account saved = accountRepository.save(account);

        AccountBalance accountBalance = new AccountBalance();
        accountBalance.setAccount(saved);
        accountBalance.setBalance(BigDecimal.ZERO);
        balanceRepository.save(accountBalance);
        return saved;
    }

    private String generateAccountNumber() {
        return UUID.randomUUID().toString().substring(0, 10);
    }

    private void saveAudit(String status, String message) {
        String actor = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        System.out.println("Actor"+ actor);
        AuditLog auditLog = new AuditLog();
        auditLog.setAction("OPEN_ACCOUNT");
        auditLog.setStatus(status);
        auditLog.setActor(actor);
        auditLog.setMessage(message);

        auditLogRepository.save(auditLog);
    }
}
