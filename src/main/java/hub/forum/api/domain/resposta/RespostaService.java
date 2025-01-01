package hub.forum.api.domain.resposta;

import hub.forum.api.domain.topico.TopicoRepository;
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
public class RespostaService {

    @Autowired
    private RespostaRepository respostaRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    private final Resposta resposta = new Resposta();

    @Transactional
    public DadosDetalhamentoResposta salvarResposta(DadosRegistroReposta dados, Long topicoID) {

        var topico = topicoRepository.getReferenceById(topicoID);

        if (topico.isResolvido()) {
            throw new ValidacaoException("Este tópico já foi resolvido. Crie outro tópico caso tenha alguma dúvida.");
        }

        var resposta = new Resposta();
        resposta.salvarResposta(dados);

        log.info("Nova resposta registrada para o tópico ID: {}", topico.getId());

        return new DadosDetalhamentoResposta(resposta);
    }

    @Transactional
    public DadosDetalhamentoResposta atualizarResposta(Long topicoID, DadosRegistroReposta dados) {

        var usuario = usuarioRepository.getReferenceById(dados.autorID());

        var topico = topicoRepository.getReferenceById(topicoID);

            if (!usuario.getId().equals(topico.getAutor().getId())) {
                throw new ValidacaoException("Somente o autor da resposta pode realizar essa alteração");
            }

            resposta.atualizarResposta(dados);

            return new DadosDetalhamentoResposta(resposta);
    }

    public Page<DadosListagemResposta> listarRespostas(Long topicoID, Pageable paginacao) {

        return respostaRepository.findRespostasTopicoID(topicoID,paginacao).map(DadosListagemResposta::new);
    }

    @Transactional
    public void marcarMelhorResposta(Long topicoId, Long respostaId) {

        var resposta = respostaRepository.findByIdAndTopicoId(respostaId, topicoId)
                .orElseThrow(() -> new ValidacaoException("Resposta não encontrada para este tópico"));

        log.debug("Marcando resposta {} como melhor resposta do tópico {}", respostaId, topicoId);
        resposta.escolherMelhorResposta();

        log.info("Resposta marcada como solução e tópico fechado");
    }




}