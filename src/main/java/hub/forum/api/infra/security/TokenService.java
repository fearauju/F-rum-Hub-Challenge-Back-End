package hub.forum.api.infra.security;

import hub.forum.api.domain.usuario.Usuario;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
@Slf4j
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secretKey;

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
        log.debug("Gerando token para usuário: {}", usuario.getLogin());
        String token = Jwts.builder()
                .setSubject(usuario.getLogin())
                .setIssuer("API Forum.hub")
                .setAudience("meu-cliente")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (tokenExpirationInMinutes * 60 * 1000L)))
                .signWith(getSigningKey())
                .compact();
        log.debug("Token gerado com sucesso");
        return token;
    }

    public String getSubject (String token){
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey()) // Usa a chave inicializada
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            System.out.println(getSigningKey());
            System.out.println(claims.getSubject());

            return claims.getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Token inválido ou expirado!", e);
        }
    }

    public String validarToken(String token) {
        try {
            log.debug("Iniciando validação do token");
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey()) // Aqui está a correção
                    .requireIssuer("API Forum.hub")
                    .requireAudience("meu-cliente")
                    .build()
                    .parseClaimsJws(token);

            String subject = claimsJws.getBody().getSubject();
            log.debug("Token válido para o usuário: {}", subject);
            return subject;
        } catch (JwtException e) {
            log.error("Erro na validação do token: ", e);
            throw new RuntimeException("Token inválido ou expirado!", e);
        }
    }
}


