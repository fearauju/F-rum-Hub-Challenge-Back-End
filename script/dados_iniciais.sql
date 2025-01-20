Inserções nas tabelas usuário para criar o login  --> limpar todos os dados e reiniciar o id : TRUNCATE TABLE usuarios;

INSERT INTO usuarios (login, senha, tipo_usuario, ativo, bloqueado_permanente, ultimo_login, version) VALUES
-- Estudantes
('joao.silva@hub.com', '$2a$12$kU4mewXQutAtiduL.untM.CH7xZBxR0lGNHX05Hk11OZjhNb4Zo3e', 'ESTUDANTE', true, false, '2024-01-18 10:30:00', 0),
('maria.santos@hub.com', '$2a$12$kU4mewXQutAtiduL.untM.CH7xZBxR0lGNHX05Hk11OZjhNb4Zo3e', 'ESTUDANTE', true, false, '2024-01-18 11:00:00', 0),
('pedro.oliveira@hub.com', '$2a$12$kU4mewXQutAtiduL.untM.CH7xZBxR0lGNHX05Hk11OZjhNb4Zo3e', 'ESTUDANTE', true, false, '2024-01-18 09:15:00', 0),

-- Professores
('carlos.professor@hub.com', '$2a$12$kU4mewXQutAtiduL.untM.CH7xZBxR0lGNHX05Hk11OZjhNb4Zo3e', 'PROFESSOR', true, false, '2024-01-18 08:00:00', 0),
('ana.professora@ghub.com',  '$2a$12$kU4mewXQutAtiduL.untM.CH7xZBxR0lGNHX05Hk11OZjhNb4Zo3e', 'PROFESSOR', true, false, '2024-01-18 08:30:00', 0),
('Paula.professora@ghub.com',  '$2a$12$kU4mewXQutAtiduL.untM.CH7xZBxR0lGNHX05Hk11OZjhNb4Zo3e', 'PROFESSOR', true, false, '2024-01-18 08:30:00', 0),
('Victor.professora@ghub.com',  '$2a$12$kU4mewXQutAtiduL.untM.CH7xZBxR0lGNHX05Hk11OZjhNb4Zo3e', 'PROFESSOR', true, false, '2024-01-18 08:30:00', 0),

-- Suporte
('paulo.suporte@hub.com', '$2a$12$kU4mewXQutAtiduL.untM.CH7xZBxR0lGNHX05Hk11OZjhNb4Zo3e', 'SUPORTE', true, false, '2024-01-18 09:00:00', 0),
('lucia.suporte@hub.com', '$2a$12$kU4mewXQutAtiduL.untM.CH7xZBxR0lGNHX05Hk11OZjhNb4Zo3e', 'SUPORTE', true, false, '2024-01-18 09:30:00', 0),
('Yamin.suporte@hub.com', '$2a$12$kU4mewXQutAtiduL.untM.CH7xZBxR0lGNHX05Hk11OZjhNb4Zo3e', 'SUPORTE', true, false, '2024-01-18 09:30:00', 0),

-- Administrador
('admin.sistema@hub.com', '$2a$12$kU4mewXQutAtiduL.untM.CH7xZBxR0lGNHX05Hk11OZjhNb4Zo3e', 'ADMINISTRADOR', true, false, '2024-01-18 07:00:00', 0);

-- Inserir administrador usando o ID do usuário recém-criado
INSERT INTO administradores (id) 
SELECT id FROM usuarios WHERE login = 'admin.sistema@hub.com';