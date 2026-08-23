package com.myservicecards.my_service_cards.infrastructure.adapter.out.persistence.repository;

import com.myservicecards.my_service_cards.infrastructure.adapter.out.persistence.entity.CardProviderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SpringDataCardProviderRepository extends JpaRepository<CardProviderEntity, UUID> {

    @Query("SELECT p FROM CardProviderEntity p WHERE p.userId IS NULL OR p.userId = :userId")
    List<CardProviderEntity> findAllAvailableForUser(@Param("userId") UUID userId);
}