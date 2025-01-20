package hub.forum.api.domain.usuario.validacao;

import hub.forum.api.domain.usuario.OperacaoDominio;
import hub.forum.api.domain.usuario.repository.UsuarioRepository;
import hub.forum.api.infra.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class validarDesbloqueioConta implements ValidadorBase<DadosValidacaoUsuario> {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void validar(DadosValidacaoUsuario dados, OperacaoDominio operacao) {

        if (operacao != OperacaoDominio.DESBLOQUEIO) {
            return; // Sai da validação se não for operação de desbloqueio
        }

        var usuario = usuarioRepository.findByIdAndAtivoTrue(dados.id());
        if (usuario == null) {
            throw new ValidacaoException("Usuário não encontrado ou inativo");
        }

        if (!usuario.isBloqueadoPermanente()) {
            throw new ValidacaoException("Esta conta não está bloqueada de forma permanente. Verifique os dados do usuário");
        }
    }

    @Override
    public boolean suportaOperacao(OperacaoDominio operacao) {
        return operacao == OperacaoDominio.DESBLOQUEIO;
    }
}
