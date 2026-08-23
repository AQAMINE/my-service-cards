package com.myservicecards.my_service_cards.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardProvider {
    private UUID id;
    private UUID userId;
    private String name;
    private String code;
    private String logoUrl;
    private Instant createdAt;
    private Instant updatedAt;

    public boolean isSystem() {
        return userId == null;
    }
}