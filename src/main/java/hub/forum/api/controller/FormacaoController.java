package hub.forum.api.controller;

import hub.forum.api.domain.formacao.*;
import hub.forum.api.infra.security.anotacoes.AutorizacaoAtualizarFormacao;
import hub.forum.api.infra.security.anotacoes.AutorizacaoCadastrarFormacao;
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
@RequestMapping("/formacoes")
@Slf4j
public class FormacaoController {

   @Autowired
   private FormacaoService service;

    @PostMapping("/cadastrar")
    @AutorizacaoCadastrarFormacao
    public ResponseEntity<DadosDetalhamentoFormacao> cadastrar(
            @RequestBody @Valid DadosCadastroFormacao dados,
            UriComponentsBuilder uriBuilder) {

        log.debug("Cadastrando nova formação");
        var formacao = service.cadastrarFormacao(dados);

        var uri = uriBuilder.path("/formacoes/{id}")
                .buildAndExpand(formacao.getId())
                .toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoFormacao(formacao));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemFormacao>> listarFormacoes(
            @PageableDefault(sort = {"formacao"}) Pageable paginacao) {

        var formacoes = service.listarFormacoes(paginacao);
        return ResponseEntity.ok(formacoes);
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
