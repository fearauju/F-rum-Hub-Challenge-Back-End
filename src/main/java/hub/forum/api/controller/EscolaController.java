package hub.forum.api.controller;

import hub.forum.api.domain.escola.*;
import hub.forum.api.infra.security.anotacoes.AutorizacaoAtualizarEscola;
import hub.forum.api.infra.security.anotacoes.AutorizacaoCadastrarEscola;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/escolas")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Escolas", description = "Gerenciamento de escolas")
@Slf4j
public class EscolaController {

    @Autowired
   private EscolaService service;

    @Operation(summary = "Cadastrar uma nova escola",
            description = "Apenas administradores podem realizar esta operação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Sem permissão para cadastrar uma nova escola")
    })
    @PostMapping("/cadastrar_escola")
    @AutorizacaoCadastrarEscola
    public ResponseEntity<DadosDetalhamentoCadastroEscola> cadastrarEscola(
            @RequestBody @Valid DadosCadastroEscola dados,
            UriComponentsBuilder uriBuilder) {

        log.info("EscolaController delega o cadastro da escola para o serviço EscolaService");
        var escola = service.cadastrarEscola(dados);

        log.debug("Iniciando criação da uri");
        var uri = uriBuilder.path("/escolas/{id}")
                .buildAndExpand(escola.escolaID())
                .toUri();

        log.info("EscolaController retorna resultado da operação de cadastro");
        return ResponseEntity.created(uri).body(escola);
    }

    @Operation(summary = "Listar escolas",
            description = "Listar as escolas cadastradas organizadas em ordem alfabética.")
    @GetMapping
    public ResponseEntity<Page<DadosListagemEscola>> listarEscolas(
            @PageableDefault(sort = {"nomeEscola"}) Pageable paginacao) {

        log.info("Iniciando a busca por escolas cadastradas");
        var page = service.listarEscolas(paginacao);

        log.info("Retorna una lista paginada de escolas cadastradas");
        return ResponseEntity.ok(page);
    }

    @Operation(summary = "Atualizar escola",
            description = "Apenas administradores podem realizar esta operação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Sem permissão para realizar esta operação")
    })
    @PutMapping("/{id}/atualizar_escola")
    @AutorizacaoAtualizarEscola
    public ResponseEntity<DadosDetalhamentoEscola> atualizarEscola(
            @PathVariable Long id,
            @RequestBody @Valid DadosAtualizacaoEscola dados) {

        log.info("Iniciando atualização de dados da escola  {}", dados.nomeEscola());
        var escola = service.atualizarDadosEscola(id, dados);

        log.info("retornando dados detalhados da escola após atualização");
        return ResponseEntity.ok(escola);
    }
}
