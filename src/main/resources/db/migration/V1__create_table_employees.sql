CREATE TABLE employees (
    id BIGSERIAL NOT NULL,

    name VARCHAR(150) NOT NULL,
    employee_type VARCHAR(50) NOT NULL,
    cpf VARCHAR(11) NOT NULL,
    password VARCHAR(255) NOT NULL,

    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT pk_employees PRIMARY KEY (id),
    CONSTRAINT uk_employees_cpf UNIQUE (cpf)
);