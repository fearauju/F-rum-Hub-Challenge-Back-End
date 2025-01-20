package hub.forum.api.controller;

import hub.forum.api.domain.matricula.dto.DadosDetalhamentoMatricula;
import hub.forum.api.domain.matricula.service.MatriculaService;
import hub.forum.api.domain.usuario.Usuario;
import hub.forum.api.domain.usuario.estudante.Estudante;
import hub.forum.api.infra.security.anotacoes.AutorizacaoInscreverCurso;
import hub.forum.api.infra.security.anotacoes.AutorizacaoRenovarMatricula;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matriculas")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Matricula", description = "Gerenciamento da matricula do estudante")
@Slf4j
public class MatriculaController {

    @Autowired
    private MatriculaService service;

    @PutMapping("{id}/renovar_matricula") // Melhor prática: RESTful
    @Operation(summary = "Renova matrícula",
            description = "Renova a matrícula de um estudante por mais um ano")
    @AutorizacaoRenovarMatricula
    public ResponseEntity<DadosDetalhamentoMatricula> renovarMatriculaEstudante(@PathVariable Long id){

        log.info("Iniciando renovação de matrícula para o estudante com ID: {}", id);
        var dadosMatricula = service.renovarMatriculaEstudante(id);

        log.info("retornando dados da matricula renovada");
        return ResponseEntity.ok(dadosMatricula);
    }

    @PostMapping("/{cursoId}/inscrever")
    @AutorizacaoInscreverCurso
    public ResponseEntity<Void> inscreverEmCurso(
            @PathVariable Long cursoId,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        service.inscreverEmCurso(cursoId,usuarioLogado);
        return ResponseEntity.noContent().build();
    }
}
