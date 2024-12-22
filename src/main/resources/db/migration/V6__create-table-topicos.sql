CREATE TABLE topicos(

    id BIGINT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(100) UNIQUE NOT NULL,
    mensagem VARCHAR(255) UNIQUE NOT NULL,
    data_criacao DATETIME NOT NULL,
    status TINYINT DEFAULT 1,
    perfil_id BIGINT NOT NULL,
    curso_id BIGINT NOT NULL,

    PRIMARY KEY(id),
    CONSTRAINT fk_topicos_perfil_id FOREIGN KEY(perfil_id) REFERENCES perfis(id),
    CONSTRAINT fk_topicos_curso_id FOREIGN KEY(curso_id) REFERENCES cursos(id)
);