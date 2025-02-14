package com.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.config.AppConstants;
import com.app.entites.Bank;
import com.app.payloads.BankDTO;
import com.app.payloads.BankResponse;
import com.app.services.BankService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class BankController {
    @Autowired
    private BankService bankService;

    @PostMapping("/admin/bank")
    public ResponseEntity<BankDTO> createBank(@Valid @RequestBody Bank bank) {
        BankDTO savedBankDTO = bankService.createBank(bank);
        return new ResponseEntity<>(savedBankDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/banks")
    public ResponseEntity<BankResponse> getBanks(
        @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
        @RequestParam(name = "sortBy", defaultValue = "bankName") String sortBy,
        @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        BankResponse banks = bankService.getBanks(pageNumber, pageSize, sortBy, sortOrder);
        return ResponseEntity.ok(banks);
    }

    @PutMapping("/admin/banks/{bankId}")
    public ResponseEntity<BankDTO> updateBank(@RequestBody Bank bank, @PathVariable Long bankId) {
        BankDTO updatedBankDTO = bankService.updateBank(bank, bankId);
        return new ResponseEntity<>(updatedBankDTO, HttpStatus.OK);
    }

    @DeleteMapping("/admin/banks/{bankId}")
    public ResponseEntity<String> deleteBank(@PathVariable Long bankId) {
        String status = bankService.deleteBank(bankId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
    
}
