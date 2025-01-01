package hub.forum.api.domain.curso;

import hub.forum.api.domain.topico.Topico;
import hub.forum.api.domain.formacao.Formacao;
import hub.forum.api.domain.matricula.Matricula;
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
    @JoinColumn(name = "formacaoID")
    private Formacao formacao;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL)
    private List<Matricula> matriculas;

    @OneToMany(mappedBy = "curso")
    private List<Topico> topico = new ArrayList<>();


    public Curso(@Valid DadoscadastroCurso dados, Formacao formacao) {
        this.curso = dados.curso();
        this.duracao = Duration.parse("PT" + dados.duracao().replace(":","H") + "M");
        this.formacao = formacao;
    }

    public void atualizarDadosCurso(DadosAtualizacaoCurso dados){
        this.curso = dados.nomeCurso();
        this.duracao = Duration.parse("PT" + dados.duracao().replace(":","H") + "M");
    }
}
