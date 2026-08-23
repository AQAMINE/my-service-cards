package com.myservicecards.my_service_cards.infrastructure.adapter.out.persistence.mapper;

import com.myservicecards.my_service_cards.domain.model.CardProvider;
import com.myservicecards.my_service_cards.infrastructure.adapter.out.persistence.entity.CardProviderEntity;
import org.springframework.stereotype.Component;

@Component
public class ProviderPersistenceMapper {

    public CardProviderEntity toEntity(CardProvider domain) {
        if (domain == null) return null;
        return CardProviderEntity.builder()
                .id(domain.getId())
                .userId(domain.getUserId())
                .name(domain.getName())
                .code(domain.getCode())
                .logoUrl(domain.getLogoUrl())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }

    public CardProvider toDomain(CardProviderEntity entity) {
        if (entity == null) return null;
        return CardProvider.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .name(entity.getName())
                .code(entity.getCode())
                .logoUrl(entity.getLogoUrl())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}