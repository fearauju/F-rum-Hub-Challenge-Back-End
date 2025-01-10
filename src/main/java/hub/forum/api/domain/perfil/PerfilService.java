package hub.forum.api.domain.perfil;

import hub.forum.api.domain.perfil.validacao.DadosValidacaoPerfil;
import hub.forum.api.domain.usuario.UsuarioRepository;
import hub.forum.api.domain.validacao.ValidadorBase;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class PerfilService {

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private List<ValidadorBase<DadosValidacaoPerfil>> validadores;

    @Autowired
    private UsuarioRepository usuarioRepository;


    @Transactional
    public DadosDetalhamentoPerfil criarPerfil( DadosCadastroPerfil dados, Long usuarioID) {

        System.out.println("Usuário ID recebido: " + usuarioID);

        var validacaoDados = new DadosValidacaoPerfil(
                dados.nome(),
                dados.dataNascimento(),
                dados.descricaoPessoal(),
                usuarioID
        );

        validadores.forEach(v-> v.validar(validacaoDados));

        var usuario = usuarioRepository.findByIdAndAtivoTrue(usuarioID);

        var perfil = new Perfil();
        perfil.criarPerfil(validacaoDados, usuario);

        perfilRepository.save(perfil);

        return new DadosDetalhamentoPerfil(perfil);
    }


    public DadosDetalhamentoPerfil buscarPerfil(Long perfilID) {

        var perfil = perfilRepository.getReferenceById(perfilID);

        return  new DadosDetalhamentoPerfil(perfil);
    }


    public Page<DadosDetalhamentoPerfil> listarPefil(@Valid DadosPerfil dados, Pageable paginacao) {

        return perfilRepository.findByListasDoNome(dados.nomePerfil(), paginacao).map(DadosDetalhamentoPerfil::new);
    }


    @Transactional
    public DadosDetalhamentoPerfil atualizar(Long perfilID, @Valid DadosAtualizacaoPerfil dados) {

        var validacaoDados = new DadosValidacaoPerfil(
                dados.nome(),
                dados.dataNascimento(),
                dados.descricaoPessoal(),
                perfilID
        );

        validadores.forEach(v -> v.validar(validacaoDados));

        var perfil = perfilRepository.getReferenceById(perfilID);

        perfil.atualizarPerfil(dados);

        return new DadosDetalhamentoPerfil(perfil);
    }
}
