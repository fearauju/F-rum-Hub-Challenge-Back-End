package hub.forum.api.controller;

import hub.forum.api.domain.usuario.Usuario;
import hub.forum.api.domain.usuario.suporte.DadosCadastroSuporte;
import hub.forum.api.domain.usuario.suporte.DadosDetalhamentoSuporte;
import hub.forum.api.domain.usuario.suporte.SuporteService;
import hub.forum.api.infra.security.anotacoes.AutorizacaoPesquisarPorUsuarios;
import io.swagger.v3.oas.annotations.Operation;
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
@Slf4j
public class SuporteController {

    @Autowired
    private SuporteService service;

    //add logs posteriormente
    @PostMapping("/{id}/cadastrar_suporte")
    public ResponseEntity<DadosDetalhamentoSuporte> cadastrarSuporte(@PathVariable Long id,
                                                                     @RequestBody @Valid DadosCadastroSuporte dados,
                                                                     UriComponentsBuilder uriBuilder){
        var suporte = service.cadastrarSuporte(id, dados);
        var uri = uriBuilder.path("/cadastrar_suporte/{id}").buildAndExpand(id).toUri();

        return ResponseEntity.created(uri).body(suporte);
    }

    @Operation(summary = "Lista equipe de suporte",
            description = "Retorna uma lista paginada da equipe de suporte")
    @GetMapping("/time_suporte")
    @AutorizacaoPesquisarPorUsuarios
    public ResponseEntity<Page<DadosDetalhamentoSuporte>> listarEquipeSuporte(
            @PageableDefault(sort = "perfil.nome") Pageable paginacao,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        log.debug("Listando equipe de suporte. Solicitado por: {}", usuarioLogado.getLogin());

        var listaSuporte = service.listarEquipeSuporte(paginacao);
        log.info("Retornando {} registros de suporte", listaSuporte.getTotalElements());

        return ResponseEntity.ok(listaSuporte);
    }
}
