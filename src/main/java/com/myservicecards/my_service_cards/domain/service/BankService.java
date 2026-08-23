package com.myservicecards.my_service_cards.domain.service;

import com.myservicecards.my_service_cards.domain.model.Bank;
import com.myservicecards.my_service_cards.ports.in.BankUseCase;
import com.myservicecards.my_service_cards.ports.out.BankRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BankService implements BankUseCase {

    private final BankRepositoryPort bankRepositoryPort;

    @Override
    public List<Bank> getAllBanks(UUID userId) {
        // Retourne les banques globales (user_id IS NULL) + les banques custom de l'utilisateur
        return bankRepositoryPort.findAllSystemAndUserBanks(userId);
    }

    @Override
    public Bank createBank(Bank bank) {
        return bankRepositoryPort.save(bank);
    }

    @Override
    public void deleteBank(UUID bankId, UUID userId) {
        Bank bank = bankRepositoryPort.findById(bankId)
                .orElseThrow(() -> new IllegalArgumentException("Banque introuvable."));

        // Protection : Les enregistrements système ne peuvent pas être supprimés
        if (bank.isSystem()) {
            throw new IllegalStateException("Impossible de supprimer une banque système par défaut.");
        }

        if (!bank.getUserId().equals(userId)) {
            throw new SecurityException("Vous n'avez pas l'autorisation de supprimer cette banque.");
        }

        bankRepositoryPort.deleteById(bankId);
    }
}