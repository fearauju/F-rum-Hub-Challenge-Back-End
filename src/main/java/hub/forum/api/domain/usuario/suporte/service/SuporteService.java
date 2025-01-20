package hub.forum.api.domain.usuario.suporte.service;

import hub.forum.api.domain.usuario.OperacaoDominio;
import hub.forum.api.domain.usuario.repository.UsuarioRepository;
import hub.forum.api.domain.usuario.suporte.dto.DadosAtualizacaoSuporte;
import hub.forum.api.domain.usuario.suporte.Suporte;
import hub.forum.api.domain.usuario.suporte.repository.SuporteRepository;
import hub.forum.api.domain.usuario.suporte.dto.DadosCadastroSuporte;
import hub.forum.api.domain.usuario.suporte.dto.DadosDetalhamentoSuporte;
import hub.forum.api.domain.usuario.suporte.validacao.DadosValidacaoSuporte;
import hub.forum.api.domain.usuario.suporte.validacao.ValidadorSuporte;
import hub.forum.api.domain.usuario.validacao.DadosValidacaoUsuario;
import hub.forum.api.domain.usuario.validacao.ValidadorBase;
import hub.forum.api.domain.util.PageResponse;
import hub.forum.api.infra.exceptions.ValidacaoException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SuporteService {

    @Autowired
    private SuporteRepository suporteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    @Qualifier("validadorUsuarioExistente")
    private ValidadorBase<DadosValidacaoUsuario> validadorBase;

    @Autowired
    private List<ValidadorSuporte> validadores;

    @Transactional
    public DadosDetalhamentoSuporte cadastrarSuporte(Long usuarioId, @Valid DadosCadastroSuporte dados) {

        log.info("Iniciando cadastro de suporte para usuário ID: {}", usuarioId);

        // Valida existência do usuário
        validadorBase.validar(
                new DadosValidacaoUsuario(usuarioId, null, null, null, null, OperacaoDominio.CADASTRO_SUPORTE),
                OperacaoDominio.CADASTRO_SUPORTE
        );

        var dadosValidacao = new DadosValidacaoSuporte(
                usuarioId,
                dados.especializacoes(),
                dados.turnoTrabalho(),
                dados.dataAdmissao()
        );

        validadores.forEach(v -> v.validar(dadosValidacao));

        var usuario = usuarioRepository.getReferenceById(usuarioId);
        var suporte = new Suporte();
        suporte.setUsuario(usuario);
        suporte.cadastrarSuporte(dados);

        suporteRepository.save(suporte);

        log.info("Suporte cadastrado com sucesso: ID {}", suporte.getId());
        return new DadosDetalhamentoSuporte(suporte);
    }

    public PageResponse<DadosDetalhamentoSuporte> listarEquipeSuporte(Pageable paginacao) {
        var page = suporteRepository.listaUsuariosSuporte(paginacao)
                .map(DadosDetalhamentoSuporte::new);
        return new PageResponse<>(page);
    }

    @Transactional
    public DadosDetalhamentoSuporte atualizarSuporte(@Valid DadosAtualizacaoSuporte dados, Long usuarioId) {


        if (dados.turnoTrabalho() == null && dados.especializacoes() == null) {
            throw new ValidacaoException("Nenhum dado foi fornecido para atualização");
        }

        // Buscar o suporte existente
        var suporte = suporteRepository.findById(usuarioId)
                .orElseThrow(() -> new ValidacaoException("Suporte não encontrado"));

        // Atualizar os dados
        suporte.atualizarSuporte(dados);
        suporteRepository.save(suporte);

        log.info("Suporte atualizado com sucesso: {}", suporte);
        return new DadosDetalhamentoSuporte(suporte);
    }
}
