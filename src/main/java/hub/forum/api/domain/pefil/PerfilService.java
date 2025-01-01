package hub.forum.api.domain.pefil;

import hub.forum.api.domain.usuario.UsuarioRepository;
import hub.forum.api.infra.exceptions.ValidacaoException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PerfilService {

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Page<DadosDetalhamentoPerfil> listarPefil(@Valid DadosPerfil dados, Pageable paginacao) {

        return perfilRepository.findByNome(dados.nomePerfil(), paginacao).map(DadosDetalhamentoPerfil::new);
    }


    @Transactional
    public DadosDetalhamentoPerfil atualizar(@Valid DadosAtualizacaoPerfil dados) {

        //validações feitas pela anotação posteriormente

        var usuario = usuarioRepository.findById(dados.usuario_id())
                .orElseThrow(() -> {
                        log.error("Usuário não encontrado");
                       return new ValidacaoException("Usuário não encontrado");});

        var perfil = new Perfil();
        perfil.atualizarPefil(dados);

        return new DadosDetalhamentoPerfil(perfil);
    }
}
