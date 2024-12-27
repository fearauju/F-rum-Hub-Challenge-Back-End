CREATE TABLE suportes(

   id BIGINT NOT NULL PRIMARY KEY,
   especializacoes VARCHAR(500) NOT NULL,
   turno_de_trabalho VARCHAR(20) NOT NULL,
   casos_resolvidos INT DEFAULT 0,
   avaliacao_atendimento DOUBLE DEFAULT 0.0,
   data_admissao DATE NOT NULL,
   ativo TINYINT NOT NULL,

   FOREIGN KEY(id) REFERENCES usuarios(id)
);