package hub.forum.api.domain.usuario;

import hub.forum.api.domain.perfil.Perfil;
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


@Entity()
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_usuario", discriminatorType = DiscriminatorType.STRING)
@Slf4j
public abstract class Usuario implements UserDetails {

    //O cadastro de usuários será feito de forma interna, via terminal. Senha, em formato hash --> BCrypt.
    //Principalmente as subclasses conforme o tipo de usuário. Verifique o Enum TipoUsuario.

    @Version //tentar evitar conflito de concorrência.
    @Column(name = "version", nullable = false)
    private Long version = 0L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(unique = true, nullable = false)
    @Email
    private String login;

    @Column(nullable = false)
    @NotBlank
    private String senha;

    @Transient
    public abstract TipoUsuario obterTipoUsuario();

    @Column(name = "ultimo_login", nullable = false)
    private LocalDateTime ultimoLogin;

    @Column(name = "tentativas_login")
    private Integer tentativasLogin = 0;

    @Column(name = "conta_bloqueada")
    private boolean contaBloqueada = false;

    @Column(name = "ativo",nullable = false)
    private boolean ativo = true;


    @OneToOne(mappedBy = "usuario", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @JoinColumn(name = "perfil_id")
    private Perfil perfil;


    public void incrementarTentativasLogin() {

        this.tentativasLogin++;
        if (this.tentativasLogin >= 6) {
            this.contaBloqueada = true;
            log.warn("Conta bloqueada após {} tentativas falhas: {}",
                    this.tentativasLogin, this.login);
        }
    }

    public void resetarTentativasLogin() {

        if (this.tentativasLogin > 0) {
            log.info("Resetando tentativas de login para usuário: {}", this.login);
            this.tentativasLogin = 0;
        }
    }

    public void inativarUsuario() {

        this.ativo = false;
    }

    public void atualizarDadosLogin(DadosAtualizacaoLogin dados) {

        if (dados.login() != null) {
            this.login = dados.login().trim();
        }

        if (dados.senha() != null) {
            this.senha = dados.senha();
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
        return !contaBloqueada;
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
