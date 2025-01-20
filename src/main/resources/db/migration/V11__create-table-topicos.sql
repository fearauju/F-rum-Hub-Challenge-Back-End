CREATE TABLE topicos(
    id BIGINT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(100) NOT NULL,
    mensagem VARCHAR(255) NOT NULL,
    data_criacao DATETIME NOT NULL,
    resolvido BOOLEAN DEFAULT FALSE,
    curso_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,

    PRIMARY KEY(id),
    CONSTRAINT fk_topicos_curso_id FOREIGN KEY(curso_id) REFERENCES cursos(id),
    CONSTRAINT fk_topicos_usuario_id FOREIGN KEY(usuario_id) REFERENCES usuarios(id),
    CONSTRAINT unique_titulo_mensagem UNIQUE(titulo, mensagem)
);

CREATE INDEX idx_topicos_curso_id ON topicos(curso_id);
CREATE INDEX idx_topicos_usuario_id ON topicos(usuario_id);