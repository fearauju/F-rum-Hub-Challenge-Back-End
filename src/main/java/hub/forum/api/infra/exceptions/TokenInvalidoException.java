package hub.forum.api.infra.exceptions;

public class TokenInvalidoException extends RuntimeException {
    public TokenInvalidoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
