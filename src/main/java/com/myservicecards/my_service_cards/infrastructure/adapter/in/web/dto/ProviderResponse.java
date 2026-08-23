package com.myservicecards.my_service_cards.infrastructure.adapter.in.web.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ProviderResponse {
    private UUID id;
    private String name;
    private String code;
    private String logoUrl;
    private boolean isSystem;
}