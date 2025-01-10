CREATE TABLE estudantes(

    id BIGINT NOT NULL PRIMARY KEY,
    ano_ingresso INT NOT NULL,
    cursos_inscrito VARCHAR (150),
    interesses_academicos VARCHAR(150),
    Pontuacao DECIMAL(10,2) DEFAULT 0.00,
    carga_horaria_concluida INT DEFAULT 0,

    FOREIGN KEY(id) REFERENCES usuarios(id)
);