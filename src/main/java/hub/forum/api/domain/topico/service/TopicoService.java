package hub.forum.api.domain.topico.service;

import hub.forum.api.domain.curso.repository.CursoRepository;
import hub.forum.api.domain.topico.Topico;
import hub.forum.api.domain.topico.dto.*;
import hub.forum.api.domain.topico.repository.TopicoRepository;
import hub.forum.api.domain.topico.validacao.DadosValidacaoTopico;
import hub.forum.api.domain.topico.validacao.ValidadorTopico;
import hub.forum.api.domain.usuario.TipoUsuario;
import hub.forum.api.domain.usuario.Usuario;
import hub.forum.api.domain.usuario.repository.UsuarioRepository;
import hub.forum.api.domain.util.PageResponse;
import hub.forum.api.infra.exceptions.ValidacaoException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class TopicoService {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;


    @Autowired
    private List<ValidadorTopico> validadores;


    public DadosDetalhamentoTopico criarTopico(DadosCadastroTopico dados, Usuario autor) {

        log.debug("Iniciando criação de tópico para usuário ID: {}", autor.getId());

        var curso = cursoRepository.findById(dados.cursoId())
                .orElseThrow(() -> {
                    log.error("Curso não encontrado ID: {}", dados.cursoId());
                    return new ValidacaoException("Curso não encontrado");
                });

        // Executa todas as validações
        var dadosValidacao = new DadosValidacaoTopico(
                null,
                autor.getId(),
                dados.cursoId(),
                curso.getFormacao().getFormacao(),
                dados.titulo(),
                dados.mensagem(),
                false,
                autor.getTipoUsuario() == TipoUsuario.ESTUDANTE // verifica se é estudante
        );

        validadores.forEach(v -> v.validar(dadosValidacao));

        var topico = new Topico();
        topico.cadastrarTopico(dados, curso, autor);

        topicoRepository.save(topico);
        log.info("Tópico ID: {} criado com sucesso pelo usuário ID: {} na formação: {}",
                topico.getId(), autor.getId(), curso.getFormacao().getFormacao());

        return new DadosDetalhamentoTopico(topico);
    }

    @Transactional
    public DadosDetalhamentoTopico atualizarTopico(Long topicoId, DadosAtualizacaoTopico dados) {

        log.debug("Buscando o tópico com ID {} para atualização", topicoId);

        var topico = topicoRepository.getReferenceById(topicoId);

        var dadosValidacao = new DadosValidacaoTopico(
                topicoId,
                topico.getAutor().getId(),
                topico.getCurso().getId(),
                topico.getCurso().getFormacao().getFormacao(),
                dados.titulo() != null ? dados.titulo() : topico.getTitulo(),
                dados.mensagem() != null ? dados.mensagem() : topico.getMensagem(),
                topico.isResolvido(),
                topico.getAutor().getTipoUsuario() == TipoUsuario.ESTUDANTE
        );

        validadores.forEach(v -> v.validar(dadosValidacao));

        topico.atualizarTopico(dados);
        log.info("Tópico com ID {} atualizado com sucesso", topicoId);

        return new DadosDetalhamentoTopico(topico);
    }

    public DadosDetalhamentoTopico detalharTopico(Long id) {
        var topico = topicoRepository.findById(id)
                .orElseThrow(() -> new ValidacaoException("Tópico não encontrado"));

        return new DadosDetalhamentoTopico(topico);
    }


    @Transactional
    public void deletarTopico(Long topicoId) {

        //a anotação já valida que apenas o administrador pode executar esta função
        log.info("Verficando tópico no banco de dados");
        var topico = topicoRepository.findById(topicoId)
                .orElseThrow(() -> new ValidacaoException("Tópico não encontrado"));

        // Não faria sentido usar exclusão lógica em tópicos
        // Farei exclusão permanente.
        log.info("Tópico deletado de forma permanente");
        topicoRepository.deleteById(topico.getId());
    }

    @Transactional
    public void fecharTopico(Long topicoId, Usuario usuarioLogado) {

        log.info("Iniciando o fechamento do tópico com ID {}", topicoId);

        var usuario = usuarioRepository.findById(usuarioLogado.getId())
                .orElseThrow(() ->
                        new ValidacaoException("Usuário não encontrado"));

        log.info("Usuário {} está fechando este tópico",usuarioLogado.obterTipoUsuario());
        var topico = topicoRepository.getReferenceById(topicoId);
        topico.marcarComoResolvido();

        topicoRepository.save(topico);

    }

    public Integer totalTopicosNaoResolvidos() {
        return topicoRepository.totalTopicosNaoResolvidos();
    }

    public Integer totalTopicosResolvidos() {
        return topicoRepository.totalTopicosResolvidos();
    }


    public PageResponse<DadosListagemTopico> listarTopicos(Pageable paginacao) {
        var page = topicoRepository.findAll(paginacao)
                .map(DadosListagemTopico::new);
        return new PageResponse<>(page);
    }

    public PageResponse<DadosListagemTopico> listarTopicosNaoResolvidos(Pageable paginacao) {
        var page = topicoRepository.topicosNaoResolvidos(paginacao)
                .map(DadosListagemTopico::new);
        return new PageResponse<>(page);
    }

    public PageResponse<DadosListagemTopico> listarTopicosResolvidos(Pageable paginacao) {
        var page = topicoRepository.topicosResolvidos(paginacao)
                .map(DadosListagemTopico::new);
        return new PageResponse<>(page);
    }

    public PageResponse<DadosListagemTopico> listarTopicosSemRespostas(Pageable paginacao) {
        var page = topicoRepository.listarTopicosSemRespostas(paginacao)
                .map(DadosListagemTopico::new);
        return new PageResponse<>(page);
    }

    public PageResponse<DadosListagemTopico> listarTopicosRecentes(Pageable paginacao) {
        var page = topicoRepository.findTopicosRecentes(paginacao)
                .map(DadosListagemTopico::new);
        return new PageResponse<>(page);
    }

    public DadosEstatisticasTopico obterEstatisticas() {
        log.debug("Coletando estatísticas dos tópicos");

        try {
            var topicosResolvidos = topicoRepository.totalTopicosResolvidos();
            var topicosNaoResolvidos = topicoRepository.totalTopicosNaoResolvidos();
            var topicosSemResposta = topicoRepository.countTopicosSemResposta();
            var topicosUrgentes = topicoRepository.countTopicosRecentes();
            var tempoMedioResposta = topicoRepository.calcularTempoMedioResposta();

            var estatisticas = new DadosEstatisticasTopico(
                    topicosResolvidos,
                    topicosNaoResolvidos,
                    topicosSemResposta,
                    topicosUrgentes,
                    null, // será calculado no construtor do record
                    tempoMedioResposta
            );

            log.info("Estatísticas coletadas com sucesso. Taxa de resolução: {}%",
                    String.format("%.2f", estatisticas.percentualResolucao()));

            return estatisticas;

        } catch (Exception e) {
            log.error("Erro ao coletar estatísticas: {}", e.getMessage());
            throw new ValidacaoException("Erro ao gerar estatísticas dos tópicos");
        }
    }

    // Método auxiliar para calcular métricas específicas se necessário
    private Map<String, Double> calcularMetricasAdicionais() {
        var metricas = new HashMap<String, Double>();

        // Exemplo de métrica: média de respostas por tópico
        var totalTopicos = topicoRepository.count();
        var totalRespostas = topicoRepository.countTotalRespostas();

        if (totalTopicos > 0) {
            metricas.put("mediaRespostasPorTopico",
                    (double) totalRespostas / totalTopicos);
        }

        return metricas;
    }
}

