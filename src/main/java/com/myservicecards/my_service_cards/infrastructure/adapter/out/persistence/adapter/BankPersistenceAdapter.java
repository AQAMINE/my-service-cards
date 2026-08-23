package com.myservicecards.my_service_cards.infrastructure.adapter.out.persistence.adapter;

import com.myservicecards.my_service_cards.domain.model.Bank;
import com.myservicecards.my_service_cards.infrastructure.adapter.out.persistence.entity.BankEntity;
import com.myservicecards.my_service_cards.infrastructure.adapter.out.persistence.repository.SpringDataBankRepository;
import com.myservicecards.my_service_cards.ports.out.BankRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BankPersistenceAdapter implements BankRepositoryPort {

    private final SpringDataBankRepository repository;

    @Override
    public List<Bank> findAllSystemAndUserBanks(UUID userId) {
        return repository.findByIsSystemTrueOrUserId(userId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Bank save(Bank bank) {
        BankEntity entity = toEntity(bank);
        BankEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Bank> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    private BankEntity toEntity(Bank domain) {
        return BankEntity.builder()
                .id(domain.getId())
                .userId(domain.getUserId())
                .name(domain.getName())
                .code(domain.getCode())
                .websiteUrl(domain.getWebsiteUrl())
                .primaryColor(domain.getPrimaryColor())
                .logoUrl(domain.getLogoUrl())
                .createdAt(domain.getCreatedAt() != null ? domain.getCreatedAt().toInstant(ZoneOffset.UTC) : null)
                .updatedAt(domain.getUpdatedAt() != null ? domain.getUpdatedAt().toInstant(ZoneOffset.UTC) : null)
                .build();
    }

    private Bank toDomain(BankEntity entity) {
        return Bank.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .name(entity.getName())
                .code(entity.getCode())
                .websiteUrl(entity.getWebsiteUrl())
                .primaryColor(entity.getPrimaryColor())
                .logoUrl(entity.getLogoUrl())
                .isSystem(entity.isSystem())
                .createdAt(entity.getCreatedAt() != null ? entity.getCreatedAt().atZone(ZoneOffset.UTC).toLocalDateTime() : null)
                .updatedAt(entity.getUpdatedAt() != null ? entity.getUpdatedAt().atZone(ZoneOffset.UTC).toLocalDateTime() : null)
                .build();
    }
}