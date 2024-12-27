package hub.forum.api.infra.exceptions;


public class ValidacaoException extends RuntimeException {

    private String mensagem;

    public ValidacaoException(String mensagem) {
        super(mensagem);
    }
}
