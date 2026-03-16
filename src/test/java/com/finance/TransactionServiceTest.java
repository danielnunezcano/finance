package com.nunez.finance; // Corrected package

import com.nunez.finance.model.Transaction; // Corrected import
import com.nunez.finance.repository.TransactionRepository; // Corrected import
import com.nunez.finance.service.TransactionService; // Corrected import
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.List; // Import List

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        String transactionId = "test-123";
        Transaction mockTransaction = new Transaction(
            transactionId,
            "Test Description",
            new BigDecimal("150.00"),
            LocalDate.now()
        );
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(mockTransaction));

        // When
        Optional<Transaction> foundTransaction = transactionService.getTransactionById(transactionId);

        // Then
        assertTrue(foundTransaction.isPresent());
        assertEquals(transactionId, foundTransaction.get().getId());
        assertEquals("Test Description", foundTransaction.get().getDescription());
        assertEquals(new BigDecimal("150.00"), foundTransaction.get().getAmount());
        verify(transactionRepository).findById(transactionId); // Verify repository method was called
    }

    @Test
    void getTransactionById_NotFound() {
        // Given
        String transactionId = "non-existent-id";
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        // When
        Optional<Transaction> foundTransaction = transactionService.getTransactionById(transactionId);

        // Then
        assertFalse(foundTransaction.isPresent());
        verify(transactionRepository).findById(transactionId); // Verify repository method was called
    }

    // Test for getAllTransactionsMock if needed, though it's a mock method
    @Test
    void getAllTransactionsMock_ReturnsList() {
        // Given
        // The mock method is hardcoded, so we just call it.

        // When
        List<Transaction> transactions = transactionService.getAllTransactionsMock();

        // Then
        assertNotNull(transactions);
        assertFalse(transactions.isEmpty());
        assertEquals(2, transactions.size());
        assertEquals("1", transactions.get(0).getId());
        assertEquals("2", transactions.get(1).getId());
    }
}
