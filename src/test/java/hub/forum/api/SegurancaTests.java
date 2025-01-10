package hub.forum.api;

import hub.forum.api.domain.usuario.*;
import hub.forum.api.infra.exceptions.ValidacaoException;
import hub.forum.api.infra.security.SegurancaService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SegurancaTests {


    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private SegurancaService segurancaService;

    // Método helper para configurar o usuário logado no contexto de segurança
    private void configurarUsuarioLogado(Usuario usuario) {

        // Cria uma lista de authorities baseada no tipo do usuário
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        switch(usuario.obterTipoUsuario()) {
            case PROFESSOR -> authorities.add(new SimpleGrantedAuthority("ROLE_PROFESSOR"));
            case SUPORTE -> authorities.add(new SimpleGrantedAuthority("ROLE_SUPORTE"));
            case ESTUDANTE -> authorities.add(new SimpleGrantedAuthority("ROLE_ESTUDANTE"));
            case ADMINISTRADOR -> authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        var authentication = new UsernamePasswordAuthenticationToken(usuario, null, authorities);
        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioSemPermissaoTentarAlterarDadosDeOutroUsuario() {
        // Arrange
        var professor = criarUsuarioMock(1L, "prof1@email.com", TipoUsuario.PROFESSOR);
        var suporte = criarUsuarioMock(2L, "suporte1@email.com", TipoUsuario.SUPORTE);

        configurarUsuarioLogado(professor);
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(suporte));

        // Act & Assert
        var exception = assertThrows(ValidacaoException.class, () ->
                segurancaService.podeAtualizarDadosUsuario(2L));

        assertEquals("Não pode realizar essa alteração", exception.getMessage());
    }

    @Test
    void devePermitirAdministradorAtualizarQualquerUsuario() {
        // Arrange
        var admin = criarUsuarioMock(1L, "admin@email.com", TipoUsuario.ADMINISTRADOR);
        var professor = criarUsuarioMock(2L, "prof@email.com", TipoUsuario.PROFESSOR);

        configurarUsuarioLogado(admin);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(admin));

        // Act & Assert
        assertTrue(segurancaService.podeAtualizarDadosUsuario(1L));
    }

    @Test
    void devePermitirUsuarioAtualizarPropriosDados() {
        // Arrange
        var usuario = criarUsuarioMock(1L, "user@email.com", TipoUsuario.PROFESSOR);

        configurarUsuarioLogado(usuario);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        // Act & Assert
        assertTrue(segurancaService.podeAtualizarDadosUsuario(1L));
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        // Arrange
        var usuarioLogado = criarUsuarioMock(1L, "user@email.com", TipoUsuario.PROFESSOR);
        configurarUsuarioLogado(usuarioLogado);

        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(ValidacaoException.class, () ->
                segurancaService.podeAtualizarDadosUsuario(99L));

        assertEquals("Usuário não encontrado", exception.getMessage());
    }

    private Usuario criarUsuarioMock(Long id, String login, TipoUsuario tipo) {
        Usuario usuario;
        switch(tipo) {
            case PROFESSOR -> usuario = new Professor();
            case SUPORTE -> usuario = new Suporte();
            case ESTUDANTE -> usuario = new Estudante();
            case ADMINISTRADOR -> usuario = new Administrador();
            default -> throw new IllegalArgumentException("Tipo inválido");
        }
        usuario.setId(id);
        usuario.setLogin(login);
        usuario.setAtivo(true);
        return usuario;
    }
}