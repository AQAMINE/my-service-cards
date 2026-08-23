package com.myservicecards.my_service_cards.ports.out;

import com.myservicecards.my_service_cards.domain.model.Bank;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BankRepositoryPort {
    List<Bank> findAllSystemAndUserBanks(UUID userId);
    Bank save(Bank bank);
    Optional<Bank> findById(UUID id);
    void deleteById(UUID id);
}
