package com.nunez.finance.controller;


import com.nunez.finance.dto.SharedTransactionRequest;
import com.nunez.finance.dto.TransactionRequest;
import com.nunez.finance.model.SharedExpense;
import com.nunez.finance.model.Transaction;
import com.nunez.finance.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    // Endpoint para un gasto individual o ingreso
    @PostMapping
    public ResponseEntity<Transaction> create(@RequestBody TransactionRequest request) {
        return ResponseEntity.ok(this.transactionService.saveManual(request));
    }

    // Endpoint para los gastos del "Bote" (sustituye tu Excel de Compartidos)
    @PostMapping("/shared")
    public ResponseEntity<SharedExpense> createShared(@RequestBody SharedTransactionRequest request) {
        return ResponseEntity.ok(this.transactionService.saveShared(request));
    }
}