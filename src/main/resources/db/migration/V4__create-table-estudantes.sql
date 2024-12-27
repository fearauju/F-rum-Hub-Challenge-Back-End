CREATE TABLE estudantes(

    id BIGINT NOT NULL PRIMARY KEY,
    ano_de_ingresso INT NOT NULL,
    interesses_academicos VARCHAR(100),
    Pontuacao INT DEFAULT 0,
    status_da_matricula TINYINT DEFAULT TRUE,
    carga_horaria_concluida INT DEFAULT 0,

    FOREIGN KEY(id) REFERENCES usuarios(id)
);