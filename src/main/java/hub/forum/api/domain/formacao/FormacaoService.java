package hub.forum.api.domain.formacao;

import hub.forum.api.domain.curso.DadosCursosFormacao;
import hub.forum.api.domain.escola.EscolaRepository;
import hub.forum.api.domain.formacao.validacao.DadosValidacaoFormacao;
import hub.forum.api.domain.usuario.Professor;
import hub.forum.api.domain.usuario.UsuarioRepository;
import hub.forum.api.domain.validacao.ValidadorBase;
import hub.forum.api.infra.exceptions.ValidacaoException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class FormacaoService {

    @Autowired
    private FormacaoRepository formacaoRepository;

    @Autowired
    private EscolaRepository escolaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private List<ValidadorBase<DadosValidacaoFormacao>> validadores;

    @Transactional
    public Formacao cadastrarFormacao(DadosCadastroFormacao dados) {

        var escola = escolaRepository.findById(dados.escolaID())
                .orElseThrow(() ->  new ValidacaoException("Escola não encontrada"));

        var dadosValidacao = new DadosValidacaoFormacao(
                dados.escolaID(),
                dados.formacao(),
                dados.cursos()
        );

        validadores.forEach(v -> v.validar(dadosValidacao));

        List<String> nomes = dados.cursos()
                .stream()
                .map(DadosCursosFormacao::nome_professor) // Retorna List<String>
                .flatMap(List::stream) // Achata a lista de listas
                .collect(Collectors.toList());


        List<Professor> professores = usuarioRepository.findByNomeIn(nomes);

        var formacao = new Formacao();

        formacao.cadastrarFormacao(dados, escola, professores);

        return formacaoRepository.save(formacao);
    }


    public Page<DadosListagemFormacao> listarFormacoes(Pageable paginacao) {
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
