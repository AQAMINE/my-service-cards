package com.myservicecards.my_service_cards.ports.in;

import com.myservicecards.my_service_cards.domain.model.CardProvider;

import java.util.List;
import java.util.UUID;

public interface CardProviderUseCase {
    List<CardProvider> getAllProviders(UUID userId);
    CardProvider createProvider(CardProvider provider);
    void deleteProvider(UUID providerId, UUID userId);
}