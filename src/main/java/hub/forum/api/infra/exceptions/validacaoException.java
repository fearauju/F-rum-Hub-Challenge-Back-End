package hub.forum.api.infra.exceptions;


public class validacaoException extends RuntimeException {

    private String mensagem;

    public validacaoException(String mensagem) {
        super(mensagem);
    }
}
