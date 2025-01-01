package hub.forum.api.domain.escola;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class EscolaService {

    @Autowired
    private EscolaRepository escolaRepository;

    @Transactional
    public DadosDetalhamentoEscola cadastrarEscola(DadosCadastroEscola dados) {
        var escola = new Escola();
        escola.cadastrarEscola(dados);
        escolaRepository.save(escola);

        log.info("Escola cadastrada com ID: {}", escola.getId());
        return new DadosDetalhamentoEscola(escola);
    }

    @Transactional
    public DadosDetalhamentoEscola atualizarDadosEscola(DadosAtualizacaoEscola dados) {

        var escola = escolaRepository.getReferenceById(dados.escolaID());
        escola.atualizarInformacoes(dados);

        log.info("Escola ID {} atualizada", dados.escolaID());
        return new DadosDetalhamentoEscola(escola);
    }

    public Page<DadosListagemEscola> listarEscolas(Pageable paginacao) {
        return escolaRepository.findAll(paginacao).map(DadosListagemEscola::new);
    }
}
