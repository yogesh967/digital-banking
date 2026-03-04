package com.bank.digital_banking.repository;

import com.bank.digital_banking.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long>{
}
