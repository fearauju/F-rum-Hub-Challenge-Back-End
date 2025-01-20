package hub.forum.api.controller;

import hub.forum.api.domain.formacao.dto.DadosAtualizacaoFormacao;
import hub.forum.api.domain.formacao.dto.DadosCadastroFormacao;
import hub.forum.api.domain.formacao.dto.DadosDetalhamentoFormacao;
import hub.forum.api.domain.formacao.dto.DadosListagemFormacao;
import hub.forum.api.domain.formacao.service.FormacaoService;
import hub.forum.api.domain.util.PageResponse;
import hub.forum.api.infra.security.anotacoes.AutorizacaoAtualizarFormacao;
import hub.forum.api.infra.security.anotacoes.AutorizacaoCadastrarFormacao;
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
@RequestMapping("/formacoes")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Formações", description = "Gerenciamento de formações das Escolas")
@Slf4j
public class FormacaoController {

   @Autowired
   private FormacaoService service;

    @PostMapping("/cadastrar_formacao")
    @AutorizacaoCadastrarFormacao
    public ResponseEntity<DadosDetalhamentoFormacao> cadastrarFormacao(
            @RequestBody @Valid DadosCadastroFormacao dados,
            UriComponentsBuilder uriBuilder) {

        log.debug("Cadastrando nova formação");
        var formacao = service.cadastrarFormacao(dados);

        var uri = uriBuilder.path("/formacoes/{id}")
                .buildAndExpand(formacao.id())
                .toUri();

        return ResponseEntity.created(uri).body(formacao);
    }

    @GetMapping
    public ResponseEntity<PageResponse<DadosListagemFormacao>> listarFormacoes(
            @PageableDefault(sort = {"formacao"}) Pageable paginacao) {

        var formacoes = service.listarFormacoes(paginacao);
        return ResponseEntity.ok(formacoes);
    }

    @GetMapping("/escola/{escolaId}")
    public ResponseEntity<PageResponse<DadosListagemFormacao>> listarFormacoesPorEscola(
            @PathVariable Long escolaId,
            @PageableDefault( sort = {"formacao"}) Pageable paginacao) {

        var formacoes = service.listarFormacoesPorEscola(escolaId, paginacao);
        return ResponseEntity.ok(formacoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoFormacao> detalhar(@PathVariable Long id) {
        var formacao = service.detalharFormacao(id);
        return ResponseEntity.ok(formacao);
    }

    @PutMapping(("/{id}"))
    @AutorizacaoAtualizarFormacao
    public ResponseEntity<DadosDetalhamentoFormacao> atualizar(
            @RequestBody @Valid DadosAtualizacaoFormacao dados, @PathVariable Long id) {

        log.debug("Atualizando formação com ID: {}", id);
        var formacao = service.atualizarFormacao(dados, id);
        return ResponseEntity.ok(formacao);
    }
}
