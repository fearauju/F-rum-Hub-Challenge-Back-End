package hub.forum.api.controller;

import hub.forum.api.domain.usuario.Usuario;
import hub.forum.api.domain.usuario.suporte.dto.DadosAtualizacaoSuporte;
import hub.forum.api.domain.usuario.suporte.dto.DadosCadastroSuporte;
import hub.forum.api.domain.usuario.suporte.dto.DadosDetalhamentoSuporte;
import hub.forum.api.domain.usuario.suporte.service.SuporteService;
import hub.forum.api.domain.util.PageResponse;
import hub.forum.api.infra.security.anotacoes.AutorizacaoAtualizarSuporte;
import hub.forum.api.infra.security.anotacoes.AutorizacaoCadastrarUsuarios;
import hub.forum.api.infra.security.anotacoes.AutorizacaoPesquisarPorUsuarios;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/suportes")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Suporte", description = "Gerenciamento do usuário suporte")
@Slf4j
public class SuporteController {

    @Autowired
    private SuporteService service;

    @Operation(summary = "Cadastrar suporte")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Suporte cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para cadastrar suporte"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PostMapping("/{usuarioId}/cadastrar_suporte")
    @AutorizacaoCadastrarUsuarios
    public ResponseEntity<DadosDetalhamentoSuporte> cadastrarSuporte(@PathVariable Long usuarioId,
                                                                     @RequestBody @Valid DadosCadastroSuporte dados,
                                                                     UriComponentsBuilder uriBuilder){
        var suporte = service.cadastrarSuporte(usuarioId, dados);
        var uri = uriBuilder.path("/suportes/{id}").buildAndExpand(suporte.id ()).toUri();

        return ResponseEntity.created(uri).body(suporte);
    }

    @Operation(summary = "Listar equipe de suporte")
    @GetMapping
    @AutorizacaoPesquisarPorUsuarios
    public ResponseEntity<PageResponse<DadosDetalhamentoSuporte>> listarEquipeSuporte(
            @PageableDefault(sort = "usuario.perfil.nome") Pageable paginacao) {
        return ResponseEntity.ok(service.listarEquipeSuporte(paginacao));
    }


    @Operation(summary = "Atualizar cadastro",
            description = "Atualizar dados do usuário do tipo suporte")
    @PutMapping("/{usuarioId}/atualizar_suporte")
    @AutorizacaoAtualizarSuporte
    public ResponseEntity<DadosDetalhamentoSuporte> atualizarCadastro(@RequestBody @Valid DadosAtualizacaoSuporte dados,
                                                                      @PathVariable Long usuarioId){

        var suporte = service.atualizarSuporte(dados, usuarioId);
        return ResponseEntity.ok(suporte);
    }
}
