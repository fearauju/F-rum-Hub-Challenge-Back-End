package hub.forum.api.domain.curso;

import hub.forum.api.domain.matricula.MatriculaCurso;
import hub.forum.api.domain.topico.Topico;
import hub.forum.api.domain.formacao.Formacao;
import hub.forum.api.domain.matricula.Matricula;
import hub.forum.api.domain.usuario.Estudante;
import hub.forum.api.domain.usuario.Professor;
import hub.forum.api.infra.exceptions.ValidacaoException;
import jakarta.persistence.*;
import lombok.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cursos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Curso {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String curso;

    @Column(nullable = false)
    private Duration duracao;

    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private Integer totalDeAlunos;

    @Column(columnDefinition = "DECIMAL(2,1) DEFAULT 0.0")
    private Double avaliacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formacao_id")
    private Formacao formacao;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "curso_professores",
            joinColumns = @JoinColumn(name = "curso_id"),
            inverseJoinColumns = @JoinColumn(name = "professor_id")
    )
    private List<Professor> professores = new ArrayList<>();

    @OneToMany(mappedBy = "curso", fetch = FetchType.LAZY)
    private List<Topico> topico = new ArrayList<>();

    @OneToMany(mappedBy = "curso", fetch = FetchType.LAZY)
    private List<MatriculaCurso> matriculaCursos = new ArrayList<>();

    public void cadastrarCurso(DadoscadastroCurso dados, Formacao formacao) {

        this.curso = dados.curso().trim();
        this.duracao = parseDuracao(dados.duracao());
        this.formacao = formacao;
        this.totalDeAlunos = 0;
        this.avaliacao = 0.0;
    }

    private Duration parseDuracao(String duracao) {
        return Duration.parse("PT" + duracao.replace(":", "H") + "M");
    }


    public void adicionarProfessor(Professor professor) {
        if (professor != null && !professores.contains(professor)) {
            professores.add(professor);
            professor.getCursos().add(this);
        }
    }

    public void removerProfessor(Professor professor) {
        if (professor != null) {
            professores.remove(professor);
            professor.getCursos().remove(this);
        }
    }

    // Método para atualizar avaliação
    public void atualizarAvaliacao(Double novaAvaliacao) {
        if (novaAvaliacao != null && novaAvaliacao >= 0 && novaAvaliacao <= 10) {
            this.avaliacao = novaAvaliacao;
        } else {
            throw new ValidacaoException("Avaliação deve estar entre 0 e 10");
        }
    }

    public void atualizarDadosCurso(DadosAtualizacaoCurso dados){

        if(dados.nomeCurso() != null){
            this.curso = dados.nomeCurso();
        }

        if(dados.duracao() != null){
            this.duracao = Duration.parse("PT" + dados.duracao().replace(":","H") + "M");
        }
    }
}
