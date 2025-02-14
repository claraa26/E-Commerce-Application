package com.app.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.app.entites.Bank;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.BankDTO;
import com.app.payloads.BankResponse;
import com.app.repositories.BankRepo;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class BankServiceImpl implements BankService{
    @Autowired
    private BankRepo bankRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public BankDTO createBank(Bank bank) {
        Bank savedBank = bankRepo.findByBankName(bank.getBankName());
        if (savedBank != null) {
            throw new APIException("Bank with the name '" + bank.getBankName() + "' already exists !!!");
        }
        savedBank = bankRepo.save(bank);
        return modelMapper.map(savedBank, BankDTO.class);
    }

    @Override
    public BankResponse getBanks(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Bank> pageBanks = bankRepo.findAll(pageDetails);
        List<Bank> banks = pageBanks.getContent();

        if (banks.isEmpty()) {
            throw new APIException("No bank is created yet.");
        }

        List<BankDTO> bankDTOs = banks.stream().map(bank -> modelMapper.map(bank, BankDTO.class))
                .collect(Collectors.toList());

        BankResponse bankResponse = new BankResponse();
        bankResponse.setContent(bankDTOs);
        bankResponse.setPageNumber(pageBanks.getNumber());
        bankResponse.setPageSize(pageBanks.getSize());
        bankResponse.setTotalElements(pageBanks.getTotalElements());
        bankResponse.setTotalPages(pageBanks.getTotalPages());
        bankResponse.setLastPage(pageBanks.isLast());

        return bankResponse;
    }

    @Override
    public BankDTO updateBank(Bank bank, Long bankId) {
        Bank savedBank = bankRepo.findById(bankId)
                .orElseThrow(() -> new ResourceNotFoundException("Bank", "bankId", bankId));

        bank.setBankId(bankId);
        savedBank = bankRepo.save(bank);

        return modelMapper.map(savedBank, BankDTO.class);
    }

    @Override
    public String deleteBank(Long bankId) {
        Bank bank = bankRepo.findById(bankId)
                .orElseThrow(() -> new ResourceNotFoundException("Bank", "bankId", bankId));

        bankRepo.delete(bank);
        return "Bank with bankId: " + bankId + " deleted successfully !!!";
    }
    
}
