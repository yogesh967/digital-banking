package com.bank.digital_banking.repository;

import com.bank.digital_banking.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByFromAccountOrToAccount(Long fromAccountId, Long toAccountId);
    boolean existsByReferenceId(String referenceId);

}
