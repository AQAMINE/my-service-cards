package com.myservicecards.my_service_cards.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bank {
    private UUID id;
    private UUID userId;
    private String name;
    private String code;
    private String websiteUrl;
    private String primaryColor;
    private String logoUrl;
    private boolean isSystem; // <-- Champ à ajouter
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}