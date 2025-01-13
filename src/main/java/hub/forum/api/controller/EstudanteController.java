package hub.forum.api.controller;

import hub.forum.api.domain.usuario.Usuario;
import hub.forum.api.domain.usuario.estudante.DadosCadastroEstudante;
import hub.forum.api.domain.usuario.estudante.DadosDetalhamentoEstudante;
import hub.forum.api.domain.usuario.estudante.EstudanteService;
import hub.forum.api.infra.security.anotacoes.AutorizacaoCadastrarUsuarios;
import hub.forum.api.infra.security.anotacoes.AutorizacaoPesquisarPorUsuarios;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/estudantes")
@SecurityRequirement(name = "bearer-key")
@Slf4j
public class EstudanteController {

    @Autowired
    private EstudanteService service;

    @Operation(summary = "cadastrar estudante",
            description = "Cadastrar dados dos estudantes e da matricula")
    @ApiResponse(responseCode = "403", description = "Sem premissão para cadastrar estudantes")
    @PostMapping("/{id}/cadastro_estudantes")
    @AutorizacaoCadastrarUsuarios
    public ResponseEntity<DadosDetalhamentoEstudante> cadastrarEstudante(@PathVariable Long id,
                                                                         @RequestBody @Valid DadosCadastroEstudante dados,
                                                                         UriComponentsBuilder uriBuilder){

        var estudanteMatriculado = service.cadastrarEstudante(id,dados);
        var uri = uriBuilder.path("/cadastro_estudantes/{id}").buildAndExpand(id).toUri();

        return ResponseEntity.created(uri).body(estudanteMatriculado);
    }

    @Operation(summary = "Lista estudantes matriculados",
            description = "Retorna uma lista paginada dos estudantes matriculados")
    @GetMapping("/estudantes")
    @AutorizacaoPesquisarPorUsuarios
    public ResponseEntity<Page<DadosDetalhamentoEstudante>> listarEstudantesMatriculados(
            @PageableDefault(sort = "perfil.nome") Pageable paginacao,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        log.debug("Listando estudantes matriculados. Solicitado por: {}", usuarioLogado.getLogin());

        var listaEstudantes = service.listarEstudantesMatriculados(paginacao);
        log.info("Retornando {} registros de estudantes", listaEstudantes.getTotalElements());

        return ResponseEntity.ok(listaEstudantes);
    }
}
