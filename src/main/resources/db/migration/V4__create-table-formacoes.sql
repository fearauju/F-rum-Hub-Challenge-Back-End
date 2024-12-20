CREATE TABLE formacoes(

    id BIGINT NOT NULL AUTO_INCREMENT,
    formacao VARCHAR(255) NOT NULL,
    descricao VARCHAR(1000) NOT NULL,
    area_formacao VARCHAR(100) NOT NULL,
    escola_id BIGINT NOT NULL,

    PRIMARY KEY(id),
    CONSTRAINT fk_formacoes_escola_id FOREIGN KEY(escola_id) REFERENCES escolas(id)
);