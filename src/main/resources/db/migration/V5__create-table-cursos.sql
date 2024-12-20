CREATE TABLE cursos(

    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    duracao BIGINT NOT NULL,
    total_alunos BIGINT NOT NULL,
    avaliacao DOUBLE NOT NULL,
    usuario_id BIGINT NOT NULL,
    formacao_id BIGINT NOT NULL,


    PRIMARY KEY(id),
    CONSTRAINT fk_cursos_usuario_id FOREIGN KEY(usuario_id) REFERENCES usuarios(id),
    CONSTRAINT fk_cursos_formacao_id FOREIGN KEY(formacao_id) REFERENCES formacoes(id)
);