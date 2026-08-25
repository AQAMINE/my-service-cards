package com.myservicecards.my_service_cards.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "bank_cards")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankCardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bank_id", nullable = false)
    private BankEntity bank;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "provider_id", nullable = false)
    private CardProviderEntity provider;

    @Column(name = "card_holder_name", nullable = false, length = 100)
    private String cardHolderName;

    @Column(name = "card_name", nullable = false, length = 100)
    private String cardName;

    @Column(name = "last_four_digits", nullable = false, length = 4)
    private String lastFourDigits;

    @Column(name = "expiry_month", nullable = false)
    private int expiryMonth;

    @Column(name = "expiry_year", nullable = false)
    private int expiryYear;

    @Column(name = "card_color", length = 20)
    private String cardColor;

    @Column(name = "encrypted_pan", nullable = false, columnDefinition = "TEXT")
    private String encryptedPan;

    @Column(name = "pan_iv", nullable = false)
    private String panIv;

    @Column(name = "encrypted_cvv", nullable = false, columnDefinition = "TEXT")
    private String encryptedCvv;

    @Column(name = "cvv_iv", nullable = false)
    private String cvvIv;

    @Column(name = "encrypted_pin")
    private String encryptedPin;

    @Column(name = "pin_iv")
    private String pinIv;

    @Builder.Default
    @Column(name = "is_active", nullable = true)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}