package hub.forum.api.infra.security;

import hub.forum.api.domain.usuario.Usuario;
import hub.forum.api.domain.usuario.repository.UsuarioRepository;
import hub.forum.api.infra.exceptions.BloqueioPermanenteException;
import hub.forum.api.infra.exceptions.BloqueioTemporarioException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class RateLimitService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final Map<String, Bucket> tempBuckets = new ConcurrentHashMap<>();
    private final Map<String, Integer> failedAttempts = new ConcurrentHashMap<>();

    public boolean tryConsume(String login) {
        Usuario usuario = usuarioRepository.findByLogin(login);
        if (usuario != null && usuario.isBloqueadoPermanente()) {
            log.error("Tentativa de login em conta bloqueada permanentemente: {}", login);
            throw new BloqueioPermanenteException(
                    "Conta bloqueada permanentemente por excesso de tentativas. Contate o administrador.");
        }

        // Usando lambda expression
        Bucket tempBucket = tempBuckets.computeIfAbsent(login, k -> {
            Bandwidth limit = Bandwidth.builder()
                    .capacity(3)
                    .refillGreedy(3, Duration.ofMinutes(10))
                    .build();
            return Bucket.builder()
                    .addLimit(limit)
                    .build();
        });

        boolean tempAllowed = tempBucket.tryConsume(1);

        if (!tempAllowed) {
            incrementarTentativasFalhas(login);
            log.warn("Bloqueio temporário para usuário: {}. Tente novamente em 10 minutos.", login);
            throw new BloqueioTemporarioException(
                    "Número máximo de tentativas excedido. Tente novamente em 10 minutos.");
        }

        return true;
    }

    // Novo método para registrar falha de autenticação
    public void registrarFalhaAutenticacao(String login) {
        incrementarTentativasFalhas(login);
    }

    private void incrementarTentativasFalhas(String login) {
        int attempts = failedAttempts.merge(login, 1, Integer::sum);
        log.info("Tentativa de login falha para usuário: {}. Total de tentativas falhas: {}", login, attempts);

        if (attempts >= 6) {
            Usuario usuario = usuarioRepository.findByLogin(login);
            if (usuario != null) {
                usuario.setBloqueadoPermanente(true);
                usuarioRepository.save(usuario);
                log.error("Conta bloqueada permanentemente para usuário: {}. Limite de 6 tentativas excedido.", login);
                throw new BloqueioPermanenteException(
                        "Conta bloqueada permanentemente por excesso de tentativas. Contate o administrador.");
            }
        }
    }

    // Método para login bem-sucedido
    public void registrarLoginSucesso(String login) {
        tempBuckets.remove(login);
        failedAttempts.remove(login);
        log.info("Login bem-sucedido: tentativas resetadas para usuário: {}", login);
    }

    public void desbloquearConta(String login) {
        Usuario usuario = usuarioRepository.findByLogin(login);
        if (usuario != null && usuario.isBloqueadoPermanente()) {
            usuario.setBloqueadoPermanente(false);
            usuarioRepository.save(usuario);
        }
        tempBuckets.remove(login);
        failedAttempts.remove(login);
        log.info("Conta desbloqueada para usuário: {}", login);
    }

    /**
     * Reseta as tentativas de login do usuário (após login bem-sucedido).
     *
     * @param login Login do usuário
     */
    public void resetarTentativas(String login) {
        tempBuckets.remove(login);
        failedAttempts.remove(login);
        log.info("Tentativas de login resetadas para usuário: {}", login);
    }
}

