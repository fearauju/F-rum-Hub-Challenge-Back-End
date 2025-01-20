package hub.forum.api.domain.resposta.validacao;

import hub.forum.api.domain.resposta.OperacaoResposta;
import hub.forum.api.domain.resposta.dto.DadosValidacaoResposta;

public interface ValidadorResposta {
    void validar(DadosValidacaoResposta dados, OperacaoResposta operacao);
}