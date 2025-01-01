package hub.forum.api.controller;

import hub.forum.api.domain.curso.*;
import hub.forum.api.infra.security.anotacoes.PodeAtualizarCurso;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/cursos")
@Slf4j
public class CursoController {

    @Autowired
    private CursoService service;


    @PostMapping
    @PreAuthorize("ROLE_ADMINISTRADOR")
    public ResponseEntity<DadosDetalhamentoCurso> cadastrar(@RequestBody @Valid DadoscadastroCurso dados, UriComponentsBuilder uriComponentsBuilder){

        var curso = service.cadastrarCurso(dados);

        var uri = uriComponentsBuilder.path("/cursos/{id}").buildAndExpand(curso.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoCurso(curso));
    }

    @GetMapping("/formacao/{formacaoId}")
    public ResponseEntity<Page<DadosListagemCurso>> listarPorFormacao(
            @PathVariable Long formacaoId,
            @PageableDefault(size = 10, sort = {"curso"}) Pageable paginacao) {

        var cursos = service.listarPorFormacao(paginacao, formacaoId);
        return ResponseEntity.ok(cursos);
    }


    @PutMapping
    @PodeAtualizarCurso
    public ResponseEntity<DadosDetalhamentoCurso> atualizarCurso(@Valid DadosAtualizacaoCurso dados){

        var curso = service.atualizarCurso(dados);
        return ResponseEntity.ok(curso);
    }

    //inscrição no curso
    // aumenta o atributo total de alunos (PUT)
    // exibe curso no perfil (GET)
    //é exibida a lista de curso (GET)
    //ao clicar em inscrever-se em um curso atualiza a quantidade de estudante daquele curso específico (PUT)
}
