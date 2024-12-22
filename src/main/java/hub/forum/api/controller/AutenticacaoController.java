package hub.forum.api.controller;

import hub.forum.api.domain.usuario.DadosAutenticacao;
import hub.forum.api.domain.usuario.Usuario;
import hub.forum.api.infra.security.DadosTokenJWT;
import hub.forum.api.infra.security.TokenService;
import jakarta.validation.Valid;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<DadosTokenJWT> efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) throws JoseException, MalformedClaimException {

        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(),dados.senha());
        var authentication = authenticationManager.authenticate(authenticationToken);

        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());
        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }
}
