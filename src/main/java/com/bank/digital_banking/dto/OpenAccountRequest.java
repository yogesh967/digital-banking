package com.bank.digital_banking.dto;

import jakarta.validation.constraints.NotBlank;

public class OpenAccountRequest {

    @NotBlank
    private String accountType;
    private Long userId;

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
