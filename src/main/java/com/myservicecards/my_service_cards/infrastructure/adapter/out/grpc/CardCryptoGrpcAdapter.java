package com.myservicecards.my_service_cards.infrastructure.adapter.out.grpc;

import com.myservice.crypto.grpc.CryptoServiceGrpc;
import com.myservice.crypto.grpc.DecryptRequest;
import com.myservice.crypto.grpc.EncryptRequest;
import com.myservice.crypto.grpc.EncryptResponse;
import com.myservicecards.my_service_cards.domain.model.CryptoResult;
import com.myservicecards.my_service_cards.ports.out.CardCryptoPort;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CardCryptoGrpcAdapter implements CardCryptoPort {

    private static final Logger log = LoggerFactory.getLogger(CardCryptoGrpcAdapter.class);

    @GrpcClient("crypto-service")
    private CryptoServiceGrpc.CryptoServiceBlockingStub cryptoStub;

    @Override
    public CryptoResult encrypt(String plaintext, UUID userId) {
        if (plaintext == null || plaintext.isBlank()) {
            throw new IllegalArgumentException("Plaintext cannot be null or empty for encryption");
        }

        log.debug("[ENCRYPT] Requesting encryption for userId: {}", userId);

        EncryptRequest request = EncryptRequest.newBuilder()
                .setPlainText(plaintext)
                .build();

        EncryptResponse response = cryptoStub.encrypt(request);
        log.debug("[ENCRYPT] Successfully encrypted data for userId: {}", userId);

        return new CryptoResult(response.getCipherTextBase64(), response.getIvB64());
    }

    @Override
    public String decrypt(String ciphertext, String iv, UUID userId) {
        if (ciphertext == null || ciphertext.isBlank()) {
            throw new IllegalArgumentException("Ciphertext cannot be null or empty for decryption");
        }
        if (iv == null || iv.isBlank()) {
            throw new IllegalArgumentException("IV cannot be null or empty for decryption");
        }

        log.debug("[DECRYPT] Requesting decryption for userId: {}", userId);

        DecryptRequest request = DecryptRequest.newBuilder()
                .setCipherTextBase64(ciphertext)
                .setIvB64(iv)
                .build();

        String plaintext = cryptoStub.decrypt(request).getPlainText();
        log.debug("[DECRYPT] Successfully decrypted data for userId: {}", userId);

        return plaintext;
    }
}