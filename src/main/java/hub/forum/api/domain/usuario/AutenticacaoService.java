package hub.forum.api.domain.usuario;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        log.debug("Tentando autenticar usuário com login: {}", login);

        return repository.findByLogin(login)
                .orElseThrow(() -> {
                    log.error("Usuário não encontrado para o login: {}", login);
                    return new UsernameNotFoundException("Usuário não encontrado: " + login);
                });
    }
}

