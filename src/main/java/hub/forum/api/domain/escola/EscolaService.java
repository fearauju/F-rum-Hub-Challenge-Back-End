package hub.forum.api.domain.escola;

import hub.forum.api.domain.escola.validacao.DadosValidacaoEscola;
import hub.forum.api.domain.util.ValidadorBase;
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
    private List<ValidadorBase<DadosValidacaoEscola>> validadores;

    @Transactional
    public DadosDetalhamentoCadastroEscola cadastrarEscola(DadosCadastroEscola dados) {

        var validacaoDados = new DadosValidacaoEscola(
                dados.nomeEscola()
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
            Long escolaID, DadosAtualizacaoEscola dados) {

        var validacaoDados = new DadosValidacaoEscola(
                dados.nomeEscola()
        );

        validadores.forEach(v-> v.validar(validacaoDados));

        var escola = escolaRepository.getReferenceById(escolaID);

        if(escola == null){
            throw  new ValidacaoException("Escola não encontrada");
        }

        escola.atualizarInformacoes(dados);

        return new DadosDetalhamentoEscola(escola);
    }
}
