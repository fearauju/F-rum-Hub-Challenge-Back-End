package hub.forum.api.controller;

import hub.forum.api.domain.escola.*;
import hub.forum.api.infra.security.anotacoes.AutorizacaoAtualizarEscola;
import hub.forum.api.infra.security.anotacoes.AutorizacaoCadastrarEscola;
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
@Slf4j
@Tag(name = "Escolas", description = "Gerenciamento de escolas")
public class EscolaController {

    @Autowired
   private EscolaService service;

    @PostMapping
    @AutorizacaoCadastrarEscola
    public ResponseEntity<DadosDetalhamentoCadastroEscola> cadastrar(
            @RequestBody @Valid DadosCadastroEscola dados,
            UriComponentsBuilder uriBuilder) {
        var escola = service.cadastrarEscola(dados);

        var uri = uriBuilder.path("/escolas/{id}")
                .buildAndExpand(escola.escolaID())
                .toUri();

        return ResponseEntity.created(uri).body(escola);
    }

    @GetMapping()
    public ResponseEntity<Page<DadosListagemEscola>> listar(
            @PageableDefault(sort = {"nomeEscola"}) Pageable paginacao) {
        var page = service.listarEscolas(paginacao);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    @AutorizacaoAtualizarEscola
    public ResponseEntity<DadosDetalhamentoEscola> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DadosAtualizacaoEscola dados) {
        var escola = service.atualizarDadosEscola(id, dados);
        return ResponseEntity.ok(escola);
    }
}
