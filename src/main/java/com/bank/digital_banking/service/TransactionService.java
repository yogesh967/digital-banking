package com.bank.digital_banking.service;

import com.bank.digital_banking.dto.TransferRequest;
import com.bank.digital_banking.entity.AccountBalance;
import com.bank.digital_banking.entity.AuditLog;
import com.bank.digital_banking.entity.Transaction;
import com.bank.digital_banking.repository.AccountBalanceRepository;
import com.bank.digital_banking.repository.AuditLogRepository;
import com.bank.digital_banking.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class TransactionService {
    private final AccountBalanceRepository balanceRepository;
    private final TransactionRepository transactionRepository;
    private final AuditLogRepository auditLogRepository;

    String requestId = java.util.UUID.randomUUID().toString();

    public TransactionService(AccountBalanceRepository balanceRepository, TransactionRepository transactionRepository, AuditLogRepository auditLogRepository) {
        this.balanceRepository = balanceRepository;
        this.transactionRepository = transactionRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public void transfer(TransferRequest request) {
        if (transactionRepository.existsByReferenceId(requestId)) {
            return;
        }
        AccountBalance sender = balanceRepository.findById(request.getFromAccountId())
                .orElseThrow(() ->
                        new RuntimeException("Sender balance not found"));

        AccountBalance receiver = balanceRepository.findById(request.getToAccountId())
                .orElseThrow(() ->
                        new RuntimeException("receiver balance not found"));

        BigDecimal amount = request.getAmount();

        if(sender.getBalance().compareTo(amount) < 0) {
            saveFailedTransaction(request, "FAILED");
            saveAudit("FAILED", "Insufficient balance");
            throw new RuntimeException("Insufficient balance");
        }

//        Debit
        sender.setBalance(sender.getBalance().subtract(amount));
//        Credit
        receiver.setBalance(receiver.getBalance().add(amount));

        balanceRepository.save(sender);
        balanceRepository.save(receiver);

        saveSuccessfulTransaction(request);
        saveAudit("SUCCESS", "Transfer successfully");
    }

    private void saveSuccessfulTransaction(TransferRequest request) {
        Transaction tx = new Transaction();
        tx.setAmount(request.getAmount());
        tx.setFromAccount(request.getFromAccountId());
        tx.setToAccount(request.getToAccountId());
        tx.setType("TRANSFER");
        tx.setStatus("SUCCESS");
        tx.setReferenceId(requestId);
        transactionRepository.save(tx);
    }

    private void saveFailedTransaction(TransferRequest request, String status) {
        Transaction tx = new Transaction();
        tx.setAmount(request.getAmount());
        tx.setFromAccount(request.getFromAccountId());
        tx.setToAccount(request.getToAccountId());
        tx.setType("TRANSFER");
        tx.setStatus(status);
        tx.setReferenceId(requestId);
        transactionRepository.save(tx);
    }

    private void saveAudit(String status, String message) {
        String actor = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        System.out.println("Actor"+ actor);
        AuditLog auditLog = new AuditLog();
        auditLog.setAction("TRANSFER");
        auditLog.setStatus(status);
        auditLog.setActor(actor);
        auditLog.setMessage(message);

        auditLogRepository.save(auditLog);
    }
}
