CREATE TABLE topicos(

    id BIGINT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(100) UNIQUE NOT NULL,
    mensagem VARCHAR(255) UNIQUE NOT NULL,
    data_criacao DATETIME NOT NULL,
    resolvido TINYINT DEFAULT 1,
    curso_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,

    PRIMARY KEY(id),
    CONSTRAINT fk_topicos_curso_id FOREIGN KEY(curso_id) REFERENCES cursos(id),
    CONSTRAINT fk_topicos_usuario_id FOREIGN KEY(usuario_id) REFERENCES usuarios(id)
);