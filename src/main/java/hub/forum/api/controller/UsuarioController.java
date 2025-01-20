package hub.forum.api.controller;

import hub.forum.api.domain.usuario.*;
import hub.forum.api.domain.usuario.dto.DadosAtualizacaoLogin;
import hub.forum.api.domain.usuario.dto.DadosDetalhamentoUsuarioInativo;
import hub.forum.api.domain.usuario.service.UsuarioService;
import hub.forum.api.domain.util.PageResponse;
import hub.forum.api.infra.security.anotacoes.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/usuarios")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Controller do usuário", description = "Endpoints para receber requisições referentes ao usuário")
@Slf4j
public class UsuarioController {

    @Autowired
    private UsuarioService service;


    @Operation(summary = "Inativa um usuário",
            description = "Inativa um usuário do sistema. Apenas administradores podem realizar esta operação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário inativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para inativar usuários"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @DeleteMapping("/{id}/inativar")
    @AutorizacaoInativarUsuarios
    public ResponseEntity<Void> inativarUsuario(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        log.info("Iniciando processo de inativação do usuário ID: {} pelo usuário: {}",
                id, usuarioLogado.getLogin());

        service.inativarUsuario(id);
        log.info("Usuário ID: {} inativado com sucesso", id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Reativar um usuário",
            description = "Reativar um usuário do sistema. Apenas administradores podem realizar esta operação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário reativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para reativar usuários"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PutMapping("/{id}/reativar")
    @AutorizacaoReativarUsuario
    public ResponseEntity<Void> reativarUsuario(@PathVariable Long id){

        log.info("Iniciando reativação do usuário com ID {}", id);
        service.reativar(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Desbloqueia conta de usuário",
            description = "Desbloqueia a conta de um usuário que foi bloqueada por tentativas de login.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Conta desbloqueada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para desbloquear conta"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PutMapping("/{id}/desbloquear")
    @AutorizacaoDesbloquearConta
    public ResponseEntity<Void> desbloquearConta(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuarioLogado) {
        log.info("Iniciando desbloqueio de conta ID: {} pelo usuário: {}",
                id, usuarioLogado.obterTipoUsuario());

        service.desbloquearConta(id);
        log.info("Conta ID: {} desbloqueada com sucesso", id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/inativados")
    public ResponseEntity<PageResponse<DadosDetalhamentoUsuarioInativo>> detalharUsuariosInativos(
            @PageableDefault(sort = "perfil.nome") Pageable paginacao
            ){

        log.info("Listando usuários inativos");
        return ResponseEntity.ok(service.detalharUsuariosInativos(paginacao));
    }


    @Operation(summary = "Atualiza dados de login",
            description = "Permite que um usuário atualize seu login e/ou senha.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados atualizados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
    })
    @PutMapping("/{id}/atualizar")
    @AutorizacaoAtualizarDadosLogin
    public ResponseEntity<String> atualizarLogin(
            @PathVariable Long id,
            @Valid @RequestBody DadosAtualizacaoLogin dados) {

        log.info("Iniciando atualização de dados de login para usuário ID: {}", id);

        service.atualizarDadosLogin(id, dados);
        log.info("Dados de login atualizados com sucesso para usuário ID: {}", id);

        return ResponseEntity.ok("Dados atualizados com sucesso");
    }
}
