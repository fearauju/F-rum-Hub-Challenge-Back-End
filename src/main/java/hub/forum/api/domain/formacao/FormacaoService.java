package hub.forum.api.domain.formacao;

import hub.forum.api.domain.escola.EscolaRepository;
import hub.forum.api.domain.usuario.UsuarioRepository;
import hub.forum.api.infra.exceptions.ValidacaoException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class FormacaoService {

    @Autowired
    private FormacaoRepository formacaoRepository;

    @Autowired
    private EscolaRepository escolaRepository;

    @Transactional
    public Formacao cadastrarFormacao(DadosCadastroFormacao dados) {

        log.debug("Verificando se escola existe");
        var escola = escolaRepository.findById(dados.escola_id())
                .orElseThrow(() -> new ValidacaoException("Escola não encontrada"));

        log.debug("Criando nova formação");
        var formacao = new Formacao();

        formacao.cadastrarFormacao(dados, escola);

        log.debug("Salvando formação no banco de dados");
        formacaoRepository.save(formacao);

        log.info("Formação cadastrada com ID: {} para escola ID: {}",
                formacao.getId(), escola.getId());

        return formacao;
    }

    public Page<DadosListagemFormacao> listarFormacao(DadosListagemFormacao filtro, Pageable paginacao) {
        return formacaoRepository.findAll(paginacao).map(DadosListagemFormacao::new);
    }

    @Transactional
    public DadosDetalhamentoFormacao atualizarFormacao(DadosAtualizacaoFormacao dados, Long formacaoID) {

        var formacao = formacaoRepository.getReferenceById(formacaoID);
        formacao.atualizarInformacoes(dados);

        log.info("Formação ID {} atualizada", formacao.getId());
        return new DadosDetalhamentoFormacao(formacao);
    }
}
