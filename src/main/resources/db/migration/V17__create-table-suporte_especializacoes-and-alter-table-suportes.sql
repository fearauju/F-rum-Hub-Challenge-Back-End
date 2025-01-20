-- Primeiro, criar a nova tabela para especializações
CREATE TABLE suporte_especializacoes(
    suporte_id BIGINT NOT NULL,
    especializacao VARCHAR(100) NOT NULL,

    PRIMARY KEY(suporte_id, especializacao),
    CONSTRAINT fk_especializacoes_suporte_id
        FOREIGN KEY(suporte_id)
        REFERENCES suportes(id)
        ON DELETE CASCADE
);

-- Migrar dados existentes de especializações (versão MySQL)
INSERT INTO suporte_especializacoes (suporte_id, especializacao)
SELECT
    id,
    TRIM(SUBSTRING_INDEX(SUBSTRING_INDEX(especializacoes, ',', numbers.n), ',', -1)) especializacao
FROM
    (SELECT 1 n UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4) numbers
    INNER JOIN suportes
    ON CHAR_LENGTH(especializacoes) - CHAR_LENGTH(REPLACE(especializacoes, ',', '')) >= numbers.n - 1
WHERE
    especializacoes IS NOT NULL;

-- Modificar a tabela suportes
ALTER TABLE suportes
    -- Remover coluna antiga de especializações
    DROP COLUMN especializacoes,

    -- Renomear e ajustar coluna de turno
    RENAME COLUMN turno_de_trabalho TO turno_trabalho;

-- Adicionar constraints
ALTER TABLE suportes
    ADD CONSTRAINT chk_turno_trabalho
        CHECK (turno_trabalho IN ('MANHA', 'TARDE', 'NOITE')),
    ADD CONSTRAINT chk_avaliacao_range
        CHECK (avaliacao_suporte >= 0.0 AND avaliacao_suporte <= 5.0);

-- Criar índices
CREATE INDEX idx_suporte_avaliacao ON suportes(avaliacao_suporte);
CREATE INDEX idx_suporte_turno ON suportes(turno_trabalho);