package hub.forum.api.infra.security;


import hub.forum.api.domain.usuario.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@Slf4j
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        log.debug("Processando requisição para: {} {}",
                request.getMethod(),
                request.getRequestURI());

        var tokenJWT = recuperarToken(request);

        if (tokenJWT != null) {
            try {
                log.debug("Token encontrado, iniciando validação");
                var subject = tokenService.getSubject(tokenJWT);

                var usuario = usuarioRepository.findByLoginAndAtivoTrue(subject);

                if(usuario == null){
                    log.error("Usuário não encontrado para o login: {}", subject);
                    throw new UsernameNotFoundException("Usuário não encontrado");
                }

                log.debug("Usuário encontrado: {}", usuario.getLogin());
                log.debug("Tipo de usuário: {}", usuario.obterTipoUsuario());
                log.debug("Authorities: {}", usuario.getAuthorities());

                var authentication = new UsernamePasswordAuthenticationToken(
                        usuario,
                        null,
                        usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.info("Autenticação realizada com sucesso. Authorities definidas: {}",
                        authentication.getAuthorities());
            } catch (Exception e) {
                log.error("Erro ao processar token: ", e);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizedHeader = request.getHeader("Authorization");

        if(authorizedHeader != null){
            return authorizedHeader.replace("Bearer ","");
        }

        return null;
    }
}
