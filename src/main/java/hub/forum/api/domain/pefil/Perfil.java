package hub.forum.api.domain.pefil;

import com.fasterxml.jackson.annotation.JsonFormat;
import hub.forum.api.domain.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

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

    // validações para cadastro interno
    //fazer pelo mysql todas as regex e validações, se possível.

    @NotBlank(message = "O nome é obrigatório")
    @Pattern(regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ ]+$", message = "O nome deve conter apenas letras e espaços")
    private String nome;

    @NotBlank
    private String descricaoPessoal;

    @NotNull(message = "A data de nascimento é obrigatória")
    @Past(message = "A data de nascimento deve estar no passado")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario  usuario;

    public Perfil(@NotBlank DadosPerfil dadosUsuario) {
        this.nome = dadosUsuario.nome(); //usuário pode alterar o seu próprio perfil
    }
}
