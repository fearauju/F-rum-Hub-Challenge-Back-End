package hub.forum.api.infra.exceptions;

import hub.forum.api.domain.perfil.OperacaoPerfil;

public class PerfilValidacaoException extends RuntimeException {
    private final OperacaoPerfil erro;

    public PerfilValidacaoException(OperacaoPerfil erro) {
        super(erro.name());
        this.erro = erro;
    }

    public OperacaoPerfil getErro() {
        return erro;
    }
}