package hub.forum.api.domain.perfil.validacao;

import hub.forum.api.domain.util.ValidadorBase;
import hub.forum.api.infra.exceptions.ValidacaoException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;

@Component
public class ValidarIdadePerfil implements ValidadorBase<DadosValidacaoPerfil> {

    @Override
    public void validar(DadosValidacaoPerfil dados) {
        if (dados.dataNascimento() != null) {
            var idade = Period.between(dados.dataNascimento(), LocalDate.now()).getYears();
            if (idade < 13) {
                throw new ValidacaoException("Usuário deve ter no mínimo 13 anos");
            }
        }
    }
}
