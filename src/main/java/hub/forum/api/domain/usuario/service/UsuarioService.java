package hub.forum.api.domain.usuario.service;

import hub.forum.api.domain.usuario.repository.UsuarioRepository;
import hub.forum.api.domain.usuario.dto.DadosAtualizacaoLogin;
import hub.forum.api.domain.usuario.dto.DadosExclusaoUsuario;
import hub.forum.api.domain.usuario.estudante.DadosCadastroEstudante;
import hub.forum.api.domain.usuario.estudante.DadosDetalhamentoEstudante;
import hub.forum.api.domain.usuario.validacao_regras.DadosValidacaoUsuario;
import hub.forum.api.domain.util.ValidadorBase;
import hub.forum.api.infra.exceptions.ValidacaoException;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@Slf4j
@Transactional
public class UsuarioService {


    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private List<ValidadorBase<DadosValidacaoUsuario>> validadores;


    @Transactional
    public DadosDetalhamentoEstudante cadastrarEstudante(Long id, @Valid DadosCadastroEstudante dados) {
        return null;
    }


    @Transactional
    public void inativarUsuario(DadosExclusaoUsuario dados) {

        log.debug("Iniciada operação para inativar usuário ID: {}", dados.usuarioInativoID());

        var usuarioParaInativar = usuarioRepository.findByIdAndAtivoTrue(dados.usuarioInativoID());

        if (usuarioParaInativar == null){
            log.error("Não foi encontrado usuário com o id {}",dados.usuarioInativoID());
            throw new ValidacaoException("Usuário não encontrado");
        }
        var dadosValidacao = new DadosValidacaoUsuario(
                usuarioParaInativar.getId(),
                usuarioParaInativar.getLogin(),
                usuarioParaInativar.obterTipoUsuario()
        );

        validadores.forEach(v -> v.validar(dadosValidacao));

        usuarioParaInativar.inativarUsuario();

        log.info("Usuário com ID {} foi inativado ", dados.usuarioInativoID());

        usuarioRepository.save(usuarioParaInativar);
    }


    @Transactional
    public void registrarTentativaLoginFalha(String login) {

        var usuario = usuarioRepository.findByLoginAndAtivoTrue(login);

        if(usuario == null){
            log.error("Usuário não encontrado para o login: {}", login);
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

            usuario.incrementarTentativasLogin();

            log.warn("Tentativa de login falha para usuário: {}. Total: {}",
                    login, usuario.getTentativasLogin());

        usuarioRepository.save(usuario);
    }

    @Transactional
    public void registrarLoginSucesso(String login) {

        try {
            var usuario = usuarioRepository.findByLoginAndAtivoTrue(login);

            if(usuario == null){
                log.error("Usuário não encontrado para este login: {}", login);
                throw new UsernameNotFoundException("Usuário não encontrado");
            }

            // Atualizar campos
            usuario.atualizarRegistroLogin();

            // O Spring/Hibernate gerenciará automaticamente o incremento da versão
            usuarioRepository.save(usuario);
        } catch (OptimisticLockException e) {
            // Tratar conflito de concorrência
            log.error("Conflito de versão ao atualizar usuário", e);
            throw new RuntimeException("Erro ao atualizar usuário", e);
        }
    }

    @Transactional
    public void desbloquearConta(Long usuarioId) {

        var usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ValidacaoException("Usuário não encontrado"));

        usuario.resetarTentativasLogin();

        log.info("Conta desbloqueada para usuário: {}", usuario.getLogin());

        usuarioRepository.save(usuario);
    }

    @Transactional
    public void atualizarDadosLogin(Long usuarioID, DadosAtualizacaoLogin dados ) {

        log.info("Iniciando atualização de login para ID: {}", usuarioID);

        var usuario = usuarioRepository.findByIdAndAtivoTrue(usuarioID);

        if(usuario == null){
            log.error("Usuário não encontrado para o ID: {}", usuarioID);
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        try {

            var dadosComSenhaCriptografada = new DadosAtualizacaoLogin(
                    dados.login(),
                    passwordEncoder.encode(dados.senha())
            );

            usuario.atualizarDadosLogin(dadosComSenhaCriptografada);
            usuarioRepository.save(usuario);
            log.info("Dados atualizados com sucesso para usuário ID: {}", usuarioID);

        } catch (Exception e) {
            log.error("Erro ao atualizar dados: {}", e.getMessage(), e);
            throw new ValidacaoException("Erro ao atualizar dados do usuário");
        }
    }
}
