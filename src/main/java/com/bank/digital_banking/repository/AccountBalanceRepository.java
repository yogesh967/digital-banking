package com.bank.digital_banking.repository;

import com.bank.digital_banking.entity.AccountBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountBalanceRepository extends JpaRepository<AccountBalance, Long> {
}
