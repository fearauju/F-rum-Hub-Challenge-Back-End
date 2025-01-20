package hub.forum.api.domain.escola.service;

import hub.forum.api.domain.escola.Escola;
import hub.forum.api.domain.escola.dto.*;
import hub.forum.api.domain.escola.repository.EscolaRepository;
import hub.forum.api.domain.escola.validacao.DadosValidacaoEscola;
import hub.forum.api.domain.escola.validacao.ValidadorEscola;
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
public class EscolaService {

    @Autowired
    private EscolaRepository escolaRepository;

    @Autowired
    private List<ValidadorEscola > validadores;

    @Transactional
    public DadosDetalhamentoCadastroEscola cadastrarEscola(DadosCadastroEscola dados) {

        var validacaoDados = new DadosValidacaoEscola(
                dados.nomeEscola(),
                dados.areaFormacao()
        );

        validadores.forEach(v-> v.validar(validacaoDados));

        var escola = new Escola();
        escola.cadastrarEscola(dados);

        log.info("Cadastrando nova escola: {}", dados.nomeEscola());
        escolaRepository.save(escola);

        return new DadosDetalhamentoCadastroEscola(escola);
    }

    public Page<DadosListagemEscola> listarEscolas(Pageable paginacao) {
        return escolaRepository.findAll(paginacao).map(DadosListagemEscola::new);
    }

    @Transactional
    public DadosDetalhamentoEscola atualizarDadosEscola(
            Long escolaId, DadosAtualizacaoEscola dados) {

        if(dados.areaFormacao() == null && dados.nomeEscola() == null){
            throw new ValidacaoException("Nenhum dado foi fornecido para atualização");
        }
        var validacaoDados = new DadosValidacaoEscola(
                dados.nomeEscola(),
                dados.areaFormacao()
        );

        validadores.forEach(v-> v.validar(validacaoDados));

        var escola = escolaRepository.getReferenceById(escolaId);

        if(escola == null){
            throw  new ValidacaoException("Escola não encontrada");
        }

        escola.atualizarInformacoes(dados);

        escolaRepository.save(escola);

        var formacoesEscola = escolaRepository.findByFormacaoEscola(escola.getNomeEscola());

        return new DadosDetalhamentoEscola(escola, formacoesEscola);
    }
}
