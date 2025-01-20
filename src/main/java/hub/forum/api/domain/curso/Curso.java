package hub.forum.api.domain.curso;

import hub.forum.api.domain.curso.dto.DadosAtualizacaoCurso;
import hub.forum.api.domain.curso.dto.DadoscadastroCurso;
import hub.forum.api.domain.matricula.MatriculaCurso;
import hub.forum.api.domain.topico.Topico;
import hub.forum.api.domain.formacao.Formacao;
import hub.forum.api.domain.usuario.professor.Professor;
import hub.forum.api.domain.util.DurationConverter;
import hub.forum.api.domain.util.DurationUtils;
import hub.forum.api.infra.exceptions.ValidacaoException;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "cursos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Slf4j
public class Curso {

    private static final int MAX_AVALIACAO = 5;
    private static final int MIN_AVALIACAO = 0;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String curso;

    @Column(nullable = false)
    @Convert(converter = DurationConverter.class)
    private Duration duracao;

    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private Integer totalDeAlunos;

    @Column(columnDefinition = "DECIMAL(2,1) DEFAULT 0.0")
    private Double avaliacao;

    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private Integer totalAvaliacoes;

    @ManyToOne
    @JoinColumn(name = "formacao_id", nullable = false)
    private Formacao formacao;

    @ManyToMany(mappedBy = "cursosLecionados")
    private Set<Professor> professores = new HashSet<>();

    @OneToMany(mappedBy = "curso")
    private List<Topico> topicos = new ArrayList<>();

    @OneToMany(mappedBy = "curso")
    private List<MatriculaCurso> matriculaCursos = new ArrayList<>();

    public void cadastrarCurso(DadoscadastroCurso dados, Formacao formacao, List<Professor> professores) {

        this.curso = dados.curso().trim();
        this.duracao = DurationUtils.parseDuracao(dados.duracao());
        this.formacao = formacao;
        this.totalDeAlunos = 0;
        this.avaliacao = 0.0;

        for(Professor professor : professores){
            adicionarProfessor(professor);
        }
    }


    public void adicionarProfessor(Professor professor) {
        if (professor == null) return;

        if (this.professores.contains(professor)) {
            log.warn("Professor {} já está associado ao curso {}", professor.getId(), this.getId());
            return;
        }

        this.professores.add(professor);
        professor.adicionarCurso(this, this.duracao);
    }

    public void removerProfessor(Professor professor) {
        if (professor != null) {
            if (this.professores.remove(professor)) {
                professor.getCursosLecionados().remove(this);
            }
        }
    }

    public void incrementarTotalAlunos() {
        this.totalDeAlunos++;
    }

    public void decrementarTotalAlunos() {
        if (this.totalDeAlunos > 0) {
            this.totalDeAlunos--;
        }
    }

    public void avaliarCurso(Double novaAvaliacao) {
        if (novaAvaliacao == null || novaAvaliacao < MIN_AVALIACAO || novaAvaliacao > MAX_AVALIACAO) {
            throw new ValidacaoException(
                    String.format("Avaliação deve estar entre %d e %d", MIN_AVALIACAO, MAX_AVALIACAO)
            );
        }

        if (this.totalAvaliacoes == null) {
            this.totalAvaliacoes = 0;
        }

        // Calcula a nova média
        double somaAtual = this.avaliacao * this.totalAvaliacoes;
        this.totalAvaliacoes++;
        this.avaliacao = (somaAtual + novaAvaliacao) / this.totalAvaliacoes;

        log.info("Curso {} recebeu nova avaliação. Média atual: {}", this.curso, this.avaliacao);
    }

    public void atualizarDadosCurso(DadosAtualizacaoCurso dados){
        if(dados.nomeCurso() != null){
            this.curso = dados.nomeCurso().trim();
        }

        if(dados.duracao() != null){
            this.duracao = DurationUtils.parseDuracao(dados.duracao());
        }
    }
}
