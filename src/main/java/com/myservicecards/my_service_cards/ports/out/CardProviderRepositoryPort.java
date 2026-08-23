package com.myservicecards.my_service_cards.ports.out;

import com.myservicecards.my_service_cards.domain.model.CardProvider;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CardProviderRepositoryPort {
    List<CardProvider> findAllSystemAndUserProviders(UUID userId);
    CardProvider save(CardProvider provider);
    Optional<CardProvider> findById(UUID id);
    void deleteById(UUID id);
}