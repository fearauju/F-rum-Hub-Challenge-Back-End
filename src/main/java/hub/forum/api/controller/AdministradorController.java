package hub.forum.api.controller;

import hub.forum.api.domain.usuario.Usuario;
import hub.forum.api.domain.usuario.administrador.dto.DadosRegistroAcao;
import hub.forum.api.domain.usuario.administrador.service.AdministradorService;
import hub.forum.api.infra.security.anotacoes.AutorizacaoRegistrarAcaoSAdministrador;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/administrador")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Administrador", description = "Endpoints para gerenciar ações do usuário administrador")
@Slf4j
public class AdministradorController {

    @Autowired
    private AdministradorService service;

    @Operation(summary = "Registrar alterações internas",
            description = "Somente administrador pode executar essas alterações.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para realizar esta operação"),
    })
    @PostMapping("/registrar_acao")
    @AutorizacaoRegistrarAcaoSAdministrador
    public ResponseEntity<String> registrarAcao(@RequestBody @Valid DadosRegistroAcao dados,
                                                @AuthenticationPrincipal Usuario usuario){

        log.info("Iniciando registro de ação pelo usuário ADMINISTRADOR");
        service.registrarAcao(dados, usuario);

        return ResponseEntity.ok("A ação foi Registrada");
    }


}
