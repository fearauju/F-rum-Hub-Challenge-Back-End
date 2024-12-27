package hub.forum.api.domain.usuario.ValidarPermissoesDeAcesso;

import hub.forum.api.domain.usuario.TipoUsuario;
import hub.forum.api.domain.usuario.UsuarioRepository;
import hub.forum.api.infra.exceptions.ValidacaoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ValidarPermissaoCadastroEscola implements ValidarPermissoesUsuario {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public void validarPermissoes(Long id, TipoUsuario tipoUsuario) {

        log.debug("Verificando permissões para usuário ID: {} com tipo: {}", id, tipoUsuario);

        if (tipoUsuario != TipoUsuario.ADMINISTRADOR) {
            log.warn("Usuário {} não tem permissão de administrador", id);
            throw new ValidacaoException("Usuário não possui permissão para cadastrar uma nova Escola");
        }
    }
}
