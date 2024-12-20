package hub.forum.api.domain.pefil;

import hub.forum.api.domain.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity(name = "Perfil")
@Table(name = "perfis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Perfil {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nome;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true) // relacionamento unilateral
    private Usuario usuario;

    public Perfil(@NotBlank DadosPerfil dadosUsuario) {
        this.nome = dadosUsuario.nome();
    }
}
