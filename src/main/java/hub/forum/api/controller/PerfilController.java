package hub.forum.api.controller;

import hub.forum.api.domain.pefil.DadosAtualizacaoPerfil;
import hub.forum.api.domain.pefil.DadosDetalhamentoPerfil;
import hub.forum.api.domain.pefil.DadosPerfil;
import hub.forum.api.domain.pefil.PerfilService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/perfil")
public class PerfilController {

    @Autowired
    private PerfilService service;

    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoPerfil>> detalharPerfil(@Valid DadosPerfil dados,
                                                                        @PageableDefault(size = 10,
                                                                        sort = {"nome"})
                                                                        Pageable paginacao){

        var perfil = service.listarPefil(dados, paginacao);
        return ResponseEntity.ok(perfil);
    }

    @PutMapping
    @PreAuthorize("ROLE_ADMINISTRADOR")
    public ResponseEntity<DadosDetalhamentoPerfil> atualizarPerfil(@Valid DadosAtualizacaoPerfil dados){

        var perfilAtualizado = service.atualizar(dados);
        return ResponseEntity.ok(perfilAtualizado);
    }
}
