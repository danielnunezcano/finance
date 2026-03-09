package com.nunez.finance.repository;

import com.nunez.finance.model.SharedExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SharedExpenseRepository extends JpaRepository<SharedExpense, Long> {
    // Aquí podrías añadir en el futuro:
    // List<SharedExpense> findByPaidBy(String personName);
}