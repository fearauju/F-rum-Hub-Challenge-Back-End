package hub.forum.api.controller;

import hub.forum.api.domain.usuario.Usuario;
import hub.forum.api.domain.usuario.professor.DadosCadastroProfessor;
import hub.forum.api.domain.usuario.professor.DadosDetalhamentoProfessor;
import hub.forum.api.domain.usuario.professor.ProfessorService;
import hub.forum.api.infra.security.anotacoes.AutorizacaoCadastrarUsuarios;
import hub.forum.api.infra.security.anotacoes.AutorizacaoPesquisarPorUsuarios;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
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
@RequestMapping("/professores")
@SecurityRequirement(name = "bearer-key")
@Slf4j
public class ProfessorController {

    @Autowired
    private ProfessorService service;

    @PostMapping("/{id}/cadastrar_professores")
    @Transactional
    @AutorizacaoCadastrarUsuarios
    public ResponseEntity<DadosDetalhamentoProfessor> cadastrarProfessor(@PathVariable Long id,
                                                                         @RequestBody @Valid DadosCadastroProfessor dados,
                                                                         UriComponentsBuilder UriBuilder){
        var professor = service.cadastrarProfessor(id, dados);
        var uri = UriBuilder.path("/cadastrar_professores/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(uri).body(professor);
    }

    @Operation(summary = "Lista equipe de professores",
            description = "Retorna uma lista paginada dos professores")
    @GetMapping("/professores")
    @AutorizacaoPesquisarPorUsuarios
    public ResponseEntity<Page<DadosDetalhamentoProfessor>> listarEquipeProfessores(
            @PageableDefault(sort = "perfil.nome") Pageable paginacao,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        log.debug("Listando equipe de professores. Solicitado por: {}", usuarioLogado.getLogin());

        var listaProfessores = service.listarEquipeProfessores(paginacao);
        log.info("Retornando {} registros de professores", listaProfessores.getTotalElements());

        return ResponseEntity.ok(listaProfessores);
    }
}
