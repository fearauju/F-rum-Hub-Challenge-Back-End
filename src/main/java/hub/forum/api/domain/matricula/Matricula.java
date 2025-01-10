package hub.forum.api.domain.matricula;

import hub.forum.api.domain.curso.Curso;
import hub.forum.api.domain.formacao.Formacao;
import hub.forum.api.domain.usuario.Estudante;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "matriculas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long numeroMatricula; //GERADO AUTOMATICAMENTE

    @Column(nullable = false)
    @PastOrPresent(message = "A data de assinatura não pode estar no futuro")
    private LocalDateTime dataAssinatura;

    @Column(nullable = false)
    @Future(message = "A data de expiração deve estar no futuro")
    private LocalDateTime dataExpiracaoAssinatura;

    @Column(name = "ativa")
    private boolean ativa; //status matricula

    @ManyToOne(optional = false)
    @JoinColumn(name = "estudante_id")
    private Estudante estudante;

    @ManyToOne(optional = false)
    @JoinColumn(name = "formacao_id")
    private Formacao formacao;

    @ElementCollection
    @CollectionTable(
            name = "cursos_matriculados",
            joinColumns = @JoinColumn(name = "matricula_id")
    )
    private List<MatriculaCurso> matriculaCurso = new ArrayList<>();


    //método para gerar número
    @PrePersist
    public void gerarNumeroMatricula() {
        if (this.numeroMatricula == null) {
            // Formato: AnoAtual + Sequencial (ex: 202400001)
            this.numeroMatricula = Long.parseLong(
                    LocalDateTime.now().getYear() +
                            String.format("%05d", id == null ? 1 : id)
            );
        }
    }

    public void cadastrarMatriculaEstudante(Formacao formacao, Estudante estudante){

        this.numeroMatricula = getNumeroMatricula();
        this.dataAssinatura = LocalDateTime.now();
        this.dataExpiracaoAssinatura = dataAssinatura.plusYears(1);
        this.ativa = true;
        this.formacao = formacao;
        this.estudante = estudante;
    }

    public boolean isAtiva() {

        // Verifica e atualiza o status se necessário
        if (ativa && LocalDateTime.now().isAfter(dataExpiracaoAssinatura)) {
            this.ativa = false;
        }
        return this.ativa;
    }

    public void renovarMatricula() {

        this.dataAssinatura = LocalDateTime.now();
        this.dataExpiracaoAssinatura = dataAssinatura.plusYears(1);
        this.ativa = true;
    }

    public void adicionarCurso(Curso curso) {

        if (!possuiCurso(curso)) {
            var matriculaCurso = new MatriculaCurso(this, curso);
            this.matriculaCurso.add(matriculaCurso);
        }
    }

    public boolean possuiCurso(Curso curso) {

        return matriculaCurso.stream()
                .anyMatch(mc -> mc.getCurso().equals(curso));
    }
}