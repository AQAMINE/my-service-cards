package com.myservicecards.my_service_cards.infrastructure.adapter.in.web.controller;

import com.myservicecards.my_service_cards.domain.model.Bank;
import com.myservicecards.my_service_cards.infrastructure.adapter.in.web.dto.BankResponse;
import com.myservicecards.my_service_cards.ports.in.BankUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/banks")
@RequiredArgsConstructor
public class BankController {

    private final BankUseCase bankUseCase;

    @GetMapping
    public ResponseEntity<List<BankResponse>> getAllBanks(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        List<BankResponse> banks = bankUseCase.getAllBanks(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ResponseEntity.ok(banks);
    }

    @PostMapping
    public ResponseEntity<BankResponse> createCustomBank(
            @RequestBody Bank bankRequest,
            @AuthenticationPrincipal Jwt jwt) {

        UUID userId = UUID.fromString(jwt.getSubject());
        bankRequest.setUserId(userId);

        Bank createdBank = bankUseCase.createBank(bankRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(createdBank));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBank(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt) {

        UUID userId = UUID.fromString(jwt.getSubject());
        bankUseCase.deleteBank(id, userId);

        return ResponseEntity.noContent().build();
    }

    private BankResponse mapToResponse(Bank bank) {
        return BankResponse.builder()
                .id(bank.getId())
                .name(bank.getName())
                .code(bank.getCode())
                .websiteUrl(bank.getWebsiteUrl())
                .primaryColor(bank.getPrimaryColor())
                .logoUrl(bank.getLogoUrl())
                .isSystem(bank.isSystem())
                .build();
    }
}
