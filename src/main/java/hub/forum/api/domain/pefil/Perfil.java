package hub.forum.api.domain.pefil;

import com.fasterxml.jackson.annotation.JsonFormat;
import hub.forum.api.domain.curso.Curso;
import hub.forum.api.domain.resposta.Resposta;
import hub.forum.api.domain.topico.Topico;
import hub.forum.api.domain.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @NotBlank(message = "O nome é obrigatório")
    @Pattern(regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ ]+$", message = "O nome deve conter apenas letras e espaços")
    private String nome;

    @NotNull(message = "A data de nascimento é obrigatória")
    @Past(message = "A data de nascimento deve estar no passado")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;

    @NotNull(message = "O tipo de perfil é obrigatório e deve ser escrito em letras maiúsculas")
    @Enumerated(EnumType.STRING)
    private TipoPerfil tipoPerfil;

    @OneToOne(mappedBy = "perfil", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    private Usuario  usuario;

    @ManyToMany(mappedBy = "perfil")
    private List<Curso> curso = new ArrayList<>();

    @OneToMany(mappedBy = "perfil", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Topico> topico = new ArrayList<>();

    @OneToMany(mappedBy = "perfil", cascade = CascadeType.ALL)
    private List<Resposta> resposta = new ArrayList<>();

    public Perfil(@NotBlank DadosPerfil dadosUsuario) {
        this.nome = dadosUsuario.nome();
    }
}
