CREATE TABLE perfis(
    nome VARCHAR(100) NOT NULL,
    descricao_pessoal VARCHAR(500) NOT NULL,
    data_nascimento DATE NOT NULL,
    usuario_id BIGINT NOT NULL,

    PRIMARY KEY(usuario_id),
    CONSTRAINT fk_perfis_usuario_id FOREIGN KEY(usuario_id) REFERENCES usuarios(id)
);