package hub.forum.api.domain.resposta.validacao;

import hub.forum.api.domain.resposta.OperacaoResposta;
import hub.forum.api.domain.resposta.dto.DadosValidacaoResposta;
import hub.forum.api.domain.resposta.repository.RespostaRepository;
import hub.forum.api.domain.perfil.validacao.ValidadorPerfil;
import hub.forum.api.infra.exceptions.ValidacaoException;
import hub.forum.api.infra.security.SegurancaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarAutorResposta implements ValidadorResposta {
    @Autowired
    private RespostaRepository respostaRepository;

    @Override
    public void validar(DadosValidacaoResposta dados, OperacaoResposta operacao) {

        if(operacao != OperacaoResposta.ATUALIZAR){
            return;
        }

        if (dados.respostaId() != null) {
            var resposta = respostaRepository.getReferenceById(dados.respostaId());
            if (!resposta.getAutor().getId().equals(dados.autorId())) {
                throw new ValidacaoException("Apenas o autor pode alterar a resposta");
            }
        }
    }
}
