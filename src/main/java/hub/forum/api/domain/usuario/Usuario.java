package hub.forum.api.domain.usuario;

import hub.forum.api.domain.pefil.Perfil;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity(name = "Usuario")
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private static final int LIMITE_FALHAS = 4;

    private String login; // será o E-mail: do usuario e deve ser único
    private String senha; // deve ser em hash --> BCrypt

    //validação para cadastro interno

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime ultimoLogin; // atualizado a cada "login"

    private int falhasLogin; //atualizar, manter bloqueado a partir de determinada quantidade de erros

    @NotNull(message = "A data de assinatura é obrigatória")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @PastOrPresent(message = "A data de assinatura não pode estar no futuro")
    private LocalDateTime dataAssinatura;

    @NotNull(message = "A data de expiração é obrigatória")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Future
    private LocalDateTime expiracaoAssinatura; //// this.expiracaoAssinatura = dataAssinatura.toLocaldate().plusYears(1);


    public boolean validarAssinaturaAtiva() {
        return expiracaoAssinatura != null && expiracaoAssinatura.isAfter(LocalDate.now().atStartOfDay());
    }


    @OneToOne
    @JoinColumn(name = "perfil_id", nullable = false, unique = true) // relacionamento unilateral
    private Perfil perfil;


    // métodos para validar falhas de "login" e reset de falhas
    private void incrementarFalhasDeLogin(){
        this.falhasLogin++; // função para o usuário administrador.
    }

    private  void resetarFalhasDeLogin(){
        this.falhasLogin = 0;
    }



    //métodos da interface
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
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
        return this.falhasLogin < LIMITE_FALHAS;
    } //administrador pode desbloquear resetando as falhas de ‘login’ para zero

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
