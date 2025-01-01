package hub.forum.api.controller;

import hub.forum.api.domain.escola.*;
import hub.forum.api.infra.security.anotacoes.AutorizacaoAtualizarEscola;
import hub.forum.api.infra.security.anotacoes.AutorizacaoCadastrarEscola;
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
@Slf4j
public class EscolaController {

    @Autowired
    EscolaService service;

    @PostMapping
    @AutorizacaoCadastrarEscola
    public ResponseEntity<DadosDetalhamentoEscola> cadastrar(
            @RequestBody @Valid DadosCadastroEscola dados,
            UriComponentsBuilder uriBuilder) {

        log.debug("Cadastrando nova escola");
        var escola = service.cadastrarEscola(dados);

        var uri = uriBuilder.path("/escolas/{id}")
                .buildAndExpand(escola.escolaID())
                .toUri();

        return ResponseEntity.created(uri).body(escola);
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemEscola>> listar(
            @PageableDefault(size = 10, sort = {"nomeEscola"}) Pageable paginacao) {
        var page = service.listarEscolas(paginacao);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @AutorizacaoAtualizarEscola
    public ResponseEntity<DadosDetalhamentoEscola> atualizar(
            @RequestBody @Valid DadosAtualizacaoEscola dados) {

        log.debug("Atualizando escola ID: {}", dados.escolaID());
        var escola = service.atualizarDadosEscola(dados);
        return ResponseEntity.ok(escola);
    }
}
