package com.myservicecards.my_service_cards.domain.service;

import com.myservicecards.my_service_cards.domain.model.Bank;
import com.myservicecards.my_service_cards.domain.model.BankCard;
import com.myservicecards.my_service_cards.domain.model.CardProvider;
import com.myservicecards.my_service_cards.ports.in.BankCardUseCase;
import com.myservicecards.my_service_cards.ports.out.BankCardRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BankCardService implements BankCardUseCase {

    private final BankCardRepositoryPort bankCardRepositoryPort;

    @Override
    public BankCard createCard(
            UUID bankId,
            UUID providerId,
            String cardHolderName,
            String cardName,
            String pan,
            String cvv,
            int expiryMonth,
            int expiryYear,
            String cardColor,
            UUID userId) {

        String lastFourDigits = (pan != null && pan.length() >= 4) 
                ? pan.substring(pan.length() - 4) 
                : "****";

        BankCard card = BankCard.builder()
                .bank(Bank.builder().id(bankId).build())
                .provider(CardProvider.builder().id(providerId).build())
                .cardHolderName(cardHolderName)
                .cardName(cardName)
                .lastFourDigits(lastFourDigits)
                .expiryMonth(expiryMonth)
                .expiryYear(expiryYear)
                .cardColor(cardColor != null ? cardColor : "#00ABE4")
                .userId(userId)
                .active(true)
                .build();

        return bankCardRepositoryPort.save(card, pan, cvv);
    }

    @Override
    public List<BankCard> getUserCards(UUID userId) {
        return bankCardRepositoryPort.findByUserId(userId);
    }

    @Override
    public String getDecryptedPan(UUID cardId, UUID userId) {
        return bankCardRepositoryPort.findDecryptedPan(cardId, userId);
    }

    @Override
    public String getDecryptedCvv(UUID cardId, UUID userId) {
        return bankCardRepositoryPort.findDecryptedCvv(cardId, userId);
    }

    @Override
    public void deleteCard(UUID cardId, UUID userId) {
        bankCardRepositoryPort.deleteByIdAndUserId(cardId, userId);
    }
}