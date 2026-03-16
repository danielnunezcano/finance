package com.finance.controller;

import com.finance.model.Transaction;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.math.BigDecimal; // Import necessary for BigDecimal if used

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable String id) {
        // Simulate a transaction object
        // Using double for amount as per the Transaction class definition
        return new Transaction(
            id,
            "Mock Transaction Description for ID: " + id,
            100.50 * (id.length() % 5 + 1), // Make amount slightly variable based on ID length
            LocalDate.now().minusDays(id.length() % 7)
        );
    }
}
