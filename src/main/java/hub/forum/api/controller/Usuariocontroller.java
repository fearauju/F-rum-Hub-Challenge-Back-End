package hub.forum.api.controller;

import hub.forum.api.domain.usuario.*;
import hub.forum.api.domain.usuario.dto.DadosAtualizacaoLogin;
import hub.forum.api.domain.usuario.dto.DadosDesbloquearConta;
import hub.forum.api.domain.usuario.dto.DadosExclusaoUsuario;
import hub.forum.api.domain.usuario.service.UsuarioService;
import hub.forum.api.infra.exceptions.ValidacaoException;
import hub.forum.api.infra.security.anotacoes.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController()
@RequestMapping("/usuarios")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Controller do usuário", description = "Endpoints para receber requisições referentes ao usuário")
@Slf4j
public class Usuariocontroller {

    @Autowired
    private UsuarioService service;


    @Operation(summary = "Inativa um usuário",
            description = "Inativa um usuário do sistema. Apenas administradores podem realizar esta operação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário inativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para inativar usuários")
    })
    @PutMapping("/inativar")
    @AutorizacaoInativarUsuarios
    public ResponseEntity<Void> inativarUsuario(
            @RequestBody @Valid DadosExclusaoUsuario dados,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        log.info("Iniciando processo de inativação do usuário ID: {} pelo usuário: {}",
                dados.usuarioInativoID(), usuarioLogado.getLogin());

        try {
            service.inativarUsuario(dados);
            log.info("Usuário ID: {} inativado com sucesso", dados.usuarioInativoID());
            return ResponseEntity.noContent().build();
        } catch (ValidacaoException e) {
            log.error("Erro ao inativar usuário: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


    @Operation(summary = "Desbloqueia conta de usuário",
            description = "Desbloqueia a conta de um usuário que foi bloqueada por tentativas de login.")
    @PutMapping("/desbloquear")
    @AutorizacaoDesbloquearConta
    public ResponseEntity<Void> desbloquearConta(
            @Valid DadosDesbloquearConta dados,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        log.info("Iniciando desbloqueio de conta ID: {} pelo usuário: {}",
                dados.usuarioID(), usuarioLogado.getLogin());

        service.desbloquearConta(dados.usuarioID());
        log.info("Conta ID: {} desbloqueada com sucesso", dados.usuarioID());

        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Atualiza dados de login",
            description = "Permite que um usuário atualize seu login e/ou senha.")
    @PutMapping("{id}/atualizar")
    @AutorizacaoAtualizarDadosLogin
    public ResponseEntity<String> atualizarLogin(
            @PathVariable Long id,
            @Valid @RequestBody DadosAtualizacaoLogin dados) {

        log.info("Iniciando atualização de dados de login para usuário ID: {}", id);

        try {

            service.atualizarDadosLogin(id, dados);
            log.info("Dados de login atualizados com sucesso para usuário ID: {}", id);

            return ResponseEntity.ok("Dados atualizados com sucesso");

        } catch (ValidacaoException e) {
            log.warn("Erro de validação ao atualizar dados: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {
            log.error("Erro ao atualizar dados: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Erro ao processar a requisição");
        }
    }
}
