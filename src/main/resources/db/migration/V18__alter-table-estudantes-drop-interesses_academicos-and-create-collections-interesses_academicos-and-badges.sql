-- Alterando a tabela estudantes existente para adicionar novas colunas
ALTER TABLE estudantes
    ADD COLUMN nivel_academico VARCHAR(50) NOT NULL DEFAULT 'GRADUACAO',
    ADD COLUMN cursos_concluidos INT DEFAULT 0,
    ADD COLUMN media_avaliacoes DOUBLE DEFAULT 0.0,
    ADD COLUMN ultima_atividade DATETIME,
    ADD COLUMN certificados_emitidos INT DEFAULT 0;

-- Alterar a coluna interesses_academicos para uma tabela separada
CREATE TABLE estudante_interesses_academicos (
    estudante_id BIGINT NOT NULL,
    interesse VARCHAR(100) NOT NULL,

    PRIMARY KEY(estudante_id, interesse),
    FOREIGN KEY(estudante_id) REFERENCES estudantes(id)
        ON DELETE CASCADE
);

-- Criar tabela para badges
CREATE TABLE estudante_badges (
    estudante_id BIGINT NOT NULL,
    badge VARCHAR(100) NOT NULL,
    data_conquista DATETIME NOT NULL,
    descricao VARCHAR(255),

    PRIMARY KEY(estudante_id, badge),
    FOREIGN KEY(estudante_id) REFERENCES estudantes(id)
        ON DELETE CASCADE
);

-- Adicionando índices para melhorar performance das consultas
CREATE INDEX idx_estudante_interesses ON estudante_interesses_academicos(interesse);
CREATE INDEX idx_estudante_badges ON estudante_badges(badge);