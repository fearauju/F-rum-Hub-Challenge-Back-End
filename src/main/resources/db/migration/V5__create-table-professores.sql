CREATE TABLE professores(

    id BIGINT NOT NULL PRIMARY KEY,
    especializacoes VARCHAR(500) NOT NULL,
    titularidade_academica VARCHAR(255) NOT NULL,
    cursos_lecionados VARCHAR(500) DEFAULT NULL,
    total_horas_lecionadas INT DEFAULT 0,
    anos_de_experiencia INT DEFAULT 0,
    data_de_admissao DATE NOT NULL,
    ativo TINYINT NOT NULL,

    FOREIGN KEY(id) REFERENCES usuarios(id)
);