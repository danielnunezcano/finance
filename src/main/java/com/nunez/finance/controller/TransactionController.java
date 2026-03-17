package com.nunez.finance.controller;

import com.nunez.finance.model.Transaction;
import com.nunez.finance.service.TransactionService;
import com.nunez.finance.domain.PrimeNumberService; // Import for Prime Number Service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final PrimeNumberService primeNumberService; // Inject Prime Number Service

    @Autowired // Inject services
    public TransactionController(TransactionService transactionService, PrimeNumberService primeNumberService) {
        this.transactionService = transactionService;
        this.primeNumberService = primeNumberService;
    }

    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable String id) {
        // Use the service to get the transaction
        return transactionService.getTransactionById(id)
                .orElse(null); // Return null if not found, or handle appropriately
    }

    // New endpoint to get prime numbers
    @GetMapping("/primes/{count}")
    public List<Integer> getPrimes(@PathVariable int count) {
        return primeNumberService.getFirstNPrimes(count);
    }
}
