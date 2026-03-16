package com.nunez.finance.service;

import com.nunez.finance.model.Transaction;
import com.nunez.finance.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired // Inject the repository
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Optional<Transaction> getTransactionById(String id) {
        // Use the repository to fetch data from the database
        return transactionRepository.findById(id);
    }

    // You might want to add other methods here like getAllTransactions, saveTransaction, etc.
    // For now, let's add a mock method to ensure it works if repo is not fully set up yet.
    public List<Transaction> getAllTransactionsMock() {
        // Mock data if DB connection or repo isn't fully configured yet
        // In a real scenario, this would call transactionRepository.findAll()
        return List.of(
            new Transaction("1", "Mock Transaction 1", new BigDecimal("100.50"), LocalDate.now()),
            new Transaction("2", "Mock Transaction 2", new BigDecimal("250.75"), LocalDate.now().minusDays(5))
        );
    }
}
