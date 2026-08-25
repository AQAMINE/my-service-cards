package com.myservicecards.my_service_cards.infrastructure.adapter.out.persistence.mapper;

import com.myservicecards.my_service_cards.domain.model.Bank;
import com.myservicecards.my_service_cards.domain.model.BankCard;
import com.myservicecards.my_service_cards.domain.model.CardProvider;
import com.myservicecards.my_service_cards.infrastructure.adapter.out.persistence.entity.BankCardEntity;
import org.springframework.stereotype.Component;

@Component
public class BankCardPersistenceMapper {

    public BankCard toDomain(BankCardEntity entity) {
        if (entity == null) {
            return null;
        }

        Bank bank = null;
        if (entity.getBank() != null) {
            bank = Bank.builder()
                    .id(entity.getBank().getId())
                    .userId(entity.getBank().getUserId())
                    .name(entity.getBank().getName())
                    .code(entity.getBank().getCode())
                    .websiteUrl(entity.getBank().getWebsiteUrl())
                    .primaryColor(entity.getBank().getPrimaryColor())
                    .logoUrl(entity.getBank().getLogoUrl())
                    .isSystem(entity.getBank().isSystem())
                    .createdAt(entity.getBank().getCreatedAt() != null ? entity.getBank().getCreatedAt().atZone(java.time.ZoneOffset.UTC).toLocalDateTime() : null)
                    .updatedAt(entity.getBank().getUpdatedAt() != null ? entity.getBank().getUpdatedAt().atZone(java.time.ZoneOffset.UTC).toLocalDateTime() : null)
                    .build();
        }

        CardProvider provider = null;
        if (entity.getProvider() != null) {
            provider = CardProvider.builder()
                    .id(entity.getProvider().getId())
                    .userId(entity.getProvider().getUserId())
                    .name(entity.getProvider().getName())
                    .code(entity.getProvider().getCode())
                    .logoUrl(entity.getProvider().getLogoUrl())
                    .createdAt(entity.getProvider().getCreatedAt())
                    .updatedAt(entity.getProvider().getUpdatedAt())
                    .build();
        }

        return BankCard.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .bank(bank)
                .provider(provider)
                .cardHolderName(entity.getCardHolderName())
                .cardName(entity.getCardName())
                .lastFourDigits(entity.getLastFourDigits())
                .expiryMonth(entity.getExpiryMonth())
                .expiryYear(entity.getExpiryYear())
                .cardColor(entity.getCardColor())
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public BankCardEntity toEntity(BankCard domain) {
        if (domain == null) {
            return null;
        }

        return BankCardEntity.builder()
                .id(domain.getId())
                .userId(domain.getUserId())
                .cardHolderName(domain.getCardHolderName())
                .cardName(domain.getCardName())
                .lastFourDigits(domain.getLastFourDigits())
                .expiryMonth(domain.getExpiryMonth())
                .expiryYear(domain.getExpiryYear())
                .cardColor(domain.getCardColor())
                .active(domain.isActive())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}