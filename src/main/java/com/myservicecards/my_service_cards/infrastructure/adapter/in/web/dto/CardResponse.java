package com.myservicecards.my_service_cards.infrastructure.adapter.in.web.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CardResponse {
    private UUID id;
    private BankResponse bank;
    private ProviderResponse provider;
    private String cardHolderName;
    private String cardName;
    private String lastFourDigits;
    private int expiryMonth;
    private int expiryYear;
    private String cardColor;
}