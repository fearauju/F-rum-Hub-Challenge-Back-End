package hub.forum.api.infra.exceptions;

public class ValidacaoMatricula extends RuntimeException {

   private String mensagem;

    public ValidacaoMatricula(String mensagem) {
        super(mensagem);
    }
}
