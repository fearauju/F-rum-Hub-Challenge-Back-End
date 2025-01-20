package hub.forum.api.domain.usuario.validacao;

import hub.forum.api.domain.usuario.OperacaoDominio;
import hub.forum.api.domain.usuario.TipoUsuario;

public record DadosValidacaoUsuario(
        Long id,
        String login,
        String novoLogin,
        String novaSenha,
        TipoUsuario tipo,
        OperacaoDominio operacao
) {}
