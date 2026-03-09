package com.nunez.finance.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "shared_expenses")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SharedExpense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "transaction_id", nullable = false, unique = true)
    private Transaction transaction;

    @Column(name = "paid_by", nullable = false)
    private String paidBy;

    @Column(name = "daniel_debt")
    private BigDecimal danielDebt;

    @Column(name = "vanessa_debt")
    private BigDecimal vanessaDebt;
}