package com.app.services;

import com.app.entites.Bank;
import com.app.payloads.BankDTO;
import com.app.payloads.BankResponse;

public interface  BankService {
    BankDTO createBank(Bank bank);
    BankResponse getBanks(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    BankDTO updateBank(Bank bank, Long bankId);
    String deleteBank(Long bankId);
}
