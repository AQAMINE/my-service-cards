package com.myservicecards.my_service_cards.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankCard {
    private UUID id;
    private UUID userId;
    private Bank bank;
    private CardProvider provider;
    private String cardHolderName;
    private String cardName;
    private String lastFourDigits;
    private int expiryMonth;
    private int expiryYear;
    private String cardColor;
    private String encryptedPan;
    private String encryptedCvv;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
}