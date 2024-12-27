package hub.forum.api.domain.usuario.ValidarPermissoesDeAcesso;

import hub.forum.api.domain.usuario.TipoUsuario;

public interface ValidarPermissoesUsuario {

    void validarPermissoes(Long id, TipoUsuario tipoUsuario); // lista de regras de acesso, verificar se o usuario com detrminado id tem as permissoes para executar determinada requisicao
}
