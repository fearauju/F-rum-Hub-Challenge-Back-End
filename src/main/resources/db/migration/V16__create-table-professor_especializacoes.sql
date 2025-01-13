CREATE TABLE professor_especializacoes (
    professor_id BIGINT NOT NULL,
    especializacao VARCHAR(255) NOT NULL,

    CONSTRAINT fk_professor_especializacoes FOREIGN KEY (professor_id)
        REFERENCES professores(id) ON DELETE CASCADE
);

CREATE INDEX idx_professor_especializacoes ON professor_especializacoes(professor_id);