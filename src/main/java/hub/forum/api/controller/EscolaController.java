package hub.forum.api.controller;

import hub.forum.api.domain.escola.dto.*;
import hub.forum.api.domain.escola.service.EscolaService;
import hub.forum.api.domain.util.PageResponse;
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

    @Operation(summary = "Cadastrar uma nova escola")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Escola cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para cadastrar escola")
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
                .buildAndExpand(escola.escolaId())
                .toUri();

        log.info("EscolaController retorna resultado da operação de cadastro");
        return ResponseEntity.created(uri).body(escola);
    }

    @Operation(summary = "Listar escolas",
            description = "Lista todas as escolas cadastradas, com paginação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem realizada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para listar escolas")
    })
    @GetMapping
    public ResponseEntity<PageResponse<DadosListagemEscola>> listarEscolas(
            @PageableDefault(size = 10, sort = {"nomeEscola"}) Pageable paginacao) {

        log.info("Buscando escolas. Página: {}, Tamanho: {}",
                paginacao.getPageNumber(),
                paginacao.getPageSize());

        var page = service.listarEscolas(paginacao);
        var response = new PageResponse<>(page);

        log.info("Encontradas {} escolas", page.getTotalElements());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Atualizar escola")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Escola atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para atualizar escola"),
            @ApiResponse(responseCode = "404", description = "Escola não encontrada")
    })
    @PutMapping("/{id}/atualizar_escola")
    @AutorizacaoAtualizarEscola
    public ResponseEntity<DadosDetalhamentoEscola> atualizarEscola(
            @PathVariable Long id,
            @RequestBody @Valid DadosAtualizacaoEscola dados) {

        log.info("Iniciando atualização do nome da escola para  {}", dados.nomeEscola());
        var escola = service.atualizarDadosEscola(id, dados);

        log.info("retornando dados detalhados da escola após atualização");
        return ResponseEntity.ok(escola);
    }
}
