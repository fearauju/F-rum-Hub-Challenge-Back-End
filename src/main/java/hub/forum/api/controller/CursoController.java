package hub.forum.api.controller;

import hub.forum.api.domain.curso.dto.DadosAtualizacaoCurso;
import hub.forum.api.domain.curso.dto.DadosDetalhamentoCurso;
import hub.forum.api.domain.curso.dto.DadosListagemCurso;
import hub.forum.api.domain.curso.dto.DadoscadastroCurso;
import hub.forum.api.domain.curso.service.CursoService;
import hub.forum.api.domain.usuario.Usuario;
import hub.forum.api.domain.util.PageResponse;
import hub.forum.api.infra.security.anotacoes.AutorizacaoAtualizarCurso;
import hub.forum.api.infra.security.anotacoes.AutorizacaoCadastrarCurso;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/formacoes/{formacaoId}/cursos")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Cursos", description = "Gerenciamento de cursos de determinada formação")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CursoController {

    @Autowired
    private CursoService service;


    @PostMapping
    @AutorizacaoCadastrarCurso
    public ResponseEntity<DadosDetalhamentoCurso> cadastrarCurso(
            @PathVariable Long formacaoId,
            @RequestBody @Valid DadoscadastroCurso dados,
            UriComponentsBuilder uriBuilder) {

        var curso = service.cadastrarCurso(formacaoId,dados);

        var uri = uriBuilder.path("formacao/cursos/{cursoId}").buildAndExpand(curso.cursoId()).toUri();

        return ResponseEntity.created(uri).body(curso);
    }

    @GetMapping
    @Operation(summary = "Lista cursos por formação",
            description = "Retorna uma lista paginada de cursos pertencentes a uma formação específica")
    public ResponseEntity<PageResponse<DadosListagemCurso>> cursosPorFormacao(
            @PathVariable
            @NotNull(message = "O ID da formação é obrigatório")
            Long formacaoId,
            @PageableDefault(sort = {"curso"}) Pageable paginacao) {

        var cursosPage = service.cursosPorFormacao(paginacao, formacaoId);

        var response = new PageResponse<>(
                cursosPage.getContent(),
                cursosPage.getNumber(),
                cursosPage.getSize(),
                cursosPage.getTotalElements(),
                cursosPage.getTotalPages(),
                cursosPage.isLast()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cursoId}")
    public ResponseEntity<DadosDetalhamentoCurso> detalharCurso(
            @PathVariable Long formacaoId,
            @PathVariable Long cursoId) {
        var curso = service.detalharCurso(formacaoId, cursoId);
        return ResponseEntity.ok(curso);
    }

    @PutMapping("/{cursoId}")
    @AutorizacaoAtualizarCurso
    public ResponseEntity<DadosDetalhamentoCurso> atualizarCurso(
            @PathVariable Long cursoId,
            @RequestBody @Valid DadosAtualizacaoCurso dados,
            @AuthenticationPrincipal Usuario professor) {

        var curso = service.atualizarCurso(cursoId,dados, professor);
        return ResponseEntity.ok(curso);
    }
}
