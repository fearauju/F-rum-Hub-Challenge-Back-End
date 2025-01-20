CREATE TABLE cursos_professores (
    curso_id BIGINT NOT NULL,
    professor_id BIGINT NOT NULL,
    data_inicio DATE NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,

    PRIMARY KEY(curso_id, professor_id),
    FOREIGN KEY(curso_id) REFERENCES cursos(id),
    FOREIGN KEY(professor_id) REFERENCES professores(id)
);

CREATE INDEX idx_cursos_professores ON cursos_professores(curso_id, professor_id);