package hub.forum.api.controller;

import hub.forum.api.domain.perfil.dto.DadosAtualizacaoPerfil;
import hub.forum.api.domain.perfil.dto.DadosCadastroPerfil;
import hub.forum.api.domain.perfil.dto.DadosDetalhamentoPerfil;
import hub.forum.api.domain.perfil.dto.DadosPerfil;
import hub.forum.api.domain.perfil.service.PerfilService;
import hub.forum.api.domain.usuario.Usuario;
import hub.forum.api.domain.util.PageResponse;
import hub.forum.api.infra.security.anotacoes.AutorizacaoAtualizarPerfil;
import hub.forum.api.infra.security.anotacoes.AutorizacaoCriarPerfil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/perfil")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Perfil", description = "Gerenciamento de perfil do usuário")
@Slf4j
@RequiredArgsConstructor
@Validated
public class PerfilController {

    @Autowired
    private PerfilService service;

    @PostMapping("/{id}")
    @AutorizacaoCriarPerfil
    public ResponseEntity<DadosDetalhamentoPerfil> criarPerfil(@RequestBody @Valid DadosCadastroPerfil dados,
                                                               @PathVariable Long id,
                                                               UriComponentsBuilder uriBuilder,
                                                               @AuthenticationPrincipal Usuario usuarioLogado){

        var perfil = service.criarPerfil(dados, id, usuarioLogado);
        var uri = uriBuilder.path("/perfis/{id}").buildAndExpand(perfil.id()).toUri();

        return ResponseEntity.created(uri).body(perfil);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoPerfil> buscarPerfilUsuario(@PathVariable Long id){
        var perfil = service.buscarPerfil(id);

        return ResponseEntity.ok(perfil);
    }

    @GetMapping("/buscar")
    public ResponseEntity<PageResponse<DadosDetalhamentoPerfil>> buscarPerfisPorNome(
            //É uma boa prática utilizar parâmetros de consulta (@RequestParam)
            // em requisições HTTP GET, ao invés de utilizar o corpo da requisição (@RequestBody).
            // Isso melhora a compatibilidade e aderência aos padrões RESTful.
            @RequestParam("nomePerfil")
            @NotBlank(message = "Informe o nome do perfil do usuário")
            @Pattern(regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ ]+$", message = "O nome deve conter apenas letras e espaços")
            String nomePerfil,
            @PageableDefault(sort = {"nome"}) Pageable paginacao) {

        Page<DadosDetalhamentoPerfil> perfisPage = service.buscarPerfisPorNome(new DadosPerfil(nomePerfil), paginacao);
        PageResponse<DadosDetalhamentoPerfil> response = new PageResponse<>(
                perfisPage.getContent(),
                perfisPage.getNumber(),
                perfisPage.getSize(),
                perfisPage.getTotalElements(),
                perfisPage.getTotalPages(),
                perfisPage.isLast()
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{perfilId}")  // Melhor prática: incluir ID na URL
    @AutorizacaoAtualizarPerfil
    public ResponseEntity<DadosDetalhamentoPerfil> atualizarPerfil(
            @PathVariable Long perfilId,
            @RequestBody @Valid DadosAtualizacaoPerfil dados,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        var perfilAtualizado = service.atualizar(perfilId,dados, usuarioLogado);
        return ResponseEntity.ok(perfilAtualizado);
    }
}
