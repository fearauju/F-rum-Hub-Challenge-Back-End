package hub.forum.api.domain.usuario.validacao;

import hub.forum.api.domain.usuario.OperacaoDominio;

public interface ValidadorBase<T> {
    void validar(T dados, OperacaoDominio operacao);
    boolean suportaOperacao(OperacaoDominio operacao);
}
