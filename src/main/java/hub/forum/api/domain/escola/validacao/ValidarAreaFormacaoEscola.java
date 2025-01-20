package hub.forum.api.domain.escola.validacao;

import hub.forum.api.domain.escola.AreaFormacao;
import hub.forum.api.infra.exceptions.ValidacaoException;

import java.util.Arrays;

public class ValidarAreaFormacaoEscola implements ValidadorEscola{

    @Override
    public void validar(DadosValidacaoEscola dados) {

        if(Arrays.stream(AreaFormacao.values()).noneMatch(a -> a.equals(dados.areaFormacao()))){
            throw new ValidacaoException("Formação indisponível. Verifique as formações disponíveis");
        }
    }
}
