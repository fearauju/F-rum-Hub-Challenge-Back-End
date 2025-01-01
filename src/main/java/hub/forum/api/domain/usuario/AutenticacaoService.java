package hub.forum.api.domain.usuario;

import hub.forum.api.domain.matricula.MatriculaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AutenticacaoService implements UserDetailsService {


    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private MatriculaService matriculaService;

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        log.debug("Tentando autenticar usuário: {}", login);

        try {
            var usuario = repository.findByLogin(login)
                    .orElseThrow(() -> {
                        log.error("Usuário não encontrado: {}", login);
                        return new UsernameNotFoundException("Usuário não encontrado");
                    });

            // Verificar bloqueio primeiro
            if (!usuario.isAccountNonLocked()) {
                log.warn("Tentativa de login em conta bloqueada: {}", login);
                throw new LockedException("Conta bloqueada após múltiplas tentativas. Contate o suporte.");
            }

            // Depois verificar matrícula para estudantes
            if (usuario.obterTipoUsuario() == TipoUsuario.ESTUDANTE
                    && matriculaService.verificarStatusMatricula(usuario.getId())) {
                log.error("Tentativa de login com matrícula inativa: {}", login);
                throw new DisabledException("Login não permitido. Renove sua matrícula");
            }

            // Se chegou aqui, registra sucesso
            usuarioService.registrarLoginSucesso(login);
            log.debug("Usuário autenticado com sucesso: {}", login);
            return usuario;

        } catch (BadCredentialsException e) {
            usuarioService.registrarTentativaLoginFalha(login);
            throw e;
        }
    }
}

