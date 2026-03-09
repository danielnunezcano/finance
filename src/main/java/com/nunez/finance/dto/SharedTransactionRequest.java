package com.nunez.finance.dto;


import java.math.BigDecimal;

public record SharedTransactionRequest(
    TransactionRequest transaction,
    String paidBy,
    BigDecimal danielDebt,
    BigDecimal vanessaDebt
) {}