package hub.forum.api.controller;

import hub.forum.api.domain.curso.*;
import hub.forum.api.infra.security.anotacoes.AutorizacaoAtualizarCurso;
import hub.forum.api.infra.security.anotacoes.AutorizacaoCadastrarCurso;
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
@RequestMapping("/formacoes/{formacaoId}/cursos")
@Slf4j
public class CursoController {

    @Autowired
    private CursoService service;


    @PostMapping
    @AutorizacaoCadastrarCurso
    public ResponseEntity<DadosDetalhamentoCurso> cadastrar(
            @PathVariable Long formacaoId,
            @RequestBody @Valid DadoscadastroCurso dados,
            UriComponentsBuilder uriBuilder) {

        var curso = service.cadastrarCurso(formacaoId,dados);

        var uri = uriBuilder.path("formacao/cursos/{cursoID}").buildAndExpand(curso.cursoID()).toUri();

        return ResponseEntity.created(uri).body(curso);
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemCurso>> cursosPorFormacao(
            @PathVariable Long formacaoID,
            @PageableDefault(sort = {"curso"}) Pageable paginacao) {

        var cursos = service.cursosPorFormacao(paginacao, formacaoID);
        return ResponseEntity.ok(cursos);
    }


    @PutMapping("/{id}")
    @AutorizacaoAtualizarCurso
    public ResponseEntity<DadosDetalhamentoCurso> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DadosAtualizacaoCurso dados) {

        var curso = service.atualizarCurso(id,dados);
        return ResponseEntity.ok(curso);
    }
}
