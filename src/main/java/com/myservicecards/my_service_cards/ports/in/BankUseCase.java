package com.myservicecards.my_service_cards.ports.in;

import com.myservicecards.my_service_cards.domain.model.Bank;

import java.util.List;
import java.util.UUID;

public interface BankUseCase {
    List<Bank> getAllBanks(UUID userId);
    Bank createBank(Bank bank);
    void deleteBank(UUID bankId, UUID userId);
}