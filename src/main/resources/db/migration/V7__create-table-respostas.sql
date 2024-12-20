CREATE TABLE respostas(

    id BIGINT NOT NULL AUTO_INCREMENT,
    mensagem VARCHAR(1000) NOT NULL,
    data_criacao DATETIME NOT NULL,
    solucao TINYINT DEFAULT 0,
    usuario_id BIGINT NOT NULL,
    topico_id BIGINT NOT NULL,

    PRIMARY KEY(id),
    CONSTRAINT fk_respostas_usuario_id FOREIGN KEY(usuario_id) REFERENCES usuarios(id),
    CONSTRAINT fk_respostas_topico_id FOREIGN KEY(topico_id) REFERENCES topicos(id)
);