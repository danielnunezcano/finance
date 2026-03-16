package com.finance.test; // Adjust package to match your test structure

import com.finance.controller.TransactionController;
import com.finance.model.Transaction;
import com.finance.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class) // Scans for @Controller beans and auto-configures MockMvc
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // Mock the service used by the controller
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        // MockMvc is auto-configured by @WebMvcTest, no need for MockMvcBuilders.standaloneSetup here
    }

    @Test
    void getTransactionById_Found() throws Exception {
        // Given
        String transactionId = "test-controller-123";
        Transaction mockTransaction = new Transaction(
            transactionId,
            "Controller Mock Description",
            new BigDecimal("200.00"),
            LocalDate.now()
        );
        when(transactionService.getTransactionById(transactionId)).thenReturn(Optional.of(mockTransaction));

        // When & Then
        mockMvc.perform(get("/api/v1/transactions/{id}", transactionId))
                .andExpect(status().isOk()) // Expect HTTP 200 OK
                .andExpect(content().json("{\"id\":\"test-controller-123\",\"description\":\"Controller Mock Description\",\"amount\":200.00,\"date\":\"" + LocalDate.now().toString() + "\"}")) // Check the response body
                .andExpect(jsonPath("$.id").value(transactionId))
                .andExpect(jsonPath("$.description").value("Controller Mock Description"))
                .andExpect(jsonPath("$.amount").value(200.00)) // Check amount directly
                .andExpect(jsonPath("$.date").value(LocalDate.now().toString()));

        verify(transactionService).getTransactionById(transactionId); // Verify service method was called
    }

    @Test
    void getTransactionById_NotFound() throws Exception {
        // Given
        String transactionId = "non-existent-id-controller";
        when(transactionService.getTransactionById(transactionId)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/v1/transactions/{id}", transactionId))
                .andExpect(status().isOk()) // Controller returns null, Spring MVC returns 200 OK with empty body or JSON null
                .andExpect(content().string("null")); // Expecting JSON null for Optional.empty mapped to null

        verify(transactionService).getTransactionById(transactionId); // Verify service method was called
    }
}
