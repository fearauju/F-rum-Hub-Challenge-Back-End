package hub.forum.api.domain.resposta;

import hub.forum.api.domain.escola.EscolaRepository;
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

    @Autowired
    private EscolaRepository escolaRepository;

    private  Resposta resposta = new Resposta();

    @Transactional
    public void salvarResposta(DadosRegistroReposta dados, Long topicoId) {

        var topico = topicoRepository.findById(topicoId)
                .orElseThrow(() -> new ValidacaoException("Tópico não encontrado"));

        if (topico.isResolvido()) {
            throw new ValidacaoException("Tópico já está resolvido");
        }

        resposta.salvarResposta(dados);
    }

    @Transactional
    public Resposta atualizarResposta(Long topicoID, DadosRegistroReposta dados) {

        var usuario = usuarioRepository.findById(dados.autorID())
                .orElseThrow(() -> new ValidacaoException("Usuário não encontrado"));

        var topico = topicoRepository.getReferenceById(topicoID);

            if (!usuario.getId().equals(topico.getAutor().getId())) {
                throw new ValidacaoException("Somente o autor da resposta pode realizar essa alteração");
            }

            resposta.atualizarResposta(dados);

            return resposta;
    }


    @Transactional
    public void marcandoTopicoComoResolvido(Long topicoId, Long respostaId) {

         resposta = respostaRepository.findByIdAndTopicoId(respostaId, topicoId)
                .orElseThrow(() -> new ValidacaoException("Resposta não encontrada para este tópico"));

        log.info("Resposta {} marcada como solução do tópico {}", respostaId, topicoId);
        resposta.escolherMelhorResposta();
    }

    public Page<DadosListagemResposta> listarRespostas(Long topicoID, Pageable paginacao) {

        return respostaRepository.findRespostasTopicoID(topicoID,paginacao).map(DadosListagemResposta::new);
    }


    //Escola

    public boolean podeAtualizarEscola(Long escolaId, Long usuarioId) {
        return escolaRepository.existsById(escolaId);
    }
}