package hub.forum.api.domain.usuario;


import hub.forum.api.domain.usuario.validacao.DadosValidacaoUsuario;
import hub.forum.api.domain.validacao.ValidadorBase;
import hub.forum.api.infra.exceptions.ValidacaoException;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;


@Service
@Slf4j
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
    public DadosDetalhamentoSuporte cadastrarSuporte(Long id, @Valid DadosCadastroSuporte dados) {

        var suporte = usuarioRepository.findUsuarioSuporte(id);

        if(suporte == null){
            throw new ValidacaoException("Usuário não encontrado");
        }


        var validarDados = new DadosValidacaoUsuario(
                suporte.getId(),
                suporte.getLogin(),
                suporte.obterTipoUsuario()
        );

        validadores.forEach(v -> v.validar(validarDados));

        suporte.cadastrarSuporte(dados);
        usuarioRepository.save(suporte);

        return new DadosDetalhamentoSuporte(suporte);
    }

    @Transactional
    public DadosDetalhamentoProfessor cadastrarProfessor(Long id, @Valid DadosCadastroProfessor dados) {

        var professor = usuarioRepository.findUsuarioProfessor(id);

        if (professor == null){
            throw new ValidacaoException("Usuário não encontrado");
        }

        var validarDados = new DadosValidacaoUsuario(
                professor.getId(),
                professor.getLogin(),
                professor.obterTipoUsuario()
        );

        validadores.forEach(v -> v.validar(validarDados));

        professor.cadastrarProfessor(dados);
        usuarioRepository.save(professor);

        return new DadosDetalhamentoProfessor(professor);
    }

    @Transactional
    public void inativarUsuario(DadosExclusaoUsuario dados) {

        log.debug("Iniciada operação para inativar usuário ID: {}", dados.usuarioInativoID());

        var usuarioParaInativar = usuarioRepository.buscarUsuarioParaInativar(dados.usuarioInativoID())
                .orElseThrow(() -> new ValidacaoException("Usuário não encontrado"));

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

        var usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

            usuario.incrementarTentativasLogin();

            log.warn("Tentativa de login falha para usuário: {}. Total: {}",
                    login, usuario.getTentativasLogin());

        usuarioRepository.save(usuario);
    }

    @Transactional
    public void registrarLoginSucesso(String login) {

        try {
            Usuario usuario = usuarioRepository.findByLogin(login)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

            // Atualizar campos
            usuario.setUltimoLogin(LocalDateTime.now());
            usuario.resetarTentativasLogin();

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
    public void atualizarDadosLogin(Long usuarioID,DadosAtualizacaoLogin dados ) {

        log.info("Iniciando atualização de login para ID: {}", usuarioID);

        var usuario = usuarioRepository.findByIdWithPerfil(usuarioID)
                .orElseThrow(() -> {
                    log.error("Usuário não encontrado com ID: {}", usuarioID);
                    return new ValidacaoException("Usuário não encontrado");
                });

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


    public Page<DadosDetalhamentoSuporte> listarEquipeSuporte(Pageable paginacao) {
        return usuarioRepository.listaUsuariosSuporte(paginacao).map(DadosDetalhamentoSuporte::new);
    }

    public Page<DadosDetalhamentoProfessor> listarEquipeProfessores(Pageable paginacao) {
        return usuarioRepository.listarusuariosProfessores(paginacao).map(DadosDetalhamentoProfessor::new);
    }

    public Page<DadosDetalhamentoEstudante> listarEstudantesMatriculados(Pageable paginacao) {

        return usuarioRepository.listarEstudantesMatriculados(paginacao).map(DadosDetalhamentoEstudante::new);
    }
}
