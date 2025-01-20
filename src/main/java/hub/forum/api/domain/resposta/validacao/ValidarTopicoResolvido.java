package hub.forum.api.domain.resposta.validacao;

import hub.forum.api.domain.resposta.OperacaoResposta;
import hub.forum.api.domain.resposta.dto.DadosValidacaoResposta;
import hub.forum.api.domain.topico.repository.TopicoRepository;
import hub.forum.api.infra.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarTopicoResolvido implements ValidadorResposta {
    @Autowired
    private TopicoRepository topicoRepository;

    @Override
    public void validar(DadosValidacaoResposta dados, OperacaoResposta operacao) {

        if(operacao == OperacaoResposta.CADASTRAR){
            return;
        }

        if (topicoRepository.topicoResolvido(dados.topicoId())) {
            throw new ValidacaoException("Tópico já está resolvido");
        }
    }
}