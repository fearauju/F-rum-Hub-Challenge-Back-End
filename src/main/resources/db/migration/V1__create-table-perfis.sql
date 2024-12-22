CREATE TABLE perfis(

    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    data_nascimento DATE NOT NULL,
    tipo_perfil VARCHAR(50) NOT NULL,

    PRIMARY KEY(id)
);