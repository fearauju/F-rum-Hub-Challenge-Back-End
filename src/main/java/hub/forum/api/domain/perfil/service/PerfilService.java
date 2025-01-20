package hub.forum.api.domain.perfil.service;

import hub.forum.api.domain.perfil.OperacaoPerfil;
import hub.forum.api.domain.perfil.Perfil;
import hub.forum.api.domain.perfil.dto.DadosAtualizacaoPerfil;
import hub.forum.api.domain.perfil.dto.DadosCadastroPerfil;
import hub.forum.api.domain.perfil.dto.DadosDetalhamentoPerfil;
import hub.forum.api.domain.perfil.dto.DadosPerfil;
import hub.forum.api.domain.perfil.repository.PerfilRepository;
import hub.forum.api.domain.perfil.validacao.DadosValidacaoPerfil;
import hub.forum.api.domain.usuario.Usuario;
import hub.forum.api.domain.usuario.repository.UsuarioRepository;
import hub.forum.api.domain.perfil.validacao.ValidadorPerfil;
import hub.forum.api.infra.exceptions.ValidacaoException;
import jakarta.transaction.Transactional;
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
    private List<ValidadorPerfil<DadosValidacaoPerfil>> validadores;

    @Autowired
    private UsuarioRepository usuarioRepository;


    @Transactional
    public DadosDetalhamentoPerfil criarPerfil(DadosCadastroPerfil dados, Long usuarioId, Usuario usuarioLogado) {

        log.info("Iniciando criação de perfil para usuário ID: {}", usuarioId);

        var validacaoDados = new DadosValidacaoPerfil(
                dados.nome(),
                dados.descricaoPessoal(),
                usuarioId,
                usuarioLogado
        );

        validadores.forEach(v -> v.validar(validacaoDados, OperacaoPerfil.CRIACAO));

        var usuarioAlvo = usuarioRepository.findByIdAndAtivoTrue(usuarioId);

        // Se todas as validações passarem, cria o perfil

        var perfil = new Perfil();
        perfil.criarPerfil(dados, usuarioAlvo);
        perfilRepository.save(perfil);
        log.info("Perfil criado com sucesso para usuário ID: {}", usuarioId);
        return new DadosDetalhamentoPerfil(perfil);
    }


    public DadosDetalhamentoPerfil buscarPerfil(Long perfilId) {

        var perfil = perfilRepository.getReferenceById(perfilId);

        return  new DadosDetalhamentoPerfil(perfil);
    }


    public Page<DadosDetalhamentoPerfil> buscarPerfisPorNome(DadosPerfil dados, Pageable paginacao) {

        return perfilRepository.findByListasDoNome(dados.nomePerfil(), paginacao)
                .map(DadosDetalhamentoPerfil::new);
    }


    @Transactional
    public DadosDetalhamentoPerfil atualizar(Long perfilId, DadosAtualizacaoPerfil dados, Usuario usuarioLogado) {

        var validacaoDados = new DadosValidacaoPerfil(
                dados.nome(),
                dados.descricaoPessoal(),
                perfilId,
                usuarioLogado
        );

        validadores.forEach(v -> v.validar(validacaoDados, OperacaoPerfil.ATUALIZACAO));

        var perfil = perfilRepository.getReferenceById(perfilId);

        perfil.atualizarPerfil(dados);

        return new DadosDetalhamentoPerfil(perfil);
    }
}
