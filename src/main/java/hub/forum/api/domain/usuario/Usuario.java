package hub.forum.api.domain.usuario;

import hub.forum.api.domain.perfil.Perfil;
import hub.forum.api.domain.usuario.dto.DadosAtualizacaoLogin;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


@Entity
@Table(name = "usuarios")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Slf4j
public class Usuario implements UserDetails {

    @Version
    private Long version = 0L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Email
    private String login;

    @Column(nullable = false)
    @NotBlank
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_usuario", nullable = false, insertable = false, updatable = false)
    private TipoUsuario tipoUsuario;

    @Column(name = "ultimo_login", nullable = false)
    private LocalDateTime ultimoLogin;

    @Column(name = "ativo", nullable = false)
    private boolean ativo = true;

    @Column(name = "bloqueado_permanente", nullable = false)
    private boolean bloqueadoPermanente = false;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = true)
    private Perfil perfil;

    @Transient
    public TipoUsuario obterTipoUsuario() {
        return tipoUsuario;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
        perfil.setUsuario(this);
    }

    public void atualizarRegistroLogin() {

        this.ultimoLogin = LocalDateTime.now();
    }

    public void inativarUsuario() {

        this.ativo = false;
    }

    public void reativar() {
        this.ativo = true;
    }

    public void atualizarDadosLogin(DadosAtualizacaoLogin dados) {

        if (dados.login() != null) {
            this.login = dados.login().trim();
        }

        if (dados.senha() != null) {
            this.senha = dados.senha().trim();
        }

        log.debug("Dados de login atualizados para usuário ID: {}", this.id);
    }

    //métodos da interface
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        String role = "ROLE_" + obterTipoUsuario().name();
        log.debug("Gerando authorities para usuário {} ({}): {}",
                this.getLogin(),
                this.getId(),
                role);
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !bloqueadoPermanente;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
