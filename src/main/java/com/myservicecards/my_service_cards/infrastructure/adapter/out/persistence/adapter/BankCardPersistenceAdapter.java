package com.myservicecards.my_service_cards.infrastructure.adapter.out.persistence.adapter;

import com.myservicecards.my_service_cards.domain.model.BankCard;
import com.myservicecards.my_service_cards.domain.model.CryptoResult;
import com.myservicecards.my_service_cards.infrastructure.adapter.out.persistence.entity.BankCardEntity;
import com.myservicecards.my_service_cards.infrastructure.adapter.out.persistence.entity.BankEntity;
import com.myservicecards.my_service_cards.infrastructure.adapter.out.persistence.entity.CardProviderEntity;
import com.myservicecards.my_service_cards.infrastructure.adapter.out.persistence.mapper.BankCardPersistenceMapper;
import com.myservicecards.my_service_cards.infrastructure.adapter.out.persistence.repository.SpringDataBankCardRepository;
import com.myservicecards.my_service_cards.ports.out.BankCardRepositoryPort;
import com.myservicecards.my_service_cards.ports.out.CardCryptoPort;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BankCardPersistenceAdapter implements BankCardRepositoryPort {

    private final SpringDataBankCardRepository bankCardRepository;
    private final CardCryptoPort cardCryptoPort;
    private final BankCardPersistenceMapper mapper;
    private final EntityManager entityManager;

    @Override
    public BankCard save(BankCard card, String pan, String cvv, String pin) {
        // 1. Chiffrement gRPC via my-service-crypto
        CryptoResult panCrypto = cardCryptoPort.encrypt(pan, card.getUserId());
        CryptoResult cvvCrypto = cardCryptoPort.encrypt(cvv, card.getUserId());

        // 2. Mapping vers l'entité JPA
        BankCardEntity entity = mapper.toEntity(card);

        entity.setBank(entityManager.getReference(BankEntity.class, card.getBank().getId()));
        entity.setProvider(entityManager.getReference(CardProviderEntity.class, card.getProvider().getId()));

        entity.setEncryptedPan(panCrypto.ciphertext());
        entity.setPanIv(panCrypto.iv());
        entity.setEncryptedCvv(cvvCrypto.ciphertext());
        entity.setCvvIv(cvvCrypto.iv());

        // 3. Chiffrement facultatif du PIN si fourni
        if (pin != null && !pin.isBlank()) {
            CryptoResult pinCrypto = cardCryptoPort.encrypt(pin, card.getUserId());
            entity.setEncryptedPin(pinCrypto.ciphertext());
            entity.setPinIv(pinCrypto.iv());
        }

        BankCardEntity savedEntity = bankCardRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<BankCard> findByIdAndUserId(UUID id, UUID userId) {
        return bankCardRepository.findByIdAndUserIdAndActiveTrue(id, userId)
                .map(mapper::toDomain);
    }

    @Override
    public List<BankCard> findByUserId(UUID userId) {
        return bankCardRepository.findByUserIdAndActiveTrue(userId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public String findDecryptedPan(UUID cardId, UUID userId) {
        BankCardEntity entity = bankCardRepository.findByIdAndUserIdAndActiveTrue(cardId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));

        return cardCryptoPort.decrypt(entity.getEncryptedPan(), entity.getPanIv(), userId);
    }

    @Override
    public String findDecryptedCvv(UUID cardId, UUID userId) {
        BankCardEntity entity = bankCardRepository.findByIdAndUserIdAndActiveTrue(cardId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));

        return cardCryptoPort.decrypt(entity.getEncryptedCvv(), entity.getCvvIv(), userId);
    }

    @Override
    public String findDecryptedPin(UUID cardId, UUID userId) {
        BankCardEntity entity = bankCardRepository.findByIdAndUserIdAndActiveTrue(cardId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));

        if (entity.getEncryptedPin() == null || entity.getPinIv() == null) {
            return null;
        }

        return cardCryptoPort.decrypt(entity.getEncryptedPin(), entity.getPinIv(), userId);
    }

    @Override
    public void deleteByIdAndUserId(UUID cardId, UUID userId) {
        BankCardEntity entity = bankCardRepository.findByIdAndUserIdAndActiveTrue(cardId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));
        entity.setActive(false);
        bankCardRepository.save(entity);
    }
}