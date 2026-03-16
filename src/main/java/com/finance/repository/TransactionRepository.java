package com.finance.repository;

import com.finance.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    // JpaRepository provides basic CRUD operations (save, findById, findAll, delete, etc.)
    // You can add custom query methods here if needed.
    // For example: Optional<Transaction> findById(String id);
}
