package com.nunez.finance.controller;

import com.nunez.finance.model.Transaction;
import com.nunez.finance.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired // Inject the service
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable String id) {
        // Use the service to get the transaction
        return transactionService.getTransactionById(id)
                .orElse(null); // Return null if not found, or handle appropriately
    }
}
