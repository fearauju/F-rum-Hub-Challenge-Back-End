CREATE TABLE usuarios(

    id BIGINT NOT NULL AUTO_INCREMENT,
    login VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    ultimo_login DATETIME DEFAULT NULL,
    falhas_login INT DEFAULT 0,
    data_assinatura DATETIME NOT NULL,
    expiracao_assinatura DATETIME NOT NULL,
    perfil_id BIGINT NOT NULL,

    PRIMARY KEY(id),
    CONSTRAINT fk_usuarios_perfil_id FOREIGN KEY(perfil_id) REFERENCES perfis(id)
);