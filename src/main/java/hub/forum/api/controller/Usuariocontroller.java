package hub.forum.api.controller;

import hub.forum.api.domain.usuario.*;
import hub.forum.api.infra.exceptions.ValidacaoException;
import hub.forum.api.infra.security.anotacoes.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;


@RestController()
@RequestMapping("/usuarios")
@Tag(name = "Controller do usuário", description = "Endpoints para receber requisições referentes ao usuário e retornar uma resposta da Api")
@Slf4j
public class Usuariocontroller {

    @Autowired
    private UsuarioService service;

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

    @PostMapping("/{id}/cadastrar_professores")
    @AutorizacaoCadastrarUsuarios
    public ResponseEntity<DadosDetalhamentoProfessor> cadastrarProfessor(@PathVariable Long id,
                                                                        @RequestBody @Valid DadosCadastroProfessor dados,
                                                                         UriComponentsBuilder UriBuilder){
        var professor = service.cadastrarProfessor(id, dados);
        var uri = UriBuilder.path("/cadastrar_professores/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(uri).body(professor);
    }

    @PostMapping("/{id}/cadastrar_suporte")
    public ResponseEntity<DadosDetalhamentoSuporte> cadastrarSuporte(@PathVariable Long id,
                                                                     @RequestBody @Valid DadosCadastroSuporte dados,
                                                                     UriComponentsBuilder uriBuilder){
        var suporte = service.cadastrarSuporte(id, dados);
        var uri = uriBuilder.path("/cadastrar_suporte/{id}").buildAndExpand(id).toUri();

        return ResponseEntity.created(uri).body(suporte);
    }


    @Operation(summary = "Inativa um usuário",
            description = "Inativa um usuário do sistema. Apenas administradores podem realizar esta operação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário inativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para inativar usuários")
    })

    @PutMapping("/inativar")
    @AutorizacaoInativarUsuarios
    public ResponseEntity<Void> inativarUsuario(
            @RequestBody @Valid DadosExclusaoUsuario dados,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        log.info("Iniciando processo de inativação do usuário ID: {} pelo usuário: {}",
                dados.usuarioInativoID(), usuarioLogado.getLogin());

        try {
            service.inativarUsuario(dados);
            log.info("Usuário ID: {} inativado com sucesso", dados.usuarioInativoID());
            return ResponseEntity.noContent().build();
        } catch (ValidacaoException e) {
            log.error("Erro ao inativar usuário: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


    @Operation(summary = "Desbloqueia conta de usuário",
            description = "Desbloqueia a conta de um usuário que foi bloqueada por tentativas de login.")
    @PutMapping("/desbloquear")
    @AutorizacaoDesbloquearConta
    public ResponseEntity<Void> desbloquearConta(
            @Valid DadosDesbloquearConta dados,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        log.info("Iniciando desbloqueio de conta ID: {} pelo usuário: {}",
                dados.usuarioID(), usuarioLogado.getLogin());

        service.desbloquearConta(dados.usuarioID());
        log.info("Conta ID: {} desbloqueada com sucesso", dados.usuarioID());

        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Atualiza dados de login",
            description = "Permite que um usuário atualize seu login e/ou senha.")
    @PutMapping("{id}/atualizar")
    @AutorizacaoAtualizarDadosLogin
    public ResponseEntity<String> atualizarLogin(
            @PathVariable Long id,
            @Valid @RequestBody DadosAtualizacaoLogin dados) {

        log.info("Iniciando atualização de dados de login para usuário ID: {}", id);

        try {

            service.atualizarDadosLogin(id, dados);
            log.info("Dados de login atualizados com sucesso para usuário ID: {}", id);

            return ResponseEntity.ok("Dados atualizados com sucesso");

        } catch (ValidacaoException e) {
            log.warn("Erro de validação ao atualizar dados: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {
            log.error("Erro ao atualizar dados: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Erro ao processar a requisição");
        }
    }


    @Operation(summary = "Lista equipe de suporte",
            description = "Retorna uma lista paginada da equipe de suporte")
    @GetMapping("/suportes")
    @AutorizacaoPesquisarPorUsuarios
    public ResponseEntity<Page<DadosDetalhamentoSuporte>> listarEquipeSuporte(
            @PageableDefault(sort = "perfil.nome") Pageable paginacao,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        log.debug("Listando equipe de suporte. Solicitado por: {}", usuarioLogado.getLogin());

        var listaSuporte = service.listarEquipeSuporte(paginacao);
        log.info("Retornando {} registros de suporte", listaSuporte.getTotalElements());

        return ResponseEntity.ok(listaSuporte);
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
