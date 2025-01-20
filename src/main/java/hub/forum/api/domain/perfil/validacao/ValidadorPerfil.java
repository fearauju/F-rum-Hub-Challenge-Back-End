package hub.forum.api.domain.perfil.validacao;

import hub.forum.api.domain.perfil.OperacaoPerfil;

public interface ValidadorPerfil<T> {
    void validar(T dados, OperacaoPerfil operacao);
}