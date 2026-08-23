package com.myservicecards.my_service_cards.infrastructure.adapter.out.persistence.repository;

import com.myservicecards.my_service_cards.infrastructure.adapter.out.persistence.entity.BankCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataBankCardRepository extends JpaRepository<BankCardEntity, UUID> {

    List<BankCardEntity> findByUserIdAndActiveTrue(UUID userId);

    Optional<BankCardEntity> findByIdAndUserIdAndActiveTrue(UUID id, UUID userId);
}