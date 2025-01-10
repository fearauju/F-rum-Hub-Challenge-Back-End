package hub.forum.api.domain.escola.validacao;

import hub.forum.api.domain.escola.EscolaRepository;
import hub.forum.api.domain.validacao.ValidadorBase;
import hub.forum.api.infra.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarCadastroExistente implements ValidadorBase<DadosValidacaoEscola> {

    @Autowired
    private EscolaRepository escolaRepository;

    @Override
    public void validar(DadosValidacaoEscola dados) {

        if (escolaRepository.existsByNomeEscolaIgnoreCase(dados.nomeEscola())) {
            throw new ValidacaoException("Existe uma escola cadastrada com esse nome");
        }
    }
}
