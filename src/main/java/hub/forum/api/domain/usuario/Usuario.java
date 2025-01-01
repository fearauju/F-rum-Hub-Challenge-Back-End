package hub.forum.api.domain.usuario;

import hub.forum.api.domain.pefil.Perfil;
import jakarta.persistence.*;
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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login; // será o E-mail: do usuario e deve ser único
    private String senha; // deve ser em hash --> BCrypt
    private LocalDateTime ultimoLogin;
    @Column(name = "tentativas_login")
    private Integer tentativasLogin = 0;

    @Column(name = "conta_bloqueada")
    private boolean contaBloqueada = false;

    @Transient
    public abstract TipoUsuario obterTipoUsuario();


    @OneToOne(mappedBy = "usuario", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    private Perfil perfil;

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
    } //administrador ou suporte pode desbloquear resetando as falhas de ‘login’ para zero

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void incrementarTentativasLogin() {
        this.tentativasLogin++;
        if (this.tentativasLogin >= 6) {
            this.contaBloqueada = true;
        }
    }

    public void resetarTentativasLogin() {
        this.tentativasLogin = 0;
        this.contaBloqueada = false;
    }
}
