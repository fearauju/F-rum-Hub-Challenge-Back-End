package hub.forum.api.controller;

import hub.forum.api.domain.curso.dto.DadosInscricaoCurso;
import hub.forum.api.domain.matricula.DadosDetalhamentoMatricula;
import hub.forum.api.domain.matricula.MatriculaService;
import hub.forum.api.infra.security.anotacoes.AutorizacaoInscreverCurso;
import hub.forum.api.infra.security.anotacoes.AutorizacaoRenovarMatricula;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matriculas")
@Slf4j
public class MatriculaController {

    @Autowired
    private MatriculaService service;

    @GetMapping("/{id}")  // Melhor prática: usar path variable
    public ResponseEntity<DadosDetalhamentoMatricula> detalharMatricula(
            @PathVariable Long id) {

        log.debug("buscando dados da matricula do estudante com id {}", id);
        var matriculaEstudante = service.buscarMatriculaEstudante(id);

        return ResponseEntity.ok(matriculaEstudante);
    }

    @PutMapping("{id}/renovar") // Melhor prática: RESTful
    @Operation(summary = "Renova matrícula",
            description = "Renova a matrícula de um estudante por mais um ano")
    @AutorizacaoRenovarMatricula
    public ResponseEntity<DadosDetalhamentoMatricula> renovarMatriculaEstudante(@PathVariable Long id){

        log.debug("Iniciando renovação de matrícula para estudante ID: {}", id);
        var dadosMatricula = service.renovarMatriculaEstudante(id);

        return ResponseEntity.ok(dadosMatricula);
    }

    @PostMapping("/cursos")
    @AutorizacaoInscreverCurso
    public ResponseEntity<Void> inscreverEmCurso(
            @RequestBody @Valid DadosInscricaoCurso dados) {

        service.inscreverEmCurso(dados);
        return ResponseEntity.noContent().build();
    }
}
