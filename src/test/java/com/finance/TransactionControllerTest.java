package com.finance; // Adjust package to match your test structure

import com.finance.controller.TransactionController;
import com.finance.model.Transaction;
import com.finance.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
// Import the main application class
import com.nunez.finance.FinanceApplication;
// Correct the import for @WebMvcTest and @MockBean for newer Spring versions
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
// No longer need MockMvcBuilders.standaloneSetup with @WebMvcTest

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Explicitly specify the application class for WebMvcTest
@WebMvcTest(controllers = TransactionController.class, classes = FinanceApplication.class)
class TransactionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean // Mock the service used by the controller
	private TransactionService transactionService;

	@BeforeEach
	void setUp() {
		// MockMvc is auto-configured by @WebMvcTest, no need for
		// MockMvcBuilders.standaloneSetup here
	}

	@Test
	void getTransactionById_Found() throws Exception {
		// Given
		final String transactionId = "test-controller-123";
		final Transaction mockTransaction = new Transaction(transactionId, "Controller Mock Description",
				new BigDecimal("200.00"), LocalDate.now());
		when(this.transactionService.getTransactionById(transactionId)).thenReturn(Optional.of(mockTransaction));

		// When & Then
		this.mockMvc.perform(get("/api/v1/transactions/{id}", transactionId))
				.andExpect(status().isOk()) // Expect HTTP 200 OK
				.andExpect(content().json(
						"{\"id\":\"test-controller-123\",\"description\":\"Controller Mock Description\",\"amount\":200.00,\"date\":\""
								+ LocalDate.now() + "\"}")) // Check the response body
				.andExpect(jsonPath("$.id").value(transactionId))
				.andExpect(jsonPath("$.description").value("Controller Mock Description"))
				.andExpect(jsonPath("$.amount").value(200.00)) // Check amount directly
				.andExpect(jsonPath("$.date").value(LocalDate.now().toString()));

		verify(this.transactionService).getTransactionById(transactionId); // Verify service method was called
	}

	@Test
	void getTransactionById_NotFound() throws Exception {
		// Given
		final String transactionId = "non-existent-id-controller";
		when(this.transactionService.getTransactionById(transactionId)).thenReturn(Optional.empty());

		// When & Then
		this.mockMvc.perform(get("/api/v1/transactions/{id}", transactionId))
				.andExpect(status().isOk()) // Controller returns null, Spring MVC returns 200 OK with empty body or JSON null
				.andExpect(content().string("null")); // Expecting JSON null for Optional.empty mapped to null

		verify(this.transactionService).getTransactionById(transactionId); // Verify service method was called
	}
}
