CREATE TABLE registro_acoes_admin (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    administrador_id BIGINT NOT NULL,
    acoes_executadas TEXT NOT NULL,
    data_execucao TIMESTAMP NOT NULL,
    detalhes TEXT,

    FOREIGN KEY (administrador_id) REFERENCES administradores(id)
);

CREATE INDEX idx_admin_acoes ON registro_acoes_admin(administrador_id);
CREATE INDEX idx_data_execucao ON registro_acoes_admin(data_execucao);