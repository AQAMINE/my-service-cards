package com.myservicecards.my_service_cards.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record CreateCardRequest(
        @NotNull(message = "Bank ID is required")
        UUID bankId,

        @NotNull(message = "Provider ID is required")
        UUID providerId,

        @NotBlank(message = "Card holder name is required")
        String cardHolderName,

        @NotBlank(message = "Card name is required")
        String cardName,

        @NotBlank(message = "PAN is required")
        @Pattern(regexp = "^[0-9]{13,19}$", message = "Invalid PAN format")
        String pan,

        @NotBlank(message = "CVV is required")
        @Pattern(regexp = "^[0-9]{3,4}$", message = "Invalid CVV format")
        String cvv,

        @NotNull(message = "Expiry month is required")
        @Min(value = 1, message = "Month must be between 1 and 12")
        @Max(value = 12, message = "Month must be between 1 and 12")
        Integer expiryMonth,

        @NotNull(message = "Expiry year is required")
        @Min(value = 2025, message = "Year must be current or future")
        Integer expiryYear,

        String cardColor
) {}