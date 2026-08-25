package com.myservicecards.my_service_cards.infrastructure.adapter.out.persistence.repository;

import com.myservicecards.my_service_cards.infrastructure.adapter.out.persistence.entity.BankCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataBankCardRepository extends JpaRepository<BankCardEntity, UUID> {

    @Query("SELECT c FROM BankCardEntity c JOIN FETCH c.bank JOIN FETCH c.provider WHERE c.userId = :userId AND c.active = true")
    List<BankCardEntity> findByUserIdAndActiveTrue(@Param("userId") UUID userId);

    @Query("SELECT c FROM BankCardEntity c JOIN FETCH c.bank JOIN FETCH c.provider WHERE c.id = :id AND c.userId = :userId AND c.active = true")
    Optional<BankCardEntity> findByIdAndUserIdAndActiveTrue(@Param("id") UUID id, @Param("userId") UUID userId);
}