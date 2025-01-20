package hub.forum.api.domain.usuario.validacao;

import hub.forum.api.domain.usuario.OperacaoDominio;
import hub.forum.api.domain.usuario.repository.UsuarioRepository;
import hub.forum.api.infra.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarLoginUnico implements ValidadorBase<DadosValidacaoUsuario> {
    @Autowired
    private UsuarioRepository repository;

    @Override
    public void validar(DadosValidacaoUsuario dados, OperacaoDominio operacao) {
        if (operacao == OperacaoDominio.ATUALIZACAO_LOGIN && dados.novoLogin() != null) {
            if (repository.existsByLogin(dados.novoLogin())) {
                throw new ValidacaoException("Login já está em uso");
            }
        }
    }

    @Override
    public boolean suportaOperacao(OperacaoDominio operacao) {
        return true;
    }
}