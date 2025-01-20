package hub.forum.api.controller;


import hub.forum.api.domain.usuario.professor.dto.DadosAtualizacaoProfessor;
import hub.forum.api.domain.usuario.professor.dto.DadosCadastroProfessor;
import hub.forum.api.domain.usuario.professor.dto.DadosDetalhamentoProfessor;
import hub.forum.api.domain.usuario.professor.service.ProfessorService;
import hub.forum.api.domain.util.PageResponse;
import hub.forum.api.infra.security.anotacoes.AutorizacaoAtualizarProfessor;
import hub.forum.api.infra.security.anotacoes.AutorizacaoCadastrarUsuarios;
import hub.forum.api.infra.security.anotacoes.AutorizacaoPesquisarPorUsuarios;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/professores")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Professor", description = "Gerenciamento dos professores")
@Slf4j
public class ProfessorController {

    @Autowired
    private ProfessorService service;

    @Operation(summary = "Cadastrar professor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Professor cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para cadastrar professor"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PostMapping("/{id}/cadastrar_professor")
    @AutorizacaoCadastrarUsuarios
    public ResponseEntity<DadosDetalhamentoProfessor> cadastrarProfessor(@PathVariable Long id,
                                                                         @RequestBody @Valid DadosCadastroProfessor dados,
                                                                         UriComponentsBuilder UriBuilder){
        var professor = service.cadastrarProfessor(id, dados);
        var uri = UriBuilder.path("/professores/{id}").buildAndExpand(professor.id()).toUri();
        return ResponseEntity.created(uri).body(professor);
    }

    @Operation(summary = "Listar professores")
    @GetMapping
    @AutorizacaoPesquisarPorUsuarios
    public ResponseEntity<PageResponse<DadosDetalhamentoProfessor>> listarEquipeProfessores(
            @PageableDefault(sort = "usuario.perfil.nome") Pageable paginacao) {
        return ResponseEntity.ok(service.listarEquipeProfessores(paginacao));
    }

    @PutMapping("{id}/atualizar")
    @AutorizacaoAtualizarProfessor
    public ResponseEntity<DadosDetalhamentoProfessor> atualizarCadastro(@RequestBody @Valid DadosAtualizacaoProfessor dados,
                                                                        @PathVariable Long id){

        log.info("Iniciando atualização dos dados do professor");

        var professor = service.atualizarCadastro(dados, id);
        return ResponseEntity.ok(professor);
    }
}
