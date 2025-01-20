CREATE TABLE administradores(
   id BIGINT NOT NULL PRIMARY KEY,
   acoes_executadas VARCHAR(500) NOT NULL,
   data_acao_executada DATETIME NOT NULL,

   FOREIGN KEY(id) REFERENCES usuarios(id)
);