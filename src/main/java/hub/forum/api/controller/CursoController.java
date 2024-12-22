package hub.forum.api.controller;

import hub.forum.api.domain.curso.Curso;
import hub.forum.api.domain.curso.CursoRepository;
import hub.forum.api.domain.curso.DadosDetalhamentoCurso;
import hub.forum.api.domain.curso.DadoscadastroCurso;
import hub.forum.api.domain.formacao.FormacaoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    CursoRepository cursoRepositoryrepository;

    @Autowired
    FormacaoRepository formacaoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoCurso> cadastrar(@RequestBody @Valid DadoscadastroCurso dados, UriComponentsBuilder uriComponentsBuilder){

        var formacao = formacaoRepository.getReferenceById(dados.formacao_id());

        var curso = new Curso(dados, formacao);
        cursoRepositoryrepository.save(curso);

        var uri = uriComponentsBuilder.path("/cursos/{id}").buildAndExpand(curso.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoCurso(curso));
    }
}
