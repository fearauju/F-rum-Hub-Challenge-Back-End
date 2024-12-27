-- Adicionar índice na coluna login
ALTER TABLE usuarios ADD INDEX idx_login (login);

-- Índice composto para buscas frequentes
ALTER TABLE usuarios ADD INDEX idx_login_tipo (login, tipo_usuario);