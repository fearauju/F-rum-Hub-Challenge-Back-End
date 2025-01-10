package hub.forum.api.controller;

import hub.forum.api.domain.perfil.*;
import hub.forum.api.infra.security.anotacoes.AutorizacaoAtualizarPerfil;
import hub.forum.api.infra.security.anotacoes.AutorizacaoCriarPerfil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/perfil")
public class PerfilController {

    @Autowired
    private PerfilService service;

    @PostMapping("/{id}")
    @AutorizacaoCriarPerfil
    public ResponseEntity<DadosDetalhamentoPerfil> criarPerfil(@RequestBody @Valid DadosCadastroPerfil dados,
                                                               @PathVariable Long id,
                                                               UriComponentsBuilder uriBuilder){

        var perfil = service.criarPerfil(dados, id);
        var uri = uriBuilder.path("/perfis/{id}").buildAndExpand(perfil.id()).toUri();

        return ResponseEntity.created(uri).body(perfil);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoPerfil> buscarPerfilUsuario(@PathVariable Long id){
        var perfil = service.buscarPerfil(id);

        return ResponseEntity.ok(perfil);
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<DadosDetalhamentoPerfil>> detalharPerfil(@Valid DadosPerfil dados,
                                                                        @PageableDefault(sort = {"nome"})
                                                                        Pageable paginacao){

        var perfil = service.listarPefil(dados, paginacao);
        return ResponseEntity.ok(perfil);
    }

    @PutMapping("/{perfilID}")  // Melhor prática: incluir ID na URL
    @AutorizacaoAtualizarPerfil
    public ResponseEntity<DadosDetalhamentoPerfil> atualizarPerfil(
            @PathVariable Long perfilID,
            @Valid @RequestBody DadosAtualizacaoPerfil dados) {

        var perfilAtualizado = service.atualizar(perfilID,dados);
        return ResponseEntity.ok(perfilAtualizado);
    }
}
