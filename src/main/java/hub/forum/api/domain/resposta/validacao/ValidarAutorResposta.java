package hub.forum.api.domain.resposta.validacao;

import hub.forum.api.domain.resposta.DadosValidacaoResposta;
import hub.forum.api.domain.resposta.RespostaRepository;
import hub.forum.api.domain.validacao.ValidadorBase;
import hub.forum.api.infra.exceptions.ValidacaoException;
import hub.forum.api.infra.security.SegurancaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarAutorResposta implements ValidadorBase<DadosValidacaoResposta> {

    @Autowired
    private RespostaRepository respostaRepository;

    @Autowired
    private SegurancaService segurancaService;

    @Override
    public void validar(DadosValidacaoResposta dados) {

        var resposta = respostaRepository.getReferenceById(dados.respostaId());

        if (!resposta.getAutor().equals(segurancaService.getUsuarioLogado())) {
            throw new ValidacaoException("Apenas o autor pode alterar a resposta");
        }
    }
}
