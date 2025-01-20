package hub.forum.api.domain.usuario.service;

import hub.forum.api.domain.usuario.OperacaoDominio;
import hub.forum.api.domain.usuario.dto.DadosDetalhamentoUsuarioInativo;
import hub.forum.api.domain.usuario.repository.UsuarioRepository;
import hub.forum.api.domain.usuario.dto.DadosAtualizacaoLogin;
import hub.forum.api.domain.usuario.validacao.DadosValidacaoUsuario;
import hub.forum.api.domain.usuario.validacao.ValidadorBase;
import hub.forum.api.domain.util.PageResponse;
import hub.forum.api.infra.exceptions.ValidacaoException;
import hub.forum.api.infra.security.RateLimitService;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
    private RateLimitService rateLimitService;

    @Autowired
    private List<ValidadorBase<DadosValidacaoUsuario>> validadores;


    @Transactional
    public void inativarUsuario(Long usuarioId) {

        log.debug("Iniciada operação para inativar usuário ID: {}", usuarioId);

        var usuarioParaInativar = usuarioRepository.findByIdAndAtivoTrue(usuarioId);

        if (usuarioParaInativar == null){
            log.error("Não foi encontrado usuário com o id {}", usuarioId);
            throw new ValidacaoException("Usuário não encontrado ou ainda é um usuário ativo");
        }

        log.info("Iniciando a validação das regras");
        var dadosValidacao = new DadosValidacaoUsuario(
                usuarioId, null, null, null, null, OperacaoDominio.INATIVACAO
        );

        validadores.forEach(v -> v.validar(dadosValidacao, OperacaoDominio.INATIVACAO));

        log.info("realizando exclusão lógica do usuário com ID {}", usuarioParaInativar.getId());
        usuarioParaInativar.inativarUsuario();

        usuarioRepository.save(usuarioParaInativar);
    }

    @Transactional
    public void reativar(Long usuarioInativoId) {

        var dadosValidacao = new DadosValidacaoUsuario(
                usuarioInativoId, null, null, null, null, OperacaoDominio.REATIVACAO
        );

        validadores.forEach(v -> v.validar(dadosValidacao, OperacaoDominio.REATIVACAO));

        log.info("Buscando usuário inativo");
        var usuarioInativo = usuarioRepository.findByIdAndAtivoFalse(usuarioInativoId);

        if(usuarioInativo == null){
            throw new ValidacaoException("Usuário não encontrado");
        }

        usuarioInativo.reativar();
        usuarioRepository.save(usuarioInativo);
        log.info("Usuário foi reativado com sucesso");
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
            rateLimitService.resetarTentativas(usuario.getLogin());

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

        var dadosValidacao = new DadosValidacaoUsuario(
                usuarioId, null, null, null, null, OperacaoDominio.DESBLOQUEIO
        );

        validadores.forEach(v -> v.validar(dadosValidacao, OperacaoDominio.DESBLOQUEIO));

        log.info("Iniciando processo de desbloqueio de conta para usuário ID: {}", usuarioId);

        var usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ValidacaoException("Usuário não encontrado"));

        rateLimitService.desbloquearConta(usuario.getLogin());

        log.info("Conta desbloqueada com sucesso para usuário: {}", usuario.getLogin());
    }

    @Transactional
    public void atualizarDadosLogin(Long usuarioId, DadosAtualizacaoLogin dados ) {

        if (dados.login() == null && dados.senha() == null) {
            throw new ValidacaoException("Nenhum dado foi fornecido para atualização");
        }

        var usuario = usuarioRepository.findByIdAndAtivoTrue(usuarioId);

        if (usuario == null) {
            log.error("Usuário não encontrado para o ID: {}", usuarioId);
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        // Só criptografa a senha se ela foi fornecida
        var dadosAtualizados = new DadosAtualizacaoLogin(
                dados.login(),
                dados.senha() != null ? passwordEncoder.encode(dados.senha()) : null
        );

        usuario.atualizarDadosLogin(dadosAtualizados);
        usuarioRepository.save(usuario);
        log.info("Dados atualizados com sucesso para usuário ID: {}", usuarioId);

    }

    public PageResponse<DadosDetalhamentoUsuarioInativo> detalharUsuariosInativos(Pageable paginacao) {

        log.debug("Listando usuários inativos. Página: {}, Tamanho: {}",
                paginacao.getPageNumber(),
                paginacao.getPageSize());

        var page = usuarioRepository.findAllByAtivoFalse(paginacao)
                .map(DadosDetalhamentoUsuarioInativo::new);

        log.info("Encontrados {} professores", page.getTotalElements());
        return new PageResponse<>(page);
    }
}
