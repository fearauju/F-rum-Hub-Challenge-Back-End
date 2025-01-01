package hub.forum.api.domain.usuario;


import hub.forum.api.infra.exceptions.ValidacaoException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;


@Service
@Slf4j
public class UsuarioService {


    @Autowired
    private UsuarioRepository usuarioRepository;



    @Transactional
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public void inativarUsuario(DadosExclusaoUsuario dados) {

        log.debug("Tentando inativar usuário ID: {}", dados.usuarioInativo_id());


        // Verifica se o usuário que está a executar a ação é um usuário administrador
        var admin = usuarioRepository.findById(dados.usuario_id())
                .filter(u -> u.obterTipoUsuario() == TipoUsuario.ADMINISTRADOR)
                .orElseThrow(() -> new ValidacaoException("Apenas administradores podem inativar usuários"));

        // Busca o usuário a ser inativado
        var usuarioParaInativar = usuarioRepository.findInativableUserById(dados.usuarioInativo_id())
                .orElseThrow(() -> new ValidacaoException("Usuário não encontrado ou não pode ser inativado"));

        if (!(usuarioParaInativar instanceof InativacaoUsuario inativavel)) {
            throw new ValidacaoException("Este tipo de usuário não pode ser inativado");
        }

        inativavel.setAtivo(false);

        log.info("Usuário ID {} inativado por {}",
                dados.usuarioInativo_id(),
                dados.usuario_id());
    }

    @Transactional
    public void exlusaoUsuarios(DadosExclusaoUsuario dados) {

        var usuario = usuarioRepository.getReferenceById(dados.usuario_id());


        if(!usuarioRepository.existsById(dados.usuarioInativo_id())){
            throw new ValidacaoException("Usuário não encontrado");
        }

        var usuarioInativo = usuarioRepository.getReferenceById(dados.usuarioInativo_id());

        if(usuarioInativo.obterTipoUsuario() == TipoUsuario.ESTUDANTE ||
                usuarioInativo.obterTipoUsuario() == TipoUsuario.ADMINISTRADOR){
            throw new ValidacaoException("Esses usuários não devem ser excluídos");
        }

        usuarioRepository.updateStatusAtivo(usuarioInativo.getId(), false);

    }

    @Transactional
    public void registrarTentativaLoginFalha(String login) {

        var usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

       usuario.incrementarTentativasLogin();

        log.warn("Tentativa de login falha para usuário: {}. Total: {}",
                login, usuario.getTentativasLogin());

        usuarioRepository.atualizarTentativasLogin(usuario.getLogin(), usuario.getTentativasLogin());
    }

    @Transactional
    public void registrarLoginSucesso(String login) {

        var usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        usuario.resetarTentativasLogin();
        var agora = LocalDateTime.now();
        usuario.setUltimoLogin(agora);

        usuarioRepository.atualizarLoginSucesso(
                login,
                usuario.getTentativasLogin(),
                agora
        );
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SUPORTE')")
    public void desbloquearConta(Long usuarioId) {

        var usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ValidacaoException("Usuário não encontrado"));

        usuario.resetarTentativasLogin();
        usuarioRepository.atualizarLoginSucesso(
                usuario.getLogin(),
                0,
                usuario.getUltimoLogin()
        );

        log.info("Conta desbloqueada para usuário: {}", usuario.getLogin());
    }
}
