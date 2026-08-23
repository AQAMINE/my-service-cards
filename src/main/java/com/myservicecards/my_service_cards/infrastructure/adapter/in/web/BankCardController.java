package com.myservicecards.my_service_cards.infrastructure.adapter.in.web;

import com.myservicecards.my_service_cards.domain.model.BankCard;
import com.myservicecards.my_service_cards.infrastructure.adapter.in.web.dto.CreateCardRequest;
import com.myservicecards.my_service_cards.ports.in.BankCardUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cards")
public class BankCardController {

    private final BankCardUseCase bankCardUseCase;

    public BankCardController(BankCardUseCase bankCardUseCase) {
        this.bankCardUseCase = bankCardUseCase;
    }

    @PostMapping
    public ResponseEntity<BankCard> createCard(
            @Valid @RequestBody CreateCardRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = extractUserId(jwt);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        BankCard createdCard = bankCardUseCase.createCard(
                request.bankId(),
                request.providerId(),
                request.cardHolderName(),
                request.cardName(),
                request.pan(),
                request.cvv(),
                request.expiryMonth(),
                request.expiryYear(),
                request.cardColor(),
                userId
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(createdCard);
    }

    @GetMapping
    public ResponseEntity<List<BankCard>> getUserCards(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = extractUserId(jwt);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<BankCard> cards = bankCardUseCase.getUserCards(userId);
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/{cardId}/reveal-pan")
    public ResponseEntity<Map<String, String>> getDecryptedPan(
            @PathVariable UUID cardId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = extractUserId(jwt);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String pan = bankCardUseCase.getDecryptedPan(cardId, userId);
        return ResponseEntity.ok(Map.of("pan", pan));
    }

    @GetMapping("/{cardId}/reveal-cvv")
    public ResponseEntity<Map<String, String>> getDecryptedCvv(
            @PathVariable UUID cardId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = extractUserId(jwt);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String cvv = bankCardUseCase.getDecryptedCvv(cardId, userId);
        return ResponseEntity.ok(Map.of("cvv", cvv));
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(
            @PathVariable UUID cardId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = extractUserId(jwt);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        bankCardUseCase.deleteCard(cardId, userId);
        return ResponseEntity.noContent().build();
    }

    private UUID extractUserId(Jwt jwt) {
        if (jwt == null || jwt.getSubject() == null) {
            return null;
        }
        return UUID.fromString(jwt.getSubject());
    }
}