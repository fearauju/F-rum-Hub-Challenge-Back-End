package hub.forum.api.domain.usuario.validacao;

import hub.forum.api.domain.usuario.TipoUsuario;
import hub.forum.api.domain.validacao.ValidadorBase;
import hub.forum.api.infra.exceptions.ValidacaoException;
import org.springframework.stereotype.Component;

@Component
public class ValidarInativacaoUsuario implements ValidadorBase<DadosValidacaoUsuario> {
    @Override
    public void validar(DadosValidacaoUsuario dados) {

        if (dados.tipoUsuario() == TipoUsuario.ADMINISTRADOR ||
                dados.tipoUsuario() == TipoUsuario.ESTUDANTE) {
            throw new ValidacaoException("Este tipo de usuário não pode ser inativado");
        }
    }
}
