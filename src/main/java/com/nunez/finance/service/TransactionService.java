package com.nunez.finance.service;


import com.nunez.finance.dto.SharedTransactionRequest;
import com.nunez.finance.dto.TransactionRequest;
import com.nunez.finance.model.Account;
import com.nunez.finance.model.Category;
import com.nunez.finance.model.SharedExpense;
import com.nunez.finance.model.Transaction;
import com.nunez.finance.repository.AccountRepository;
import com.nunez.finance.repository.CategoryRepository;
import com.nunez.finance.repository.SharedExpenseRepository;
import com.nunez.finance.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    // Al ser 'final', @RequiredArgsConstructor los incluirá en el constructor
    private final TransactionRepository transactionRepo;
    private final AccountRepository accountRepo;
    private final CategoryRepository categoryRepo;
    private final SharedExpenseRepository sharedRepo;

    @Transactional
    public Transaction saveManual(TransactionRequest req) {
        final Account acc = this.accountRepo.findById(req.accountId())
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + req.accountId()));

        final Category cat = this.categoryRepo.findById(req.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + req.categoryId()));

        final Transaction tx = Transaction.builder()
                .date(req.date())
                .amount(req.amount())
                .description(req.description())
                .account(acc)
                .category(cat)
                .build();

        return this.transactionRepo.save(tx);
    }

    @Transactional
    public SharedExpense saveShared(SharedTransactionRequest req) {
        // 1. Guardamos la transacción base llamando al método anterior
        final Transaction tx = this.saveManual(req.transaction());

        // 2. Creamos y guardamos el detalle compartido
        final SharedExpense shared = SharedExpense.builder()
                .transaction(tx)
                .paidBy(req.paidBy())
                .danielDebt(req.danielDebt())
                .vanessaDebt(req.vanessaDebt())
                .build();

        return this.sharedRepo.save(shared);
    }
}