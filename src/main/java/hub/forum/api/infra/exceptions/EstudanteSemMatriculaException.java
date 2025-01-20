package hub.forum.api.infra.exceptions;

import org.springframework.security.core.AuthenticationException;

public class EstudanteSemMatriculaException extends AuthenticationException {
    public EstudanteSemMatriculaException(String msg) {
        super(msg);
    }
}