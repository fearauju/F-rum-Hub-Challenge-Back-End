CREATE TABLE respostas(
    id BIGINT NOT NULL AUTO_INCREMENT,
    mensagem VARCHAR(1000) NOT NULL,
    data_criacao DATETIME NOT NULL,
    melhor_resposta BOOLEAN DEFAULT FALSE,
    autor_id BIGINT NOT NULL,
    topico_id BIGINT NOT NULL,

    PRIMARY KEY(id),
    CONSTRAINT fk_respostas_autor_id FOREIGN KEY(autor_id) REFERENCES usuarios(id),
    CONSTRAINT fk_respostas_topico_id FOREIGN KEY(topico_id) REFERENCES topicos(id)
);