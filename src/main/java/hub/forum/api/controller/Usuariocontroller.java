package hub.forum.api.controller;

import hub.forum.api.domain.matricula.DadosDetalhamentoMatricula;
import hub.forum.api.domain.matricula.DadosEstudanteMatriculado;
import hub.forum.api.domain.matricula.MatriculaService;
import hub.forum.api.domain.usuario.DadosExclusaoUsuario;
import hub.forum.api.domain.usuario.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController()
@RequestMapping("/usuarios")
public class Usuariocontroller {

    @Autowired
    private UsuarioService usuarioService;

    private MatriculaService matriculaService;

    @PutMapping("/inativar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> inativarUsuario(@RequestBody @Valid DadosExclusaoUsuario dados) {
        usuarioService.inativarUsuario(dados);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<DadosDetalhamentoMatricula> atualizarMatriculaEstudante(@Valid DadosEstudanteMatriculado dados,
                                                                                  UriComponentsBuilder uriComponentsBuilder){

        var dadosMatricula = matriculaService.atualizarMatriculaEstudante(dados);

        var uri = uriComponentsBuilder.path("/usuarios/{cursoID}").buildAndExpand(dadosMatricula.estudante_id()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoMatricula(dadosMatricula));
    }
}
