package com.myservicecards.my_service_cards.infrastructure.adapter.out.persistence.repository;

import com.myservicecards.my_service_cards.infrastructure.adapter.out.persistence.entity.BankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SpringDataBankRepository extends JpaRepository<BankEntity, UUID> {

    @Query("SELECT b FROM BankEntity b WHERE b.userId IS NULL OR b.userId = :userId")
    List<BankEntity> findByIsSystemTrueOrUserId(@Param("userId") UUID userId);

}