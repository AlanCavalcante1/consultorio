CREATE SEQUENCE employees_seq
    START WITH 1
    INCREMENT BY 50;

CREATE TABLE employees (
    id BIGINT NOT NULL,
    name VARCHAR(150) NOT NULL,
    employee_type VARCHAR(50) NOT NULL,
    cpf VARCHAR(11) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,

    CONSTRAINT pk_employees PRIMARY KEY (id),
    CONSTRAINT uk_employees_cpf UNIQUE (cpf)
);
