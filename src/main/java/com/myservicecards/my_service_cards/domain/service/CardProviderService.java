package com.myservicecards.my_service_cards.domain.service;

import com.myservicecards.my_service_cards.domain.model.CardProvider;
import com.myservicecards.my_service_cards.ports.in.CardProviderUseCase;
import com.myservicecards.my_service_cards.ports.out.CardProviderRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardProviderService implements CardProviderUseCase {

    private final CardProviderRepositoryPort providerRepositoryPort;

    @Override
    public List<CardProvider> getAllProviders(UUID userId) {
        return providerRepositoryPort.findAllSystemAndUserProviders(userId);
    }

    @Override
    public CardProvider createProvider(CardProvider provider) {
        return providerRepositoryPort.save(provider);
    }

    @Override
    public void deleteProvider(UUID providerId, UUID userId) {
        CardProvider provider = providerRepositoryPort.findById(providerId)
                .orElseThrow(() -> new IllegalArgumentException("Provider introuvable."));

        // Protection : Les enregistrements système ne peuvent pas être supprimés
        if (provider.isSystem()) {
            throw new IllegalStateException("Impossible de supprimer un provider système par défaut.");
        }

        if (!provider.getUserId().equals(userId)) {
            throw new SecurityException("Vous n'avez pas l'autorisation de supprimer ce provider.");
        }

        providerRepositoryPort.deleteById(providerId);
    }
}