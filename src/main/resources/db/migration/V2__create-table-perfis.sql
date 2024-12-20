CREATE TABLE perfis(

    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    usuario_id BIGINT NOT NULL,

    PRIMARY KEY(id),
    CONSTRAINT fk_perfis_usuario_id FOREIGN KEY(usuario_id) REFERENCES usuarios(id)
);