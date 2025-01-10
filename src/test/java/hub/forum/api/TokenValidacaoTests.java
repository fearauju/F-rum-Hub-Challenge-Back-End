package hub.forum.api;

import hub.forum.api.domain.usuario.*;
import hub.forum.api.infra.security.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class TokenValidacaoTests {

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        this.tokenService = new TokenService();

        // Configura a chave secreta
        ReflectionTestUtils.setField(tokenService, "secretKey",
                "12345678901234567890123456789012");

        // Configura o tempo de expiração (em minutos)
        ReflectionTestUtils.setField(tokenService, "tokenExpirationInMinutes", 30);
    }

    @Test
    void deveGerarTokenValido() {
        // Arrange
        var usuario = criarUsuarioMock(1L, "user@email.com", TipoUsuario.PROFESSOR);

        // Act
        String token = tokenService.gerarToken(usuario);

        // Assert
        assertNotNull(token);
        assertTrue(tokenService.validarToken(token));
    }

    @Test
    void deveExtrairSubjectDoToken() {
        // Arrange
        var usuario = criarUsuarioMock(1L, "user@email.com", TipoUsuario.PROFESSOR);
        var token = tokenService.gerarToken(usuario);

        // Act
        String subject = tokenService.getSubject(token);

        // Assert
        assertEquals("user@email.com", subject);
    }

    @Test
    void deveRetornarFalsoParaTokenInvalido() {
        // Act & Assert
        assertFalse(tokenService.validarToken("token_invalido"));
    }

    @Test
    void deveConterTipoUsuarioNoClaim() {
        // Arrange
        var usuario = criarUsuarioMock(1L, "user@email.com", TipoUsuario.PROFESSOR);

        // Act
        String token = tokenService.gerarToken(usuario);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor("12345678901234567890123456789012"
                        .getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Assert
        assertEquals("PROFESSOR", claims.get("tipo"));
    }

    @Test
    void deveGerarTokenComTempoExpiracaoCorreto() {
        // Arrange
        var usuario = criarUsuarioMock(1L, "user@email.com", TipoUsuario.PROFESSOR);

        // Act
        String token = tokenService.gerarToken(usuario);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor("12345678901234567890123456789012"
                        .getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Assert
        Date expiration = claims.getExpiration();
        Date now = new Date();
        long diffInMinutes = (expiration.getTime() - now.getTime()) / (60 * 1000);

        // Verifica se o tempo de expiração está próximo de 30 minutos
        assertTrue(diffInMinutes <= 30 && diffInMinutes > 28);
    }

    private Usuario criarUsuarioMock(Long id, String login, TipoUsuario tipo) {
        Usuario usuario;
        switch(tipo) {
            case PROFESSOR -> usuario = new Professor();
            case SUPORTE -> usuario = new Suporte();
            case ESTUDANTE -> usuario = new Estudante();
            case ADMINISTRADOR -> usuario = new Administrador();
            default -> throw new IllegalArgumentException("Tipo inválido");
        }
        usuario.setId(id);
        usuario.setLogin(login);
        usuario.setAtivo(true);
        return usuario;
    }
}