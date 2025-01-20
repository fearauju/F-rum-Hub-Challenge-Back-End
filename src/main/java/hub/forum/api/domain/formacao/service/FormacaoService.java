package hub.forum.api.domain.formacao.service;

import hub.forum.api.domain.escola.repository.EscolaRepository;
import hub.forum.api.domain.formacao.*;
import hub.forum.api.domain.formacao.dto.DadosAtualizacaoFormacao;
import hub.forum.api.domain.formacao.dto.DadosCadastroFormacao;
import hub.forum.api.domain.formacao.dto.DadosDetalhamentoFormacao;
import hub.forum.api.domain.formacao.dto.DadosListagemFormacao;
import hub.forum.api.domain.formacao.repository.FormacaoRepository;
import hub.forum.api.domain.usuario.professor.Professor;
import hub.forum.api.domain.usuario.professor.repository.ProfessorRepository;
import hub.forum.api.domain.util.PageResponse;
import hub.forum.api.infra.exceptions.ValidacaoException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class FormacaoService {

    @Autowired
    private FormacaoRepository formacaoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private EscolaRepository escolaRepository;

    // Separar em várias transações foi uma forma de evitar conflitos de concorrências que ocorre quando o hibernate tenta atualizar a mesma entidade várias vezes,
    // Entretanto, neste projeto na segunda tentativa de carregamento já ocorria esse erro: ObjectOptimisticLockingFailureException.
    // Várias tentativas isolar e manter uma transação única para cada etapa, que funcionou em várias operações, mas no final de salvar a operação principal ocorria sempre o mesmo erro.
    // Por última tentativa é tentar a possibilidade de usar o Spring Batch para tentar contornar essa limitação, eu acho que é isso.
    // No final mudei para tentar usar camel por ser mais simples e exigir menos configurações.
    // Operações simples usar normalmente @transactional, operações
    // No final nem um dos dois era necessário, o que gerou todos esses erros foi como os relacionamentos entre as entidades foram feitos e a unicidade que queria algumas colunas gerou outros erros de repetição.


    @Transactional
    public DadosDetalhamentoFormacao cadastrarFormacao(DadosCadastroFormacao dados) {

        // Buscar a escola
        var escola = escolaRepository.findById(dados.escolaId())
                .orElseThrow(() -> new ValidacaoException("Escola não encontrada."));

        // Verificar se já existe uma formação igual para evitar duplicatas
        Optional<Formacao> formacaoExistente = formacaoRepository
                .findByFormacaoAndEscolaId(dados.formacao().trim(), dados.escolaId());

        if (formacaoExistente.isPresent()) {
            throw new ValidacaoException("Já existe uma formação com este nome para esta escola.");
        }

        // Coletar e validar professores
        List<String> nomesProfessores = dados.cursos().stream()
                .flatMap(curso -> curso.nomeProfessor().stream())
                .distinct()
                .collect(Collectors.toList());

        List<Professor> professores = professorRepository.findByUsuarioPerfilNomeIn(nomesProfessores);

        if (professores.size() != nomesProfessores.size()) {
            List<String> nomesEncontrados = professores.stream()
                    .map(prof -> prof.getUsuario().getPerfil().getNome())
                    .collect(Collectors.toList());

            List<String> nomesNaoEncontrados = nomesProfessores.stream()
                    .filter(nome -> !nomesEncontrados.contains(nome))
                    .collect(Collectors.toList());
            throw new ValidacaoException("Professores não encontrados: " + String.join(", ", nomesNaoEncontrados));
        }

        // Cadastrar a formação
        Formacao formacao = new Formacao();
        formacao.cadastrarFormacao(dados, escola, professores);
        formacaoRepository.save(formacao);

        log.info("Formação '{}' cadastrada com sucesso para a escola '{}'.",
                formacao.getFormacao(), escola.getNomeEscola());

        return new DadosDetalhamentoFormacao(formacao);
    }

    public PageResponse<DadosListagemFormacao> listarFormacoes(Pageable paginacao) {

        var page = formacaoRepository.findAll(paginacao).map(DadosListagemFormacao::new);

        return new PageResponse<>(page);
    }

    public DadosDetalhamentoFormacao detalharFormacao(Long id) {
        var formacao = formacaoRepository.findById(id)
                .orElseThrow(() -> new ValidacaoException("Formação não encontrada"));
        return new DadosDetalhamentoFormacao(formacao);
    }

    public PageResponse<DadosListagemFormacao> listarFormacoesPorEscola(Long escolaId, Pageable paginacao) {
        var page = formacaoRepository.findByEscolaId(escolaId, paginacao)
                .map(DadosListagemFormacao::new);

        return new PageResponse<>(page);
    }

    @Transactional
    public DadosDetalhamentoFormacao atualizarFormacao(DadosAtualizacaoFormacao dados, Long formacaoID) {

        var formacao = formacaoRepository.getReferenceById(formacaoID);
        formacao.atualizarInformacoes(dados);

        log.info("Formação ID {} atualizada", formacao.getId());
        return new DadosDetalhamentoFormacao(formacao);
    }
}
