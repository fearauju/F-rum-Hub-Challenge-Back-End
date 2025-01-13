package hub.forum.api.controller;

import hub.forum.api.domain.usuario.dto.DadosAutenticacao;
import hub.forum.api.domain.usuario.Usuario;
import hub.forum.api.infra.security.DadosTokenJWT;
import hub.forum.api.infra.security.RateLimitService;
import hub.forum.api.infra.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@Tag(name = "Autenticação", description = "Endpoints para autenticação de usuários")
@Slf4j
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RateLimitService rateLimitService;

    @Operation(summary = "Efetua login",
            description = "Autentica o usuário e retorna um token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login efetuado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
            @ApiResponse(responseCode = "429", description = "Muitas tentativas de login")
    })
    @PostMapping
    public ResponseEntity<?> efetuarLogin(
            @RequestBody @Valid DadosAutenticacao dados) {

        if (!rateLimitService.tryConsume(dados.login())) {
            log.warn("Limite de tentativas excedido para o login: {}", dados.login());
            return ResponseEntity
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Muitas tentativas de login. Tente novamente em 10 minutos.");
        }

        try {
            log.debug("Tentativa de login para usuário: {}", dados.login());

            var authenticationToken = new UsernamePasswordAuthenticationToken(
                    dados.login(),
                    dados.senha()
            );

            var authentication = authenticationManager.authenticate(authenticationToken);
            var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

            log.info("Login bem-sucedido para usuário: {}", dados.login());

            return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));

        } catch (AuthenticationException e) {
            log.error("Falha na autenticação para usuário: {}", dados.login());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciais inválidas");
        }
    }
}
