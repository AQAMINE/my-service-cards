package com.myservicecards.my_service_cards.ports.out;

import com.myservicecards.my_service_cards.domain.model.CryptoResult;

import java.util.UUID;

public interface CardCryptoPort {

    CryptoResult encrypt(String plaintext, UUID userId);

    String decrypt(String ciphertext, String iv, UUID userId);
}