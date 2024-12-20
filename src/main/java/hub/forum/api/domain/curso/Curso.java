package hub.forum.api.domain.curso;

import hub.forum.api.domain.Topico;
import hub.forum.api.domain.formacao.Formacao;
import hub.forum.api.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Curso")
@Table(name = "cursos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Curso {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nome;
    private Duration duracao;
    private Integer totalDeAlunos;
    private Double avaliacao;

    @ManyToOne
    @JoinColumn(name = "formacao_id")
    private Formacao formacao;

    @ManyToMany
    @JoinTable(
            name = "usuarios_cursos",
            joinColumns = @JoinColumn(name = "curso_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<Usuario> usuario = new ArrayList<>();

    @OneToMany(mappedBy = "curso")
    private List<Topico> topico = new ArrayList<>();
}
