package hub.forum.api.domain.usuario.validacao_regras;

import hub.forum.api.domain.usuario.TipoUsuario;

public record DadosValidacaoUsuario(

        Long usuarioID,
        String login,
        TipoUsuario tipoUsuario
) { }
