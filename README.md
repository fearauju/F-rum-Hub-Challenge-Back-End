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

### 🔑 Dados para Teste

Para facilitar os testes, disponibilizamos um arquivo SQL com dados iniciais:
    - Arquivo:  [`script/dados_iniciais.sql`](script/dados_iniciais.sql)
    - Contém: usuários de teste (estudantes, professores, suporte e admin)

> **Nota**: Todos os usuários de teste utilizam a senha padrão criptografada com BCrypt: `Sq@5687#21`



- Feito isto, o segundo passo pode ser feito no insomnia. Recomendo a seguinte ordem:


### 📝 Passos para Configuração no Insomnia

1. **Cadastro de Perfis**
   - Cadastrar perfil para cada usuário
   - Obrigatório para Professores (relacionamento com formação e cursos)
   - Opcional para Estudantes (requer matrícula ativa)

2. **Configuração de Escolas**
   - Cadastrar escolas e áreas de formação
   - Exemplos: Escola de Programação, Front-end, IA

3. **Cadastro de Formações**
   - Cadastrar formações específicas
   - Exemplos: Java, HTML, CSS, Machine Learning

4. **Gestão de Cursos**
   - Cadastrar cursos individualmente por formação
   - Opcional: atribuir múltiplos cursos no cadastro da formação

5. **Cadastro de Estudantes**
   - Cadastro individual de estudantes
   - Matrícula vinculada à área de formação (BACK_END, FRONT_END, IA)

6. **Gestão de Matrículas**
   - Inscrição em cursos disponíveis da formação
   - Usar Controller de Matrícula

7. **Criação de Tópicos**
   - Exclusivo para estudantes
   - Outros usuários têm funções específicas (ex: suporte)

8. **Gestão de Respostas**
   - Todos os usuários podem responder (exceto administrador)
   - Verificar permissões no Controller de Tópico

9. **Finalização de Tópicos**
   - Marcação de melhor resposta (exclusivo para autor do tópico)
   - Verificar estatísticas no Controller de Tópicos

Estou mexendo nesse projeto desde dezembro e terminei hoje (20/01/2025), devido alguns bugs que fui corrigindo ao encontrar. Espero não ter deixade algum erro grosseiro,
ainda falta algumas coisas para concluir, mas espero que gostem.



