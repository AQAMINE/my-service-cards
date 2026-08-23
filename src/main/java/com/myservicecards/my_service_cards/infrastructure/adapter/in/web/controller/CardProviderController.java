package com.myservicecards.my_service_cards.infrastructure.adapter.in.web.controller;

import com.myservicecards.my_service_cards.domain.model.CardProvider;
import com.myservicecards.my_service_cards.infrastructure.adapter.in.web.dto.ProviderResponse;
import com.myservicecards.my_service_cards.ports.in.CardProviderUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/card-providers")
@RequiredArgsConstructor
public class CardProviderController {

    private final CardProviderUseCase providerUseCase;

    @GetMapping
    public ResponseEntity<List<ProviderResponse>> getAllProviders(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        List<ProviderResponse> providers = providerUseCase.getAllProviders(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ResponseEntity.ok(providers);
    }

    @PostMapping
    public ResponseEntity<ProviderResponse> createCustomProvider(
            @RequestBody CardProvider providerRequest,
            @AuthenticationPrincipal Jwt jwt) {

        UUID userId = UUID.fromString(jwt.getSubject());
        providerRequest.setUserId(userId);

        CardProvider createdProvider = providerUseCase.createProvider(providerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(createdProvider));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProvider(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt) {

        UUID userId = UUID.fromString(jwt.getSubject());
        providerUseCase.deleteProvider(id, userId);

        return ResponseEntity.noContent().build();
    }

    private ProviderResponse mapToResponse(CardProvider provider) {
        return ProviderResponse.builder()
                .id(provider.getId())
                .name(provider.getName())
                .code(provider.getCode())
                .logoUrl(provider.getLogoUrl())
                .isSystem(provider.isSystem())
                .build();
    }
}
