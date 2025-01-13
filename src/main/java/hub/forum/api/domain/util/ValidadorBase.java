package hub.forum.api.domain.util;

public interface ValidadorBase<T> {
    void validar(T dados);
}