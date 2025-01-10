package hub.forum.api.domain.usuario.validacao;

import hub.forum.api.domain.usuario.TipoUsuario;

public record DadosValidacaoUsuario(

        Long usuarioID,
        String login,
        TipoUsuario tipoUsuario
) { }
