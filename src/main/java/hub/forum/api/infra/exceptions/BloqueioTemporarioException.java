package hub.forum.api.infra.exceptions;

public class BloqueioTemporarioException extends RuntimeException {
    public BloqueioTemporarioException(String message) {
        super(message);
    }
}
