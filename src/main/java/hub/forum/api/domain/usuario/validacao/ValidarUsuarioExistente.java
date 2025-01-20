package hub.forum.api.domain.usuario.validacao;

import hub.forum.api.domain.usuario.OperacaoDominio;
import hub.forum.api.domain.usuario.repository.UsuarioRepository;
import hub.forum.api.infra.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("validadorUsuarioExistente")
public class ValidarUsuarioExistente implements ValidadorBase<DadosValidacaoUsuario> {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public void validar(DadosValidacaoUsuario dados, OperacaoDominio operacao) {
        var usuario = switch (operacao) {
            case INATIVACAO -> repository.findByIdAndAtivoTrue(dados.id());
            case REATIVACAO -> repository.findByIdAndAtivoFalse(dados.id());
            default -> repository.findById(dados.id()).orElse(null);
        };

        if (usuario == null) {
            throw new ValidacaoException("Usuário não encontrado");
        }
    }

    @Override
    public boolean suportaOperacao(OperacaoDominio operacao) {
        return true;
    }
}
