CREATE TABLE curso_professor (
    curso_id BIGINT NOT NULL,
    professor_id BIGINT NOT NULL,
    PRIMARY KEY (curso_id, professor_id),
    CONSTRAINT cursos_professor_curso_id FOREIGN KEY (curso_id) REFERENCES cursos(id) ON DELETE CASCADE,
    CONSTRAINT cursos_professor_professor_id FOREIGN KEY (professor_id) REFERENCES professores(id) ON DELETE CASCADE
);
