package hub.forum.api.domain.perfil.validacao;

import hub.forum.api.domain.perfil.OperacaoPerfil;
import hub.forum.api.infra.exceptions.ValidacaoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ValidarProprietarioPerfil implements ValidadorPerfil<DadosValidacaoPerfil> {

    @Override
    public void validar(DadosValidacaoPerfil dados, OperacaoPerfil operacao) {
        if (!dados.usuarioLogado().getId().equals(dados.usuarioId())) {
            var acao = operacao == OperacaoPerfil.CRIACAO ? "criar" : "atualizar";
            log.warn("Usuário {} tentou {} perfil de outro usuário ID: {}",
                    dados.usuarioLogado().getLogin(),
                    acao,
                    dados.usuarioId());
            throw new ValidacaoException("Não é permitido " + acao + " perfil de outro usuário");
        }
    }
}
