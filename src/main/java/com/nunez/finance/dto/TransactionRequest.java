package com.nunez.finance.dto;


import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequest(
    LocalDate date,
    BigDecimal amount,
    String description,
    Long accountId,
    Long categoryId,
    Long destinationAccountId // Solo para traspasos
) {}