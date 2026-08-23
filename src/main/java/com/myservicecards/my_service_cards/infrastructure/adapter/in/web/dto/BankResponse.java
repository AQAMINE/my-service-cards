package com.myservicecards.my_service_cards.infrastructure.adapter.in.web.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class BankResponse {
    private UUID id;
    private String name;
    private String code;
    private String websiteUrl;
    private String primaryColor;
    private String logoUrl;
    private boolean isSystem;
}