CREATE TABLE usuarios (
    id BIGINT NOT NULL AUTO_INCREMENT,
    login VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    tipo_usuario VARCHAR(50) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    conta_bloqueada BOOLEAN DEFAULT FALSE,
    tentativas_login INT DEFAULT 0,
    ultimo_login DATETIME,
    version BIGINT DEFAULT 0,
    PRIMARY KEY(id)
);