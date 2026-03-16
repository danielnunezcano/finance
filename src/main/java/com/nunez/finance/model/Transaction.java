package com.nunez.finance.model;

import jakarta.persistence.*; // Using jakarta.persistence for Spring Boot 3+
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity // Marks this class as a JPA entity
@Table(name = "transactions") // Specifies the table name in the database
public class Transaction {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates ID
    private String id; // Changed to String to match controller's path variable and potential UUIDs

    private String description;

    @Column(precision = 10, scale = 2) // Specify precision and scale for currency
    private BigDecimal amount;

    private LocalDate date;

    // Constructors
    public Transaction() {
    }

    public Transaction(String id, String description, BigDecimal amount, LocalDate date) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
