package com.myservicecards.my_service_cards.infrastructure.adapter.out.persistence.adapter;

import com.myservicecards.my_service_cards.domain.model.CardProvider;
import com.myservicecards.my_service_cards.infrastructure.adapter.out.persistence.entity.CardProviderEntity;
import com.myservicecards.my_service_cards.infrastructure.adapter.out.persistence.mapper.ProviderPersistenceMapper;
import com.myservicecards.my_service_cards.infrastructure.adapter.out.persistence.repository.SpringDataCardProviderRepository;
import com.myservicecards.my_service_cards.ports.out.CardProviderRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CardProviderPersistenceAdapter implements CardProviderRepositoryPort {

    private final SpringDataCardProviderRepository providerRepository;
    private final ProviderPersistenceMapper mapper;

    @Override
    public List<CardProvider> findAllSystemAndUserProviders(UUID userId) {
        return providerRepository.findAllAvailableForUser(userId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public CardProvider save(CardProvider provider) {
        CardProviderEntity entity = mapper.toEntity(provider);
        CardProviderEntity savedEntity = providerRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<CardProvider> findById(UUID id) {
        return providerRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        providerRepository.deleteById(id);
    }
}