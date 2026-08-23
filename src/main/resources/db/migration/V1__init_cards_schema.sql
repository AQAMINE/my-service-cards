-- ====================================================================
-- 1. TABLE BANKS
-- ====================================================================
CREATE TABLE banks (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID DEFAULT NULL,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(30) UNIQUE,
    website_url VARCHAR(255),
    primary_color VARCHAR(20) DEFAULT '#00ABE4',
    logo_url TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_banks_user_id ON banks(user_id);

-- ====================================================================
-- 2. TABLE CARD PROVIDERS
-- ====================================================================
CREATE TABLE card_providers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID DEFAULT NULL,
    name VARCHAR(50) NOT NULL,
    code VARCHAR(30) UNIQUE,
    logo_url TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_card_providers_user_id ON card_providers(user_id);

-- ====================================================================
-- 3. TABLE BANK CARDS
-- ====================================================================
CREATE TABLE bank_cards (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    bank_id UUID NOT NULL REFERENCES banks(id),
    provider_id UUID NOT NULL REFERENCES card_providers(id),
    card_holder_name VARCHAR(100) NOT NULL,
    card_name VARCHAR(100) NOT NULL,
    last_four_digits VARCHAR(4) NOT NULL,
    expiry_month INT NOT NULL,
    expiry_year INT NOT NULL,
    card_color VARCHAR(20) DEFAULT '#00ABE4',
    encrypted_pan TEXT NOT NULL,
    pan_iv VARCHAR(255) NOT NULL,
    encrypted_cvv TEXT NOT NULL,
    cvv_iv VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_bank_cards_user_id ON bank_cards(user_id);
CREATE INDEX idx_bank_cards_user_active ON bank_cards(user_id, is_active);

-- ====================================================================
-- 4. INSERTION DES CARD PROVIDERS SYSTÈME
-- ====================================================================
INSERT INTO card_providers (id, user_id, name, code, logo_url) VALUES
(gen_random_uuid(), NULL, 'Visa', 'VISA', 'https://cdn.simpleicons.org/visa/1A1F71'),
(gen_random_uuid(), NULL, 'Mastercard', 'MASTERCARD', 'https://cdn.simpleicons.org/mastercard/EB001B'),
(gen_random_uuid(), NULL, 'American Express', 'AMEX', 'https://cdn.simpleicons.org/americanexpress/006FCF'),
(gen_random_uuid(), NULL, 'Discover', 'DISCOVER', 'https://cdn.simpleicons.org/discover/FF6000'),
(gen_random_uuid(), NULL, 'JCB', 'JCB', 'https://cdn.simpleicons.org/jcb/000000');

-- ====================================================================
-- 5. INSERTION DES BANQUES SYSTÈME
-- ====================================================================
INSERT INTO banks (id, user_id, name, code, website_url, primary_color, logo_url) VALUES
(gen_random_uuid(), NULL, 'Attijariwafa Bank', 'ATTIJARIWAFA', 'https://www.attijariwafabank.com', '#E67E22', 'https://cdn.simpleicons.org/building/E67E22'),
(gen_random_uuid(), NULL, 'Banque Populaire', 'BANQUE_POPULAIRE', 'https://www.chaabibank.ma', '#C0392B', 'https://cdn.simpleicons.org/building/C0392B'),
(gen_random_uuid(), NULL, 'BMCE Bank / Bank of Africa', 'BMCE', 'https://www.bankofafrica.ma', '#2980B9', 'https://cdn.simpleicons.org/building/2980B9'),
(gen_random_uuid(), NULL, 'CIH Bank', 'CIH', 'https://www.cihbank.ma', '#27AE60', 'https://cdn.simpleicons.org/building/27AE60'),
(gen_random_uuid(), NULL, 'Société Générale', 'SOCIETE_GENERALE', 'https://www.societegenerale.ma', '#E74C3C', 'https://cdn.simpleicons.org/societegenerale/E74C3C'),
(gen_random_uuid(), NULL, 'BMCI', 'BMCI', 'https://www.bmci.ma', '#16A085', 'https://cdn.simpleicons.org/building/16A085'),
(gen_random_uuid(), NULL, 'Crédit du Maroc', 'CDM', 'https://www.creditdumaroc.ma', '#1ABC9C', 'https://cdn.simpleicons.org/building/1ABC9C'),
(gen_random_uuid(), NULL, 'Revolut', 'REVOLUT', 'https://www.revolut.com', '#0075FF', 'https://cdn.simpleicons.org/revolut/0075FF'),
(gen_random_uuid(), NULL, 'Wise', 'WISE', 'https://wise.com', '#9FE870', 'https://cdn.simpleicons.org/wise/9FE870'),
(gen_random_uuid(), NULL, 'N26', 'N26', 'https://n26.com', '#36A18B', 'https://cdn.simpleicons.org/n26/36A18B'),
(gen_random_uuid(), NULL, 'PayPal', 'PAYPAL', 'https://www.paypal.com', '#003087', 'https://cdn.simpleicons.org/paypal/003087'),
(gen_random_uuid(), NULL, 'Payoneer', 'PAYONEER', 'https://www.payoneer.com', '#FF4800', 'https://cdn.simpleicons.org/payoneer/FF4800'),
(gen_random_uuid(), NULL, 'Chase', 'CHASE', 'https://www.chase.com', '#117ACA', 'https://cdn.simpleicons.org/chase/117ACA');