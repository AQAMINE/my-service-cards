package com.myservicecards.my_service_cards.ports.in;

import com.myservicecards.my_service_cards.domain.model.BankCard;

import java.util.List;
import java.util.UUID;

public interface BankCardUseCase {
    BankCard createCard(
            UUID bankId,
            UUID providerId,
            String cardHolderName,
            String cardName,
            String pan,
            String cvv,
            int expiryMonth,
            int expiryYear,
            String cardColor,
            UUID userId
    );

    List<BankCard> getUserCards(UUID userId);
    String getDecryptedPan(UUID cardId, UUID userId);
    String getDecryptedCvv(UUID cardId, UUID userId);
    void deleteCard(UUID cardId, UUID userId);
}