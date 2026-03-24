package com.nunez.finance.service;

import com.nunez.finance.model.Transaction;
import com.nunez.finance.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

	private final TransactionRepository transactionRepository;

	public Optional<Transaction> getTransactionById(String id) {
		// Use the repository to fetch data from the database
		return this.transactionRepository.findById(id);
	}

	// You might want to add other methods here like getAllTransactions,
	// saveTransaction, etc.
	// For now, let's add a mock method to ensure it works if repo is not fully set
	// up yet.
	public List<Transaction> getAllTransactionsMock() {
		// Mock data if DB connection or repo isn't fully configured yet
		// In a real scenario, this would call transactionRepository.findAll()
		return List.of(
				Transaction.builder().id(1L).description("Mock Transaction 1").amount(new BigDecimal("100.50"))
						.date(LocalDate.now()).build(),
				Transaction.builder().id(2L).description("Mock Transaction 2").amount(new BigDecimal("250.75"))
						.date(LocalDate.now()).build());
	}
}
