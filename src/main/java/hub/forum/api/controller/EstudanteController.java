package hub.forum.api.controller;

import hub.forum.api.domain.usuario.Usuario;
import hub.forum.api.domain.usuario.estudante.dto.*;
import hub.forum.api.domain.usuario.estudante.service.EstudanteService;
import hub.forum.api.infra.exceptions.ValidacaoException;
import hub.forum.api.infra.security.anotacoes.AutorizacaoAtualizarEstudante;
import hub.forum.api.infra.security.anotacoes.AutorizacaoBuscarEstudante;
import hub.forum.api.infra.security.anotacoes.AutorizacaoCadastrarUsuarios;
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

import java.util.Set;

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
    @PostMapping("/{id}/cadastrar_estudante")
    @AutorizacaoCadastrarUsuarios
    public ResponseEntity<DadosDetalhamentoEstudante> cadastrarEstudante(@PathVariable Long id,
                                                                         @RequestBody @Valid DadosCadastroEstudante dados,
                                                                         UriComponentsBuilder uriBuilder){
        log.info("Iniciando cadastro do estudante");
        var estudanteMatriculado = service.cadastrarEstudante(id,dados);

        log.info("criando a Uri do cadasro do estudante");
        var uri = uriBuilder.path("/estudantes/{id}").buildAndExpand(estudanteMatriculado.id()).toUri();

        log.info("Retornando dados do estudante cadastrado");
        return ResponseEntity.created(uri).body(estudanteMatriculado);
    }

    @Operation(summary = "Lista estudantes matriculados",
            description = "Retorna uma lista paginada dos estudantes matriculados. Somente Administrador e Suporte")
    @GetMapping
    @AutorizacaoBuscarEstudante
    public ResponseEntity<Page<DadosDetalhamentoEstudante>> listarEstudantesMatriculados(
            @PageableDefault(sort = "usuario.perfil.nome") Pageable paginacao,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        log.debug("Listando estudantes matriculados. Solicitado por: {}", usuarioLogado.getLogin());

        var listaEstudantes = service.listarEstudantesMatriculados(paginacao);

        log.info("Retornando {} registros de estudantes", listaEstudantes.getTotalElements());
        return ResponseEntity.ok(listaEstudantes);
    }

    @Operation(summary = "Busca um estudante matriculado",
            description = "Busca estudante pelo id do usuário. Somente Administrador e Suporte pode realizar esta requisição")
    @GetMapping("/{id}")
    @AutorizacaoBuscarEstudante
    public ResponseEntity<DadosDetalhamentoEstudante> buscarEstudante(@AuthenticationPrincipal Usuario usuarioLogado,
                                                                      @PathVariable Long id){

        log.debug("Buscando estudante matriculado. Solicitado por: {}", usuarioLogado.getLogin());

        var estudante = service.buscarEstudante(id);

        log.info("Retornando  registro do estudante {}", estudante.nome());
        return ResponseEntity.ok(estudante);
    }

    @PutMapping("/{id}/atualizar")
    @AutorizacaoAtualizarEstudante
    public ResponseEntity<DadosDetalhamentoEstudante> atualizar(@RequestBody @Valid DadosAtualizacaoEstudante dados,
                                                                @PathVariable Long id){
        log.info("Iniciando atualização dos dados do estudante. Requisição feita pelo estudante com ID {} ", id);
        var estudante = service.atualizar(dados,id);

        log.info("Retornando dados após atualização");
        return ResponseEntity.ok(estudante);
    }

    @GetMapping("/{id}/progresso")
    public ResponseEntity<DadosProgressoEstudante> obterProgresso(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuario) {

        var progresso = service.obterProgresso(id);
        return ResponseEntity.ok(progresso);
    }

    @GetMapping("/{id}/badges")
    public ResponseEntity<Set<DadosDetalhamentoBadges>> listarBadges(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuario) {

        var badges = service.listarBadges(id);
        return ResponseEntity.ok(badges);
    }
}
