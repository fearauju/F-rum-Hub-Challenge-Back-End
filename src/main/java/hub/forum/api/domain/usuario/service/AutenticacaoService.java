package hub.forum.api.domain.usuario.service;

import hub.forum.api.domain.matricula.repository.MatriculaRepository;
import hub.forum.api.domain.usuario.TipoUsuario;
import hub.forum.api.domain.usuario.repository.UsuarioRepository;
import hub.forum.api.infra.exceptions.EstudanteSemMatriculaException;
import hub.forum.api.infra.security.RateLimitService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@Slf4j
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private RateLimitService rateLimitService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        try {
            var usuario = usuarioRepository.findByLoginAndAtivoTrue(login);

            if (usuario == null) {
                log.error("Usuário não encontrado para o login: {}", login);
                throw new UsernameNotFoundException("Usuário não encontrado");
            }

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

            // Verificação específica para estudantes
            if (usuario.getTipoUsuario() == TipoUsuario.ESTUDANTE) {

                var  matriculaOptional = matriculaRepository.findByEstudanteIdWithCursos(usuario.getId());
                if (matriculaOptional.isEmpty()) {

                    log.warn("Estudante ID {} não possui matrícula ativa.", usuario.getId());
                    throw new EstudanteSemMatriculaException(
                            "Estudante ainda não foi associado a uma matricula. Realize seu cadastro");
                }

                    var matricula = matriculaOptional.get();

                // Verifica e atualiza o status da matrícula
                if (LocalDateTime.now().isAfter(matricula.getDataExpiracaoAssinatura())) {

                    matriculaRepository.alterarStatusMatricula(matricula.getId());

                    log.warn("Matrícula do estudante ID {} expirada em {}",
                            usuario.getId(), matricula.getDataExpiracaoAssinatura());
                    throw new EstudanteSemMatriculaException("Matrícula expirada. É necessário renovar a matrícula para continuar a ter acesso à plataforma");
                }
            }

            return usuario;

        } catch (AuthenticationException e) {
            throw e; // Repassar exceções de autenticação diretamente
        } catch (Exception e) {
            log.error("Erro inesperado durante autenticação", e);
            throw new AuthenticationServiceException("Erro interno durante autenticação", e);
        }
    }
}

