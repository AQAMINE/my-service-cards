# Hexagonal Architecture — my-service-cards

This service manages **banks**, **card providers** (Visa, Mastercard, …), and **bank cards** for authenticated users. Sensitive data (PAN, CVV) is encrypted via **my-service-crypto** over gRPC before persistence. The layout follows **hexagonal (ports & adapters)** so business rules stay isolated from frameworks (Spring, JPA, gRPC, Keycloak).

## Package structure

```
com.myservicecards.my_service_cards
├── domain
│   ├── model
│   │   ├── Bank
│   │   ├── CardProvider
│   │   └── BankCard
│   ├── service
│   │   ├── BankService
│   │   ├── CardProviderService
│   │   └── BankCardService
│   └── exception
├── ports
│   ├── in
│   │   ├── BankUseCase
│   │   ├── CardProviderUseCase
│   │   └── BankCardUseCase
│   └── out
│       ├── BankRepositoryPort
│       ├── CardProviderRepositoryPort
│       ├── BankCardRepositoryPort
│       └── CardCryptoPort
├── infrastructure
│   ├── adapter
│   │   ├── in
│   │   │   └── web
│   │   │       ├── controller/
│   │   │       │   ├── BankController
│   │   │       │   ├── CardProviderController
│   │   │       │   ├── BankCardController
│   │   │       │   └── GlobalExceptionHandler
│   │   │       └── dto/          # BankResponse, ProviderResponse, CreateCardRequest, CardResponse
│   │   └── out
│   │       ├── grpc
│   │       │   └── CardCryptoGrpcAdapter
│   │       └── persistence
│   │           ├── adapter/      # Bank*, CardProvider*, BankCard* PersistenceAdapter
│   │           ├── entity/       # BankEntity, CardProviderEntity, BankCardEntity
│   │           ├── mapper/       # ProviderPersistenceMapper, BankCardPersistenceMapper
│   │           └── repository/   # SpringData*Repository
│   └── config
│       └── security              # SecurityConfig, KeycloakJwtAuthenticationConverter, IsUserOrAdmin
├── MyServiceCardsApplication.java
└── src/main/proto/crypto.proto   # gRPC contract (shared with my-service-crypto)
```

## Request flow

### Bank cards (encrypt on write)

```
Client (JWT)
    │
    ▼
BankCardController              ← infrastructure/adapter/in/web/controller
    │
    ▼
BankCardUseCase                 ← ports/in
    │
    ▼
BankCardService                 ← domain/service
    │
    └──► BankCardRepositoryPort
              │
              ▼
         BankCardPersistenceAdapter
              │
              ├──► CardCryptoPort ──► CardCryptoGrpcAdapter ──► my-service-crypto
              └──► Spring Data / PostgreSQL (banks, card_providers, bank_cards)
```

### Banks & card providers

```
Controller → UseCase → Service → RepositoryPort → PersistenceAdapter → PostgreSQL
```

System rows (`user_id IS NULL`) cannot be deleted; users may create/delete their own custom rows.

## Dependency rule

| Layer | May depend on |
|-------|----------------|
| `domain/model` | Nothing outside itself |
| `domain/service` | `domain`, `ports.in`, `ports.out` |
| `ports` | `domain` only |
| `infrastructure` | Everything (implements ports, wires frameworks) |

Controllers depend on `ports.in`, never on JPA or gRPC directly.

## Implemented components

### Domain models

| Class | Role |
|-------|------|
| `Bank` | Bank catalog entry (system or user-owned): name, code, colors, logo |
| `CardProvider` | Network/brand (Visa, Mastercard, …): name, code, logo |
| `BankCard` | User card: bank + provider refs, holder/name, expiry, last four, encrypted PAN/CVV, `active` |

### Domain services

| Class | Implements | Role |
|-------|------------|------|
| `BankService` | `BankUseCase` | List system+user banks; create/delete custom banks |
| `CardProviderService` | `CardProviderUseCase` | List system+user providers; create/delete custom providers |
| `BankCardService` | `BankCardUseCase` | Build card aggregate (`active=true`), delegate save/list/reveal/delete |

### Ports (in)

| Interface | Methods |
|-----------|---------|
| `BankUseCase` | `getAllBanks`, `createBank`, `deleteBank` |
| `CardProviderUseCase` | `getAllProviders`, `createProvider`, `deleteProvider` |
| `BankCardUseCase` | `createCard`, `getUserCards`, `getDecryptedPan`, `getDecryptedCvv`, `deleteCard` |

### Ports (out)

| Interface | Implemented by |
|-----------|----------------|
| `BankRepositoryPort` | `BankPersistenceAdapter` |
| `CardProviderRepositoryPort` | `CardProviderPersistenceAdapter` |
| `BankCardRepositoryPort` | `BankCardPersistenceAdapter` |
| `CardCryptoPort` | `CardCryptoGrpcAdapter` |

`BankCardRepositoryPort.save(BankCard, pan, cvv)` receives plaintext PAN/CVV once; the adapter encrypts them before writing.

### Inbound adapters — REST (`web/controller`)

| Class | Base path |
|-------|-----------|
| `BankController` | `/api/v1/banks` |
| `CardProviderController` | `/api/v1/card-providers` |
| `BankCardController` | `/api/v1/cards` |
| `GlobalExceptionHandler` | Maps domain/runtime errors to HTTP responses |

**Banks**

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/api/v1/banks` | System + current user's banks |
| `POST` | `/api/v1/banks` | Create a custom bank |
| `DELETE` | `/api/v1/banks/{id}` | Delete a custom bank (not system) |

**Card providers**

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/api/v1/card-providers` | System + current user's providers |
| `POST` | `/api/v1/card-providers` | Create a custom provider |
| `DELETE` | `/api/v1/card-providers/{id}` | Delete a custom provider (not system) |

**Bank cards**

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/api/v1/cards` | Create card (PAN/CVV encrypted before save) |
| `GET` | `/api/v1/cards` | List current user's active cards |
| `GET` | `/api/v1/cards/{cardId}/reveal-pan` | Decrypt and return PAN |
| `GET` | `/api/v1/cards/{cardId}/reveal-cvv` | Decrypt and return CVV |
| `DELETE` | `/api/v1/cards/{cardId}` | Soft-delete (`is_active = false`) |

User identity comes from the JWT `sub` claim (`UUID`).

### Outbound adapters

| Class | Role |
|-------|------|
| `CardCryptoGrpcAdapter` | `@GrpcClient("crypto-service")`, key alias `cards-credentials` |
| `BankPersistenceAdapter` | Implements `BankRepositoryPort` |
| `CardProviderPersistenceAdapter` | Implements `CardProviderRepositoryPort` |
| `BankCardPersistenceAdapter` | Encrypts PAN/CVV, resolves bank/provider entities, maps via `BankCardPersistenceMapper` |
| `BankCardPersistenceMapper` / `ProviderPersistenceMapper` | Entity ↔ domain mapping |
| `SpringDataBankRepository` / `SpringDataCardProviderRepository` / `SpringDataBankCardRepository` | Spring Data JPA |

### Config

| Class | Role |
|-------|------|
| `SecurityConfig` | Stateless OAuth2 resource server (JWT) |
| `KeycloakJwtAuthenticationConverter` | Maps Keycloak roles → Spring authorities |
| `IsUserOrAdmin` | Method-level security annotation |

## External dependencies

| System | Purpose | Config |
|--------|---------|--------|
| **PostgreSQL** | `banks`, `card_providers`, `bank_cards` | `spring.datasource` → `my_cards_db` (port `8083`) |
| **Keycloak** | JWT validation | `spring.security.oauth2.resourceserver.jwt` → `:8085` |
| **my-service-crypto** | PAN/CVV encrypt/decrypt | `grpc.client.crypto-service` → `:50051` |

## Database

Flyway `V1__init_cards_schema.sql`:

| Table | Notes |
|-------|--------|
| `banks` | `user_id NULL` = system bank; seeded defaults |
| `card_providers` | `user_id NULL` = system provider; seeded (Visa, Mastercard, …) |
| `bank_cards` | FK to bank + provider; `encrypted_pan` / `encrypted_cvv`; soft delete via `is_active` |

Only `last_four_digits` (and non-sensitive metadata) should be exposed in normal list responses; reveal endpoints decrypt on demand.

## Adding new features

1. Define or extend a port in `ports/in` or `ports/out`.
2. Add domain logic in `domain/service`.
3. Implement outbound port(s) under `infrastructure/adapter/out`.
4. Expose via controllers under `infrastructure/adapter/in/web/controller` and DTOs under `infrastructure/adapter/in/web/dto`.

Keep `domain/model` free of Spring/JPA annotations. Map between domain and infrastructure types in adapters/mappers only.
