package hub.forum.api.domain.usuario;

public enum TipoUsuario {

    ADMINISTRADOR{

        @Override
        public boolean podeCadastrarUsuarios() {
            return true;
        }

        @Override
        public boolean podeAtualizarDadosUsuario() {
            return true;
        } // apenas da própria conta

        @Override
        public boolean podeInativarUsuarios() {
            return true;
        }

        @Override
        public boolean podeDesbloquearUsuarios() {
            return true;
        }

        @Override
        public boolean podeRenovarAssinatura() {
            return true; //apenas para ter a função como exemplo, não terá renovação por meio de pagamento
        }

        @Override
        public boolean podePesquisarPorUsuarios() {
            return true;
        }

        @Override
        public boolean podeCriarPerfil() {
            return false;
        }

        @Override
        public boolean podeAtualizarPerfil() {
            return false;
        }

        @Override
        public boolean podePesquisarPerfil() {
            return true;
        }

        @Override
        public boolean podeAcessarCursos() {
            return false; // assistir as aulas
        }

        @Override
        public boolean podeCadastrarCursos() {
            return true;
        }

        @Override
        public boolean podeAtualizarCursos() {
            return false;
        }

        @Override
        public boolean podeInscreverCursos() {
            return false;
        }

        @Override
        public boolean podeCadastrarFormacoes() {
            return true;
        }

        @Override
        public boolean podeAtualizarFormacoes() {
            return true;
        }


        @Override
        public boolean podeCriarTopicos() {
            return false;
        }

        @Override
        public boolean podeAtualizarTopicos() {
            return false;
        }

        @Override
        public boolean podeApagarTopicos() {
            return true; // redudante ser por exclusão lógica, será deletado de forma permanente.
        }

        @Override
        public boolean podeFecharTopicos() {
            return true; // Formas de fechar os tópicos, suporte e adm ou estudante marcar como melhor resposta.
            // Apenas os tópicos que o estudante escolhe a melhor resposta são fechados automaticamente.
        }


        @Override
        public boolean podeResponderNoForum() {
            return false;
        }

        @Override
        public boolean podeAtualizarResposta() {
            return false;
        }

        @Override
        public boolean podeEscolherMelhorResposta() {
            return false;
        }

        @Override
        public boolean podeCadastrarEscola() {
            return true;
        }

        @Override
        public boolean podeAtualizarEscola() {
            return true;
        }
    },


    ESTUDANTE{

        @Override
        public boolean podeCadastrarUsuarios() {
            return false;
        }

        @Override
        public boolean podeAtualizarDadosUsuario() {
            return true; // apenas os seus próprios dados
        }

        @Override
        public boolean podeInativarUsuarios() {
            return false;
        }

        @Override
        public boolean podeDesbloquearUsuarios() {
            return false;
        }

        @Override
        public boolean podeRenovarAssinatura() {
            return false; // Como não há pagamento, será feito de forma interna, apenas como exemplo.
        }

        @Override
        public boolean podePesquisarPorUsuarios() {
            return false;
        }

        @Override
        public boolean podeCriarPerfil() {
            return true;
        }

        @Override
        public boolean podeAtualizarPerfil() {
            return true;
        }

        @Override
        public boolean podePesquisarPerfil() {
            return true;
        }


        @Override
        public boolean podeAcessarCursos() {
            return true;
        }

        @Override
        public boolean podeCadastrarCursos() {
            return false;
        }

        @Override
        public boolean podeAtualizarCursos() {
            return false;
        }

        @Override
        public boolean podeInscreverCursos() {
            return true;
        }

        @Override
        public boolean podeCadastrarFormacoes() {
            return false;
        }

        @Override
        public boolean podeAtualizarFormacoes() {
            return false;
        }

        @Override
        public boolean podeCriarTopicos() {
            return true;
        }

        @Override
        public boolean podeAtualizarTopicos() {
            return true; // apenas os seus próprios tópicos, se ainda não foram marcados como solucionados.
        }

        @Override
        public boolean podeApagarTopicos() {
            return false;
        }

        @Override
        public boolean podeFecharTopicos() {
            return false; // Uma vez solucionado, não deveria permitir atualização, e sim abrir outro tópico.
        }


        @Override
        public boolean podeResponderNoForum() {
            return true;
        }

        @Override
        public boolean podeAtualizarResposta() {
            return true; // apenas as suas próprias respostas
        }

        @Override
        public boolean podeEscolherMelhorResposta() {
            return true;
        }


        @Override
        public boolean podeCadastrarEscola() {
            return false;
        }

        @Override
        public boolean podeAtualizarEscola() {
            return false;
        }
    },


    PROFESSOR{

        @Override
        public boolean podeCadastrarUsuarios() {
            return false;
        }

        @Override
        public boolean podeAtualizarDadosUsuario() {
            return true; // Os seus prórpios dados
        }

        @Override
        public boolean podeInativarUsuarios() {
            return false;
        }

        @Override
        public boolean podeDesbloquearUsuarios() {
            return false;
        }

        @Override
        public boolean podeRenovarAssinatura() {
            return false;
        }

        @Override
        public boolean podePesquisarPorUsuarios() {
            return false;
        }

        @Override
        public boolean podeCriarPerfil() {
            return true;
        }

        @Override
        public boolean podeAtualizarPerfil() {
            return true;
        }

        @Override
        public boolean podePesquisarPerfil() {
            return true;
        }


        @Override
        public boolean podeAcessarCursos() {
            return false;
        }

        @Override
        public boolean podeCadastrarCursos() {
            return false;
        }

        @Override
        public boolean podeAtualizarCursos() {
            return true; // Apenas cursos que leciona
        }

        @Override
        public boolean podeInscreverCursos() {
            return false;
        }

        @Override
        public boolean podeCadastrarFormacoes() {
            return false;
        }

        @Override
        public boolean podeAtualizarFormacoes() {
            return false;
        }


        @Override
        public boolean podeCriarTopicos() {
            return false;
        }

        @Override
        public boolean podeAtualizarTopicos() {
            return false;
        }

        @Override
        public boolean podeApagarTopicos() {
            return false;
        }

        @Override
        public boolean podeFecharTopicos() {
            return false;
        }


        @Override
        public boolean podeResponderNoForum() {
            return true;
        }

        @Override
        public boolean podeAtualizarResposta() {
            return true; // As próprias respostas
        }

        @Override
        public boolean podeEscolherMelhorResposta() {
            return false;
        }

        @Override
        public boolean podeCadastrarEscola() {
            return false;
        }

        @Override
        public boolean podeAtualizarEscola() {
            return false;
        }
    },


    SUPORTE{

        @Override
        public boolean podeCadastrarUsuarios() {
            return false;
        }

        @Override
        public boolean podeAtualizarDadosUsuario() {
            return true;
        }

        @Override
        public boolean podeInativarUsuarios() {
            return false;
        }

        @Override
        public boolean podeDesbloquearUsuarios() {
            return true;
        }

        @Override
        public boolean podeRenovarAssinatura() {
            return true;
        }

        @Override
        public boolean podePesquisarPorUsuarios() {
            return false;
        }

        @Override
        public boolean podeCriarPerfil() {
            return true;
        }

        @Override
        public boolean podeAtualizarPerfil() {
            return true;
        }

        @Override
        public boolean podePesquisarPerfil() {
            return true;
        }

        @Override
        public boolean podeAcessarCursos() {
            return false;
        }

        @Override
        public boolean podeCadastrarCursos() {
            return false;
        }

        @Override
        public boolean podeAtualizarCursos() {
            return false;
        }

        @Override
        public boolean podeInscreverCursos() {
            return false;
        }

        @Override
        public boolean podeCadastrarFormacoes() {
            return false;
        }

        @Override
        public boolean podeAtualizarFormacoes() {
            return false;
        }

        @Override
        public boolean podeCriarTopicos() {
            return false;
        }

        @Override
        public boolean podeAtualizarTopicos() {
            return false;
        }

        @Override
        public boolean podeApagarTopicos() {
            return false;
        }

        @Override
        public boolean podeFecharTopicos() {
            return true;
        }

        @Override
        public boolean podeResponderNoForum() {
            return true;
        }

        @Override
        public boolean podeAtualizarResposta() {
            return true; // As próprias respostas
        }

        @Override
        public boolean podeEscolherMelhorResposta() {
            return false;
        }

        @Override
        public boolean podeCadastrarEscola() {
            return false;
        }

        @Override
        public boolean podeAtualizarEscola() {
            return false;
        }
    };



    public abstract boolean podeCadastrarUsuarios();
    public abstract boolean podeAtualizarDadosUsuario(); // o próprio usuário
    public abstract boolean podeInativarUsuarios();// professores e suporte --> exclusão lógica
    public abstract boolean podeDesbloquearUsuarios(); // adm e suporte
    public abstract boolean podeRenovarAssinatura(); // Apenas exemplo, era para ser feito por meio de pagamento
    public abstract boolean podePesquisarPorUsuarios();

    public abstract boolean podeCriarPerfil();
    public abstract boolean podeAtualizarPerfil();
    public abstract boolean podePesquisarPerfil();

    public abstract boolean podeAcessarCursos();
    public abstract boolean podeCadastrarCursos();
    public abstract boolean podeAtualizarCursos();
    public abstract boolean podeInscreverCursos();


    public abstract boolean podeCadastrarFormacoes();
    public abstract boolean podeAtualizarFormacoes();


    public abstract boolean podeCriarTopicos();
    public abstract boolean podeAtualizarTopicos();
    public abstract boolean podeApagarTopicos();
    public abstract boolean podeFecharTopicos();


    public abstract boolean podeResponderNoForum();
    public abstract boolean podeAtualizarResposta();
    public abstract boolean podeEscolherMelhorResposta();


    public abstract boolean podeCadastrarEscola();
    public abstract boolean podeAtualizarEscola();
}

