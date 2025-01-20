package hub.forum.api.infra.exceptions;

public class BloqueioPermanenteException extends RuntimeException {
    public BloqueioPermanenteException(String message) {
        super(message);
    }
}
