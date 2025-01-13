CREATE TABLE suportes(

   id BIGINT NOT NULL PRIMARY KEY,
   especializacoes VARCHAR(250) NOT NULL,
   turno_de_trabalho VARCHAR(20) NOT NULL,
   casos_resolvidos INT DEFAULT 0,
   avaliacao_suporte DOUBLE DEFAULT 0.0,
   motivo_avaliacao VARCHAR(250),
   data_admissao DATE NOT NULL,

   FOREIGN KEY(id) REFERENCES usuarios(id)
);