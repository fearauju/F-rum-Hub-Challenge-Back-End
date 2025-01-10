package hub.forum.api.domain.resposta;

import hub.forum.api.domain.topico.DadosFechamentoTopico;
import hub.forum.api.domain.topico.TopicoRepository;
import hub.forum.api.domain.usuario.TipoUsuario;
import hub.forum.api.domain.usuario.UsuarioRepository;
import hub.forum.api.domain.validacao.ValidadorBase;
import hub.forum.api.infra.exceptions.ValidacaoException;
import hub.forum.api.infra.security.SegurancaService;
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
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SegurancaService segurancaService;

    @Autowired
    private List<ValidadorBase<DadosValidacaoResposta>> validadores;


    public RespostaService(List<ValidadorBase<DadosValidacaoResposta>> validadores){
        this.validadores = validadores;
    }

    @Transactional
    public DadosDetalhamentoResposta salvarResposta(DadosRegistroReposta dados, Long topicoID) {

        var topico = topicoRepository.getReferenceById(topicoID);
        var autor = segurancaService.getUsuarioLogado();

        var dadosValidacao = new DadosValidacaoResposta(topicoID, null, autor.getId());
        validadores.forEach(v -> v.validar(dadosValidacao));

        var resposta = new Resposta();
        resposta.salvarResposta(dados, topico, autor);

        log.info("Nova resposta registrada para o tópico ID: {}", topicoID);
        return new DadosDetalhamentoResposta(respostaRepository.save(resposta));
    }

    @Transactional
    public DadosDetalhamentoResposta atualizarResposta(Long topicoID, Long respostaID, DadosRegistroReposta dados) {

        var dadosValidacao = new DadosValidacaoResposta(topicoID, respostaID, segurancaService.getUsuarioLogado().getId());
        validadores.forEach(v -> v.validar(dadosValidacao));

        var resposta = respostaRepository.getReferenceById(respostaID);
        resposta.atualizarResposta(dados);

        return new DadosDetalhamentoResposta(resposta);
    }

    public Page<DadosListagemResposta> listarRespostas(Long topicoID, Pageable paginacao) {

        return respostaRepository.findRespostasTopicoID(topicoID,paginacao).map(DadosListagemResposta::new);
    }


    @Transactional
    public void marcarMelhorResposta(Long topicoId, Long respostaId, DadosFechamentoTopico dados) {
        var resposta = respostaRepository.findByIdAndTopicoId(respostaId, topicoId)
                .orElseThrow(() -> new ValidacaoException("Resposta não encontrada para este tópico"));

        var topico = topicoRepository.getReferenceById(topicoId);

        var dadosValidacao = new DadosValidacaoResposta(topicoId, respostaId, segurancaService.getUsuarioLogado().getId());
        validadores.forEach(v -> v.validar(dadosValidacao));

        log.debug("Marcando resposta {} como melhor resposta do tópico {}", respostaId, topicoId);
        resposta.escolherMelhorResposta(topico);

        if(resposta.getAutor().obterTipoUsuario() == TipoUsuario.SUPORTE) {

            var suporte = usuarioRepository.findUsuarioSuporte(resposta.getAutor().getId());
            suporte.adicionarCasosResolvidos(dados, suporte);
            usuarioRepository.save(suporte);
        }

        log.info("Resposta marcada como solução e tópico fechado");
    }
}