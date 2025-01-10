package hub.forum.api.domain.usuario;

import hub.forum.api.domain.matricula.MatriculaService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AutenticacaoService implements UserDetailsService {


    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MatriculaService matriculaService;

    @Autowired
    private UsuarioService usuarioService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        try {
            var usuario = usuarioRepository.findByLogin(login)
                    .orElseThrow(() -> {
                        log.error("Usuário não encontrado: {}", login);
                        return new UsernameNotFoundException("Usuário não encontrado");
                    });

            log.debug("Usuário encontrado, verificando status...");

            // Log dos estados importantes
            log.debug("Status do usuário - Ativo: {}, Bloqueado: {}, Login: {}",
                    usuario.isAtivo(),
                    usuario.isAccountNonLocked(),
                    usuario.getLogin());

            if (!usuario.isAtivo()) {
                log.warn("Usuário inativo: {}", login);
                throw new DisabledException("Usuário inativo");
            }

            if (!usuario.isAccountNonLocked()) {
                log.warn("Conta bloqueada: {}", login);
                throw new LockedException("Conta bloqueada após múltiplas tentativas");
            }

            if (usuario instanceof Estudante) {
                boolean matriculaAtiva = matriculaService.verificarStatusMatricula(usuario.getId());
                log.debug("Verificação de matrícula para estudante: {}", matriculaAtiva);

                if (!matriculaAtiva) {
                    throw new DisabledException("Matrícula expirada");
                }
            }

            usuarioService.registrarLoginSucesso(login);
            return usuario;

        } catch (Exception e) {
            log.error("Erro durante autenticação: {}", e.getMessage(), e);
            if (e instanceof BadCredentialsException) {
                usuarioService.registrarTentativaLoginFalha(login);
            }
            throw e;
        }
    }
}

