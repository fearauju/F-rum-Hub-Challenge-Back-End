package hub.forum.api.infra.exceptions;

import org.springframework.security.core.AuthenticationException;

public class AutenticacaoException extends AuthenticationException {
    public AutenticacaoException(String message) {
        super(message);
    }
}
