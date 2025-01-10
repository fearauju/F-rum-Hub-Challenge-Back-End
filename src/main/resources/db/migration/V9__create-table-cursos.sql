CREATE TABLE cursos(

    id BIGINT NOT NULL AUTO_INCREMENT,
    curso VARCHAR(255) UNIQUE NOT NULL,
    duracao BIGINT NOT NULL,
    total_de_alunos INTEGER DEFAULT 0,
    avaliacao DOUBLE DEFAULT 0.0,
    formacao_id BIGINT NOT NULL,

    PRIMARY KEY(id),
    CONSTRAINT fk_cursos_formacao_id FOREIGN KEY(formacao_id) REFERENCES formacoes(id)
);