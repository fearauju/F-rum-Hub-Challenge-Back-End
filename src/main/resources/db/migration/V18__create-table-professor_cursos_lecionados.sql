CREATE TABLE professor_cursos_lecionados (
    professor_id BIGINT NOT NULL,
    curso VARCHAR(255) NOT NULL,
    PRIMARY KEY (professor_id, curso),
    FOREIGN KEY (professor_id) REFERENCES professores(id)
);