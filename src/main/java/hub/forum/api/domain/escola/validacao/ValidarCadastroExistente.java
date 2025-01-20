package hub.forum.api.domain.escola.validacao;

import hub.forum.api.domain.escola.repository.EscolaRepository;
import hub.forum.api.infra.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarCadastroExistente implements ValidadorEscola {

    @Autowired
    private EscolaRepository escolaRepository;

    @Override
    public void validar(DadosValidacaoEscola dados) {
        if (dados.nomeEscola() != null &&
                escolaRepository.existsByNomeEscolaIgnoreCase(dados.nomeEscola().trim())) {
            throw new ValidacaoException("Existe uma escola cadastrada com esse nome");
        }
    }
}
