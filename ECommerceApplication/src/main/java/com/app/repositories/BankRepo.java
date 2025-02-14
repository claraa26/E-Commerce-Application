package com.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.entites.Bank;

@Repository
public interface BankRepo extends JpaRepository<Bank, Long> {
    Optional<Bank> findByBankNameIgnoreCase(String bankName);
}
