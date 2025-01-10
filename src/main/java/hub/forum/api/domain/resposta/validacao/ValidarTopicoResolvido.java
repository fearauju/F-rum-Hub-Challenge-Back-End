package hub.forum.api.domain.resposta.validacao;

import hub.forum.api.domain.resposta.DadosValidacaoResposta;
import hub.forum.api.domain.topico.TopicoRepository;
import hub.forum.api.domain.validacao.ValidadorBase;
import hub.forum.api.infra.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarTopicoResolvido implements ValidadorBase<DadosValidacaoResposta> {

    @Autowired
    private TopicoRepository topicoRepository;

    @Override
    public void validar(DadosValidacaoResposta dados) {

        var topico = topicoRepository.getReferenceById(dados.topicoId());

        if (topico.isResolvido()) {
            throw new ValidacaoException("Tópico já está resolvido");
        }
    }
}
