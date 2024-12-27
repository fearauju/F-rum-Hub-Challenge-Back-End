package hub.forum.api.domain.usuario;

public enum TipoUsuario {

    ADMINISTRADOR{

        @Override
        public boolean podeCadastrarUsuarios() {
            return true;
        }

        @Override
        public boolean podeAtualizarDadosDoUsuario() {
            return false;
        }

        @Override
        public boolean podeDeletarUsuarios() {
            return true;
        }

        @Override
        public boolean podeDesbloquearUsuarios() {
            return true;
        }

        @Override
        public boolean renovarAssinatura() {
            return true; //apenas para ter a função como exemplo, não terá renovação por meio de pagamento
        }

        @Override
        public boolean podePesquisarPorDadosDeUsuarios() {
            return true;
        }

        @Override
        public boolean podePesquisarPorDadosDePerfil() {
            return true;
        }

        @Override
        public boolean temAcessoACursos() {
            return false; // assistir as aulas
        }

        @Override
        public boolean podeCadastrarCursos() {
            return true;
        }

        @Override
        public boolean podeAtualizarCursos() {
            return true;
        }

        @Override
        public boolean CadastrarFormacoes() {
            return true;
        }

        @Override
        public boolean AtualizarFormacoes() {
            return true;
        }

        @Override
        public boolean PesquisarPorformacoes() {
            return true;
        }

        @Override
        public boolean podeCriarTopicos() {
            return false;
        }

        @Override
        public boolean atualizarTopicos() {
            return false;
        }

        @Override
        public boolean apagarTopicos() {
            return true; // redudante ser por exclusão lógica, será deletado de forma permanente.
        }

        @Override
        public boolean fecharTopicos() {
            return false; // Formas de fechar os tópicos, suporte e adm ou estudante marcar como melhor resposta.
            // Apenas o tópicos que são marcados como solucionados é fechado automaticamente.
        }

        @Override
        public boolean PesquisarPorTopicos() {
            return true;
        }

        @Override
        public boolean podeCriarRespostas() {
            return false;
        }

        @Override
        public boolean atualizarRespostas() {
            return false;
        }

        @Override
        public boolean cadastrarEscola() {
            return true;
        }

        @Override
        public boolean AtualizarEscola() {
            return true;
        }

        @Override
        public boolean PesquisarPorEscolas() {
            return true;
        }
    },


    ESTUDANTE{

        @Override
        public boolean podeCadastrarUsuarios() {
            return false;
        }

        @Override
        public boolean podeAtualizarDadosDoUsuario() {
            return true; // apenas os seus próprios dados
        }

        @Override
        public boolean podeDeletarUsuarios() {
            return false;
        }

        @Override
        public boolean podeDesbloquearUsuarios() {
            return false;
        }

        @Override
        public boolean renovarAssinatura() {
            return false; // Como não há pagamento, será feito de forma interna, apenas como exemplo.
        }

        @Override
        public boolean podePesquisarPorDadosDeUsuarios() {
            return false;
        }

        @Override
        public boolean podePesquisarPorDadosDePerfil() {
            return true;
        }

        @Override
        public boolean temAcessoACursos() {
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
        public boolean CadastrarFormacoes() {
            return false;
        }

        @Override
        public boolean AtualizarFormacoes() {
            return false;
        }

        @Override
        public boolean PesquisarPorformacoes() {
            return true;
        }

        @Override
        public boolean podeCriarTopicos() {
            return true;
        }

        @Override
        public boolean atualizarTopicos() {
            return true; // apenas os seus próprios tópicos, se ainda não foram marcados como solucionados.
        }

        @Override
        public boolean apagarTopicos() {
            return false;
        }

        @Override
        public boolean fecharTopicos() {
            return false; // Uma vez solucionado, não deveria permitir atualização, e sim abrir outro tópico.
        }

        @Override
        public boolean PesquisarPorTopicos() {
            return true; // assuntos
        }

        @Override
        public boolean podeCriarRespostas() {
            return true;
        }

        @Override
        public boolean atualizarRespostas() {
            return true; // apenas as suas próprias respostas
        }


        @Override
        public boolean cadastrarEscola() {
            return false;
        }

        @Override
        public boolean AtualizarEscola() {
            return false;
        }

        @Override
        public boolean PesquisarPorEscolas() {
            return true;
        }
    },


    PROFESSOR{

        @Override
        public boolean podeCadastrarUsuarios() {
            return false;
        }

        @Override
        public boolean podeAtualizarDadosDoUsuario() {
            return true; // Os seus prórpios dados
        }

        @Override
        public boolean podeDeletarUsuarios() {
            return false;
        }

        @Override
        public boolean podeDesbloquearUsuarios() {
            return false;
        }

        @Override
        public boolean renovarAssinatura() {
            return false;
        }

        @Override
        public boolean podePesquisarPorDadosDeUsuarios() {
            return false;
        }

        @Override
        public boolean podePesquisarPorDadosDePerfil() {
            return true;
        }

        @Override
        public boolean temAcessoACursos() {
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
        public boolean CadastrarFormacoes() {
            return false;
        }

        @Override
        public boolean AtualizarFormacoes() {
            return false;
        }

        @Override
        public boolean PesquisarPorformacoes() {
            return true;
        }

        @Override
        public boolean podeCriarTopicos() {
            return false;
        }

        @Override
        public boolean atualizarTopicos() {
            return false;
        }

        @Override
        public boolean apagarTopicos() {
            return false;
        }

        @Override
        public boolean fecharTopicos() {
            return false;
        }

        @Override
        public boolean PesquisarPorTopicos() {
            return true;
        }

        @Override
        public boolean podeCriarRespostas() {
            return true;
        }

        @Override
        public boolean atualizarRespostas() {
            return true; // As próprias respostas
        }

        @Override
        public boolean cadastrarEscola() {
            return false;
        }

        @Override
        public boolean AtualizarEscola() {
            return false;
        }

        @Override
        public boolean PesquisarPorEscolas() {
            return true;
        }
    },


    SUPORTE{

        @Override
        public boolean podeCadastrarUsuarios() {
            return false;
        }

        @Override
        public boolean podeAtualizarDadosDoUsuario() {
            return false;
        }

        @Override
        public boolean podeDeletarUsuarios() {
            return false;
        }

        @Override
        public boolean podeDesbloquearUsuarios() {
            return false;
        }

        @Override
        public boolean renovarAssinatura() {
            return false;
        }

        @Override
        public boolean podePesquisarPorDadosDeUsuarios() {
            return false;
        }

        @Override
        public boolean podePesquisarPorDadosDePerfil() {
            return true;
        }

        @Override
        public boolean temAcessoACursos() {
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
        public boolean CadastrarFormacoes() {
            return false;
        }

        @Override
        public boolean AtualizarFormacoes() {
            return false;
        }

        @Override
        public boolean PesquisarPorformacoes() {
            return true;
        }

        @Override
        public boolean podeCriarTopicos() {
            return false;
        }

        @Override
        public boolean atualizarTopicos() {
            return false;
        }

        @Override
        public boolean apagarTopicos() {
            return false;
        }

        @Override
        public boolean fecharTopicos() {
            return true;
        }

        @Override
        public boolean PesquisarPorTopicos() {
            return true;
        }

        @Override
        public boolean podeCriarRespostas() {
            return true;
        }

        @Override
        public boolean atualizarRespostas() {
            return true; // As próprias respostas
        }

        @Override
        public boolean cadastrarEscola() {
            return false;
        }

        @Override
        public boolean AtualizarEscola() {
            return false;
        }

        @Override
        public boolean PesquisarPorEscolas() {
            return true;
        }
    };



    public abstract boolean podeCadastrarUsuarios();
    public abstract boolean podeAtualizarDadosDoUsuario(); // o próprio usuário
    public abstract boolean podeDeletarUsuarios();// professores e suporte --> exclusão lógica
    public abstract boolean podeDesbloquearUsuarios(); // adm e suporte
    public abstract boolean renovarAssinatura(); // Apenas exemplo, era para ser feito por meio de pagamento
    public abstract boolean podePesquisarPorDadosDeUsuarios();

    public abstract boolean podePesquisarPorDadosDePerfil();

    public abstract boolean temAcessoACursos();
    public abstract boolean podeCadastrarCursos();
    public abstract boolean podeAtualizarCursos();

    public abstract boolean CadastrarFormacoes();
    public abstract boolean AtualizarFormacoes();
    public abstract boolean PesquisarPorformacoes();

    public abstract boolean podeCriarTopicos();
    public abstract boolean atualizarTopicos();
    public abstract boolean apagarTopicos();
    public abstract boolean fecharTopicos();
    public abstract boolean PesquisarPorTopicos();

    public abstract boolean podeCriarRespostas();
    public abstract boolean atualizarRespostas();

    public abstract boolean cadastrarEscola();
    public abstract boolean AtualizarEscola();
    public abstract boolean PesquisarPorEscolas();
}

