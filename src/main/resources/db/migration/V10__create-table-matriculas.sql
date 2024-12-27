CREATE TABLE matriculas(

    id BIGINT NOT NULL,
    numero_da_matricula BIGINT UNIQUE NOT NULL,
    data_assinatura DATETIME  NOT NULL,
    data_expiracao_assinatura DATETIME NOT NULL,
    curso_id BIGINT NOT NULL,
    estudante_id BIGINT NOT NULL,

    PRIMARY KEY(id),
    CONSTRAINT fk_matriculas_curso_id FOREIGN  KEY(curso_id) REFERENCES cursos(id),
    CONSTRAINT fk_matriculas_estudante_id FOREIGN  KEY(estudante_id) REFERENCES estudantes(id)
);