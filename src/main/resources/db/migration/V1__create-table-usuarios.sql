CREATE TABLE usuarios(

    id BIGINT NOT NULL AUTO_INCREMENT,
    login VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    tipo_usuario VARCHAR(50) NOT NULL,
    ultimo_login DATETIME DEFAULT NULL,
    tentativas_login INT DEFAULT 0,
    conta_bloqueada BOOLEAN DEFAULT FALSE,
    ativo TINYINT DEFAULT TRUE,
    version BIGINT DEFAULT 0 NOT NULL,

    PRIMARY KEY(id)
);