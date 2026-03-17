package com.nunez.finance.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Getter // Usamos Lombok para limpiar el código según tus reglas
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // Long mapea perfectamente a bigserial/bigint

	@Column(nullable = false)
	private LocalDate date;

	@Column(nullable = false, precision = 15, scale = 2) // Ajustado a numeric(15, 2) de tu SQL
	private BigDecimal amount;

	private String description;

	@Column(name = "account_id", nullable = false)
	private Long accountId;

	@Column(name = "category_id", nullable = false)
	private Long categoryId;

	@Column(name = "destination_account_id")
	private Long destinationAccountId;
}