package hub.forum.api.domain.curso;

import hub.forum.api.domain.pefil.Perfil;
import hub.forum.api.domain.topico.Topico;
import hub.forum.api.domain.formacao.Formacao;
import hub.forum.api.domain.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.Valid;
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
    private String curso;
    private Duration duracao;
    private Integer totalDeAlunos;
    private Double avaliacao;

    @ManyToOne
    @JoinColumn(name = "formacao_id")
    private Formacao formacao;

    @ManyToMany
    @JoinTable(
            name = "cursos_perfis",
            joinColumns = @JoinColumn(name = "curso_id"),
            inverseJoinColumns = @JoinColumn(name = "perfil_id")
    )
    private List<Perfil> perfil = new ArrayList<>();

    @OneToMany(mappedBy = "curso")
    private List<Topico> topico = new ArrayList<>();


    public Curso(@Valid DadoscadastroCurso dados, Formacao formacao) {
        this.curso = dados.curso();
        this.duracao = Duration.parse("PT" + dados.duracao().replace(":","H") + "M"); // Transformando duração em horas;
        this.formacao = formacao;
    }
}
