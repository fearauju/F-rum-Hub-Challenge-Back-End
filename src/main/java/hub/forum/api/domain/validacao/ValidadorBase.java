package hub.forum.api.domain.validacao;

public interface ValidadorBase<T> {
    void validar(T dados);
}