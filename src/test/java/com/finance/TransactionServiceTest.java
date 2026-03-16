package com.finance; // Adjust package to match your test structure

import com.finance.model.Transaction;
import com.finance.repository.TransactionRepository;
import com.finance.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TransactionServiceTest {

	@Mock
	private TransactionRepository transactionRepository;

	@InjectMocks
	private TransactionService transactionService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void getTransactionById_Found() {
		// Given
		final String transactionId = "test-123";
		final Transaction mockTransaction = new Transaction(transactionId, "Test Description", new BigDecimal("150.00"),
				LocalDate.now());
		when(this.transactionRepository.findById(transactionId)).thenReturn(Optional.of(mockTransaction));

		// When
		final Optional<Transaction> foundTransaction = this.transactionService.getTransactionById(transactionId);

		// Then
		assertTrue(foundTransaction.isPresent());
		assertEquals(transactionId, foundTransaction.get().getId());
		assertEquals("Test Description", foundTransaction.get().getDescription());
		assertEquals(new BigDecimal("150.00"), foundTransaction.get().getAmount());
		verify(this.transactionRepository).findById(transactionId); // Verify repository method was called
	}

	@Test
	void getTransactionById_NotFound() {
		// Given
		final String transactionId = "non-existent-id";
		when(this.transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

		// When
		final Optional<Transaction> foundTransaction = this.transactionService.getTransactionById(transactionId);

		// Then
		assertFalse(foundTransaction.isPresent());
		verify(this.transactionRepository).findById(transactionId); // Verify repository method was called
	}

	// Test for getAllTransactionsMock if needed, though it's a mock method
	@Test
	void getAllTransactionsMock_ReturnsList() {
		// Given
		// The mock method is hardcoded, so we just call it.

		// When
		final List<Transaction> transactions = this.transactionService.getAllTransactionsMock();

		// Then
		assertNotNull(transactions);
		assertFalse(transactions.isEmpty());
		assertEquals(2, transactions.size());
		assertEquals("1", transactions.get(0).getId());
		assertEquals("2", transactions.get(1).getId());
	}
}
