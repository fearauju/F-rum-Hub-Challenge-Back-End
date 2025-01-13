package hub.forum.api.domain.formacao.validacao;

import hub.forum.api.domain.formacao.repository.FormacaoRepository;
import hub.forum.api.domain.util.ValidadorBase;
import hub.forum.api.infra.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarFormacaoUnica implements ValidadorBase<DadosValidacaoFormacao> {
    @Autowired
    private FormacaoRepository formacaoRepository;

    @Override
    public void validar(DadosValidacaoFormacao dados) {

        if (dados.formacao() != null &&
                formacaoRepository.existsByFormacaoIgnoreCase(dados.formacao())) {
            throw new ValidacaoException("Já existe uma formação com este nome");
        }
    }
}