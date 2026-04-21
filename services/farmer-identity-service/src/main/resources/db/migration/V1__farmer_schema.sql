-- Enable UUID generation
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ─────────────────────────────────────────
-- FARMERS
-- ─────────────────────────────────────────
CREATE TABLE farmers (
    id               UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    aadhaar_number   VARCHAR(12)  NOT NULL UNIQUE,
    full_name        VARCHAR(255) NOT NULL,
    mobile_number    VARCHAR(10)  NOT NULL,
    date_of_birth    DATE,
    gender           VARCHAR(10),
    district         VARCHAR(100) NOT NULL,
    mandal           VARCHAR(100),
    village          VARCHAR(100),
    kyc_status       VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    created_at       TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_farmers_aadhaar ON farmers(aadhaar_number);
CREATE INDEX idx_farmers_district ON farmers(district);

-- ─────────────────────────────────────────
-- LAND PROFILES
-- ─────────────────────────────────────────
CREATE TABLE land_profiles (
    id               UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    farmer_id        UUID         NOT NULL REFERENCES farmers(id) ON DELETE CASCADE,
    survey_number    VARCHAR(50)  NOT NULL,
    area_acres       DECIMAL(10,2) NOT NULL,
    soil_type        VARCHAR(50),
    irrigation_type  VARCHAR(50),
    previous_crop    VARCHAR(100),
    is_active        BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at       TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_land_profiles_farmer ON land_profiles(farmer_id);

-- ─────────────────────────────────────────
-- AADHAAR VERIFICATIONS
-- ─────────────────────────────────────────
CREATE TABLE aadhaar_verifications (
    id               UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    farmer_id        UUID         NOT NULL REFERENCES farmers(id) ON DELETE CASCADE,
    aadhaar_number   VARCHAR(12)  NOT NULL,
    transaction_id   VARCHAR(100),
    otp_sent_at      TIMESTAMP,
    verified_at      TIMESTAMP,
    status           VARCHAR(20)  NOT NULL DEFAULT 'INITIATED',
    failure_reason   VARCHAR(255),
    created_at       TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_aadhaar_verif_farmer ON aadhaar_verifications(farmer_id);

-- ─────────────────────────────────────────
-- LEASE RELATIONSHIPS
-- ─────────────────────────────────────────
CREATE TABLE lease_relationships (
    id               UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    owner_farmer_id  UUID         NOT NULL REFERENCES farmers(id),
    tenant_farmer_id UUID         NOT NULL REFERENCES farmers(id),
    land_profile_id  UUID         NOT NULL REFERENCES land_profiles(id),
    start_date       DATE         NOT NULL,
    end_date         DATE,
    status           VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    created_at       TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMP    NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_different_farmers CHECK (owner_farmer_id != tenant_farmer_id)
);

CREATE INDEX idx_lease_owner   ON lease_relationships(owner_farmer_id);
CREATE INDEX idx_lease_tenant  ON lease_relationships(tenant_farmer_id);