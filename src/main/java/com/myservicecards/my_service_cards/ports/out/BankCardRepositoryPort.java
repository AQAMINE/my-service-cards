package com.myservicecards.my_service_cards.ports.out;

import com.myservicecards.my_service_cards.domain.model.BankCard;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BankCardRepositoryPort {
    // Modification: ajout de String pin
    BankCard save(BankCard bankCard, String pan, String cvv, String pin);
    List<BankCard> findByUserId(UUID userId);
    Optional<BankCard> findByIdAndUserId(UUID id, UUID userId);
    String findDecryptedPan(UUID cardId, UUID userId);
    String findDecryptedCvv(UUID cardId, UUID userId);
    // Rajout:
    String findDecryptedPin(UUID cardId, UUID userId);
    void deleteByIdAndUserId(UUID id, UUID userId);
}