package hub.forum.api.infra.security;


import hub.forum.api.domain.usuario.Usuario;
import hub.forum.api.domain.usuario.UsuarioRepository;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private static String secretKey ;

    @Autowired
    private UsuarioRepository repository;

    // Gera um token JWT
    public String gerarToken(Usuario usuario) throws JoseException {

        JwtClaims claims = new JwtClaims();

        claims.setIssuer("API Forum.hub"); // Quem emite o token
        claims.setAudience("meu-cliente"); // Quem pode usar o token
        claims.setExpirationTimeMinutesInTheFuture(120); // Expira em 120 minutos
        claims.setGeneratedJwtId(); // Gera um ID único
        claims.setIssuedAtToNow(); // Data de emissão
        claims.setSubject(usuario.getLogin()); // Identificação do usuário
        claims.setClaim("role", usuario.getAuthorities()); // Adiciona o papel do usuário

        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(new HmacKey(secretKey.getBytes())); // Assina com a chave secreta
        jws.setAlgorithmHeaderValue("HS256");

        return jws.getCompactSerialization();
    }

    // Valida o token JWT e retorna o subject (identificação do usuário)
    public String getSubject(String token) {
        try {

            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                    .setRequireExpirationTime() // Exige que o token tenha tempo de expiração
                    .setExpectedIssuer("API Forum.hub") // Valida o emissor
                    .setExpectedAudience("usuario") // Valida o destinatário
                    .setVerificationKey(new HmacKey(secretKey.getBytes())) // Chave secreta
                    .build();

            // Decodifica e valida o token
            JwtClaims claims = jwtConsumer.processToClaims(token);

            // Retorna o subject (login do usuário)
            return claims.getSubject();

        } catch (Exception e) {
            throw new RuntimeException("Token inválido ou expirado!", e);
        }
    }


    public static void verificarToken(String token) throws Exception {
        // 1. Configurar o consumidor JWT
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime() // O token deve expirar
                .setExpectedIssuer("API Forum.hub") // Valida o emissor
                .setExpectedAudience("usuario") // Valida o destinatário
                .setVerificationKey(new HmacKey(secretKey.getBytes())) // Chave secreta
                .build();

        // 2. Validar e processar o token
        jwtConsumer.processToClaims(token); // Decodifica e valida as claims
        System.out.println("Token JWT válido!");
    }

    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
