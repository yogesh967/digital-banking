package com.bank.digital_banking.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class TransferRequest {
    @NotNull
    private Long fromAccountId;

    @NotNull
    private long toAccountId;

    @NotNull
    private BigDecimal amount;

    public Long getFromAccountId() {
        return fromAccountId;
    }

    public long getToAccountId() {
        return toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
