CREATE TABLE patients (
    id BIGSERIAL PRIMARY KEY,

    -- Dados Pessoais
    name VARCHAR(150) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,

    -- Contato de Emergência
    emergency_contact_name VARCHAR(150),
    emergency_contact_phone VARCHAR(20),

    -- Endereço
    zip_code VARCHAR(9) NOT NULL,
    street VARCHAR(155) NOT NULL,
    number VARCHAR(20),
    complement VARCHAR(100),
    neighborhood VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(2) NOT NULL,

    -- Auditoria
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_patients_name ON patients(name);
CREATE INDEX idx_patients_cpf ON patients(cpf);