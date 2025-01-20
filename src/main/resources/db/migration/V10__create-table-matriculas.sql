CREATE TABLE matriculas(
    id BIGINT NOT NULL AUTO_INCREMENT,
    numero_matricula BIGINT UNIQUE NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    data_assinatura DATETIME  NOT NULL,
    data_expiracao_assinatura DATETIME NOT NULL,
    formacao_id BIGINT NOT NULL,
    estudante_id BIGINT NOT NULL,

    PRIMARY KEY(id),
    CONSTRAINT fk_matriculas_formacao_id FOREIGN  KEY(formacao_id) REFERENCES formacoes(id),
    CONSTRAINT fk_matriculas_estudante_id FOREIGN  KEY(estudante_id) REFERENCES estudantes(id)
);