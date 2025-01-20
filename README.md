# ForumHub API

## 📋 Sobre o Projeto
API REST desenvolvida para gerenciar um fórum educacional, permitindo interações entre alunos, equipe de suporte e instrutores através de tópicos de discussão e respostas.

## 🛠️ Tecnologias Utilizadas
- Java 21
- Spring Boot 3.4.0
- Spring Security
- JWT para autenticação
- MySQL
- Flyway para migrations
- Maven
- Swagger/OpenAPI para documentação

## 🔑 Funcionalidades Principais

### 👤 Autenticação e Autorização
- Sistema de login com JWT
- Diferentes níveis de acesso (ESTUDANTE, SUPORTE, PROFESSOR, ADMINISTRADOR)
- Regras de acesso de funcionalidades para cada nível
- Proteção de rotas baseada em roles

### 📚 Gestão de Cursos e Formações
- CRUD completo de cursos
- Gerenciamento de formações -> Cada escola possui uma formação --> Escola de BACK_END --> Formação: JAVA (qualquer semelhança com determinada plataforma são meras semelhanças........ou não 😊)
- Associação de cursos a formações -> Cada formação possui diversos cursos 

### 💬 Fórum
- Criação e gestão de tópicos
- Gestão de respostas
- Marcação de respostas como solução
- Categorização por formacao
- estatísticas de desempenho de resolução de tópicos
- 

### 👥 Usuários
- Gestão de perfis
- Gerenciamento de matrículas dos estudantes
- Tracking de atividades

## 🔒 Segurança
- Autenticação via JWT
- Senhas criptografadas com BCrypt
- Validação de tokens
- Rate limiting (bucket) para proteção contra ataques de força bruta
- Headers de segurança contra XSS
- Validação e sanitização de inputs e outputs

## 📊 Monitoramento
- Logs estruturados
- Métricas de performance da equipe de suporte (estatísticas dos tópicos)



## Acesse a documentação
Swagger UI: http://localhost:8080/swagger-ui/index.html

## ✒️ Autor
[Luiz Felipe]

---

⌨️ com ❤️ por [Luiz Felipe] 😊

## 🚀 Como Executar

1. Pré-requisitos:
   - Java 21
   - MySQL
   - Maven

2. Clone o repositório:

git clone https://github.com/fearauju/F-rum-Hub-Challenge-Back-End.git
cd F-rum-Hub-Challenge-Back-End

3 - ## ⚙️ Configuração

    3.1 Configure o banco de dados no arquivo `application.properties`:
        -  spring.datasource.url=${DATABASE_URL}
        -  spring.datasource.username=${DB_USER}
        -  spring.datasource.password=${DB_PASSWORD}
        -  spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver



4 - Pontos importantes

- O login é feito com o email e senha, armazena a senha em formato Hash usando Bcrypt. Essa parte é feita de forma interna, via terminal usando O
banco de dados MYSQL.

Pode usar esses dados para teste:

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


- Feito isto, o segundo passo pode ser feito no insomnia. Recomendo a seguinte ordem:
1 - Cadastrar perfil de cada usuário (Principalmente o usuário Professor que se relaciona com a formacao e os cursos que leciona), exceto o usuário ESTUDANTE. Este só consegue logar se houver uma matrícula associada e ativa, não expirada.
2 - Cadastrar Escola e sua área de formação -> Escola de Programação, Escola de Front-end, Escola de IA, etc.
3 - Cadastrar Formação -> Java, HTML, CSS, Marchine Learning, etc.
4 - Cadastrar Curso -> cadastrar curso individualmente para determinada formacao. (Opcional -> no cadastro da formação pode atribuir diversos cursos)
5 - Cadastrar Estudante -> pode realizar o cadastro de estudante individualmente. A matrícula será para determinada Area de formação -> BACK_END, FRONT_END, IA, etc.
6 - Inscrição de estudante em cursos -> pode realizar a inscrição em um dos cursos disponíveis da formação usando o Controller de Matrícula.
6 - Cadastrar Tópico -> Somente estudantes podem criar tópicos com suas dúvidas e outros usuários possuem funções específicas como o suporte. Veja o controlador de Tópico.
7 - Cadastrar Resposta -> os demais usuários podem responder aos tópicos, exceto administrador.
8 - Marcar Resposta como Solução -> Somente o estudante pode marcar uma resposta como solução.
9 - Após gerar tópicos e respostas com o suporte principalmente, verifique as estatísticas no controller da classe Tópicos.

Estou mexendo nesse projeto desde dezembro e terminei hoje (20/01/2025), devido alguns bugs que fui corrigindo ao encontrar. Espero não ter deixade algum erro grosseiro,
ainda falta algumas coisas para concluir, mas espero que gostem.



