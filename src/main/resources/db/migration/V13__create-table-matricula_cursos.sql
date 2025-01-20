CREATE TABLE matricula_cursos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    data_inscricao DATETIME NOT NULL,
    data_conclusao DATETIME,
    concluido BOOLEAN DEFAULT FALSE,
    matricula_id BIGINT NOT NULL,
    curso_id BIGINT NOT NULL,

    PRIMARY KEY(id),
    CONSTRAINT fk_matricula_cursos_matriculas FOREIGN KEY (matricula_id) REFERENCES matriculas(id),
    CONSTRAINT fk_matricula_cursos_cursos FOREIGN KEY (curso_id) REFERENCES cursos(id)
);