package hub.forum.api.infra.exceptions;

import org.springframework.security.core.AuthenticationException;

public class FalhasDeLoginExceptions extends AuthenticationException {
    public FalhasDeLoginExceptions(String message) {
        super(message);
    }
}
