package hub.forum.api.domain.resposta.service;

import hub.forum.api.domain.resposta.OperacaoResposta;
import hub.forum.api.domain.resposta.Resposta;
import hub.forum.api.domain.resposta.dto.DadosDetalhamentoResposta;
import hub.forum.api.domain.resposta.dto.DadosListagemResposta;
import hub.forum.api.domain.resposta.dto.DadosRegistroReposta;
import hub.forum.api.domain.resposta.dto.DadosValidacaoResposta;
import hub.forum.api.domain.resposta.repository.RespostaRepository;
import hub.forum.api.domain.resposta.validacao.ValidadorResposta;
import hub.forum.api.domain.topico.dto.DadosFechamentoTopico;
import hub.forum.api.domain.topico.repository.TopicoRepository;
import hub.forum.api.domain.usuario.TipoUsuario;
import hub.forum.api.domain.usuario.Usuario;
import hub.forum.api.domain.usuario.suporte.repository.SuporteRepository;
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
public class RespostaService {

    @Autowired
    private RespostaRepository respostaRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private SuporteRepository suporteRepository;

    @Autowired
    private List<ValidadorResposta> validadores;

    @Transactional
    public DadosDetalhamentoResposta salvarResposta(DadosRegistroReposta dados, Long topicoId, Usuario autorResposta) {

        log.debug("Iniciando registro de resposta para tópico ID: {}", topicoId);

        var topico = topicoRepository.findById(topicoId)
                .orElseThrow(() -> {
                    log.error("Tópico não encontrado ID: {}", topicoId);
                    return new ValidacaoException("Tópico não encontrado");
                });

        var dadosValidacao = new DadosValidacaoResposta(
                topicoId,
                null,
                autorResposta.getId()
        );
        validadores.forEach(v -> v.validar(dadosValidacao, OperacaoResposta.CADASTRAR));

        var resposta = new Resposta();
        resposta.salvarResposta(dados, topico, autorResposta);
        respostaRepository.save(resposta);

        log.info("Resposta registrada com sucesso para tópico ID: {}", topicoId);
        return new DadosDetalhamentoResposta(resposta);
    }

    @Transactional
    public DadosDetalhamentoResposta atualizarResposta(Long topicoId, Long respostaId,
                                                       DadosRegistroReposta dados, Usuario autorResposta) {
        //Tanto o tópico quanto a resposta é verificado previamente pela anotação
        // e as regras de permissões no Enum TipoUsuario
        log.debug("Iniciando atualização da resposta ID: {}", respostaId);

        var dadosValidacao = new DadosValidacaoResposta(
                topicoId,
                respostaId,
                autorResposta.getId()
        );
        validadores.forEach(v -> v.validar(dadosValidacao, OperacaoResposta.ATUALIZAR));

        var resposta = respostaRepository.getReferenceById(respostaId);
        resposta.atualizarResposta(dados);

        log.info("Resposta ID: {} atualizada com sucesso", respostaId);
        return new DadosDetalhamentoResposta(resposta);
    }

    public Page<DadosListagemResposta> listarRespostasDoTopico(Long topicoId, Pageable paginacao) {

        return respostaRepository.findRespostasTopicoId(topicoId,paginacao).map(DadosListagemResposta::new);
    }


    @Transactional
    public DadosDetalhamentoResposta marcarMelhorResposta(
            Long topicoId,
            Long respostaId,
            DadosFechamentoTopico dados,
            Usuario usuarioLogado) {

        var dadosValidaco = new DadosValidacaoResposta(
                topicoId,
                respostaId,
                usuarioLogado.getId()
        );

        validadores.forEach(v -> v.validar(dadosValidaco, OperacaoResposta.MARCAR_SOLUCAO));


        var topico = topicoRepository.getReferenceById(topicoId);
        var resposta = respostaRepository.getReferenceById(respostaId);

        log.debug("Marcando resposta {} como melhor resposta do tópico {}", respostaId, topicoId);
        resposta.escolherMelhorResposta(topico);

        // Atualiza estatísticas se for resposta do suporte
        if (resposta.getAutor().obterTipoUsuario() == TipoUsuario.SUPORTE) {

            var suporte = suporteRepository.findUsuarioSuporte(resposta.getAutor().getId());

            suporte.adicionarCasosResolvidos(dados, suporte);
            suporteRepository.save(suporte);

            log.info("Estatísticas do suporte {} atualizadas", resposta.getAutor().getPerfil().getNome());
        }

        log.info("Tópico {} marcado como resolvido com a resposta {}", topicoId, respostaId);
        return new DadosDetalhamentoResposta(resposta);
    }
}