package hub.forum.api.controller;

import hub.forum.api.domain.topico.dto.*;
import hub.forum.api.domain.topico.service.TopicoService;
import hub.forum.api.domain.usuario.Usuario;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Topicos", description = "Gerenciamento de topicos do fórum")
@Slf4j
public class TopicoController {

    @Autowired
    private TopicoService service;


    @Operation(summary = "Criar novo tópico",
            description = "Endpoint para criação de um novo tópico no fórum")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tópico criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para criar tópico")
    })
    @AutorizacaoCriarTopico
    @PostMapping
    public ResponseEntity<DadosDetalhamentoTopico> criarTopico(
            @RequestBody @Valid DadosCadastroTopico dados,
            UriComponentsBuilder uriBuilder,
            @AuthenticationPrincipal Usuario estudante) {

        log.info("Iniciando criação de novo tópico");
        var topico = service.criarTopico(dados, estudante);

        var uri = uriBuilder.path("/topicos/{id}")
                .buildAndExpand(topico.id ()).toUri();

        return ResponseEntity.created(uri).body(topico);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalhar tópico específico")
    public ResponseEntity<DadosDetalhamentoTopico> detalharTopico(@PathVariable Long id) {
        var topico = service.detalharTopico(id);

        return ResponseEntity.ok(topico);
    }

    @GetMapping
    @Operation(summary = "Listar todos os tópicos")
    public ResponseEntity<PageResponse<DadosListagemTopico>> listarTopicos(
            @PageableDefault(sort = "dataCriacao", direction = Sort.Direction.DESC)
            Pageable paginacao) {
        return ResponseEntity.ok(service.listarTopicos(paginacao));
    }



    @GetMapping("/nao-resolvidos")
    @Operation(summary = "Listar tópicos não resolvidos")
    public ResponseEntity<PageResponse<DadosListagemTopico>> listarTopicosNaoResolvidos(
            @PageableDefault(sort = "dataCriacao", direction = Sort.Direction.DESC)
            Pageable paginacao) {
        return ResponseEntity.ok(service.listarTopicosNaoResolvidos(paginacao));
    }

    @GetMapping("/resolvidos")
    @Operation(summary = "Listar tópicos resolvidos")
    public ResponseEntity<PageResponse<DadosListagemTopico>> listarTopicosResolvidos(
            @PageableDefault(sort = "dataCriacao", direction = Sort.Direction.DESC)
            Pageable paginacao) {
        return ResponseEntity.ok(service.listarTopicosResolvidos(paginacao));
    }

    @GetMapping("/total_nao_resolvidos")
    @Operation(summary = "Obter total de tópicos não resolvidos",
            description = "Retorna o número total de tópicos que ainda não foram marcados como resolvidos")
    public ResponseEntity<Integer> totalTopicosNaoResolvidos() {
        var total = service.totalTopicosNaoResolvidos();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/total_resolvidos")
    public ResponseEntity<Integer> totalTopicosResolvidos(){
        var total = service.totalTopicosResolvidos();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/sem-respostas")
    @Operation(summary = "Listar tópicos sem respostas")
    public ResponseEntity<PageResponse<DadosListagemTopico>> listarTopicosSemRespostas(
            @PageableDefault(sort = "dataCriacao", direction = Sort.Direction.DESC)
            Pageable paginacao) {
        return ResponseEntity.ok(service.listarTopicosSemRespostas(paginacao));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar tópico")
    @AutorizacaoAtualizarTopico
    public ResponseEntity<DadosDetalhamentoTopico> atualizarTopico(
            @PathVariable Long id,
            @RequestBody @Valid DadosAtualizacaoTopico dados) {

        var topico = service.atualizarTopico(id, dados);
        return ResponseEntity.ok(topico);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar tópico")
    @AutorizacaoApagarTopicos
    public ResponseEntity<Void> deletarTopico(@PathVariable Long id) {
        service.deletarTopico(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/fechar")
    @Operation(summary = "Fechar tópico como resolvido")
    @AutorizacaoFecharTopico
    public ResponseEntity<Void> fecharTopico(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        service.fecharTopico(id, usuarioLogado);
        return ResponseEntity.noContent().build();
    }


    @Operation(
            summary = "Obter estatísticas dos tópicos",
            description = "Retorna métricas detalhadas sobre os tópicos do fórum"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Estatísticas obtidas com sucesso"
    )
    @AutorizacaoVerEstatisticas
    @GetMapping("/estatisticas")
    public ResponseEntity<DadosEstatisticasTopico> obterEstatisticas() {
        return ResponseEntity.ok(service.obterEstatisticas());
    }

    @GetMapping("/recentes")
    @Operation(summary = "Listar tópicos urgentes (sem resposta há mais de 24h)")
    public ResponseEntity<PageResponse<DadosListagemTopico>> listarTopicosUrgentes(
            @PageableDefault(sort = "dataCriacao", direction = Sort.Direction.DESC)
            Pageable paginacao) {
        return ResponseEntity.ok(service.listarTopicosRecentes(paginacao));
    }
}