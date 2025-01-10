package hub.forum.api.infra.security;

import hub.forum.api.domain.usuario.Usuario;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.security.config.Elements.JWT;

@Service
@Slf4j
public class TokenService {

    @Autowired
    @Value("${api.security.token.secret}")
    private String secretKey;

    @Autowired
    @Value("${api.security.token.expiration}")
    private int tokenExpirationInMinutes;

    private Key key;

    private Key getSigningKey() {
        if (key == null) {
            key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        }
        return key;
    }
    public String gerarToken(Usuario usuario) {
        log.debug("Gerando token para usuário: {} com tipo: {}",
                usuario.getLogin(), usuario.obterTipoUsuario());

        return Jwts.builder()
                .setSubject(usuario.getLogin())
                .claim("tipo", usuario.obterTipoUsuario().name())
                .setIssuer("API Forum.hub")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() +
                        (tokenExpirationInMinutes * 60 * 1000L)))
                .signWith(getSigningKey())
                .compact();
    }

    public String getSubject(String token) {
        try {
            var claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            log.debug("Claims do token: {}", claims);
            log.debug("Tipo de usuário no token: {}", claims.get("tipo"));

            return claims.getSubject();
        } catch (Exception e) {
            log.error("Erro ao extrair subject do token: ", e);
            throw new RuntimeException("Token inválido ou expirado!", e);
        }
    }


    // Método de validação atualizado
    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}


